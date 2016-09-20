#include "slamBase.h"
#include <g2o/types/slam3d/types_slam3d.h>
#include <g2o/core/sparse_optimizer.h>
#include <g2o/core/block_solver.h>
#include <g2o/core/factory.h>
#include <g2o/core/optimization_algorithm_factory.h>
#include <g2o/core/optimization_algorithm_gauss_newton.h>
#include <g2o/solvers/csparse/linear_solver_csparse.h>
#include <g2o/core/robust_kernel.h>
#include <g2o/core/robust_kernel_factory.h>
#include <g2o/core/optimization_algorithm_levenberg.h>

// 给定index，读取一帧数据
FRAME readFrame(int index,ParameterReader* pr){
	FRAME frame;
	string rgbDir = pr->getData("rgb_dir");
	string depthDir = pr->getData("depth_dir");
	string rgbExt = pr->getData("rgb_extension");
	string depthExt = pr->getData("depth_extension");
	stringstream ss;
	ss<<rgbDir<<index<<rgbExt;
	string filename;
	ss>>filename;
	frame.rgb = cv::imread(filename);
	ss.clear();
	filename.clear();
	ss<<depthDir<<index<<depthExt;
	ss>>filename;
	frame.depth = cv::imread(filename,-1);
	frame.frameID = index;
	return frame;
}

// 度量运动的大小
double normofTransform(cv::Mat rvec,cv::Mat tvec){
	return fabs(min(cv::norm(rvec),2*M_PI-cv::norm(rvec)))+fabs(cv::norm(tvec));
}

int main(int argc,char** argv){
	ParameterReader* pr = new ParameterReader();
	
	int startIndex = atoi((pr->getData("start_index")).c_str());
	int endIndex = atoi((pr->getData("end_index")).c_str());
	
	// initialize
	cout<<"Initializing ..."<<endl;
	int currIndex = startIndex;
	FRAME lastFrame = readFrame(currIndex,pr);
	string detector = pr->getData("detector");
	string descriptor = pr->getData("descriptor");
	CAMERA_INTRINSIC_PARAMETERS camera = getDefaultCamera();
	computeKeyPointsAndDesp(lastFrame,detector,descriptor);
	PointCloud::Ptr cloud = image2PointCloud(lastFrame.rgb,lastFrame.depth,camera);
	pcl::visualization::CloudViewer viewer("viewer");
	
	// 是否显示点云
	bool visualize = strcmp((pr->getData("visualize_pointcloud").c_str()),"yes")==0;
	int min_inliers = atoi((pr->getData("min_inliers")).c_str());
	double max_norm = atof((pr->getData("max_norm")).c_str());
	double good_match_threshold = atof((pr->getData("good_match_threshold")).c_str());
	int min_good_match = atoi((pr->getData("min_good_match")).c_str());
	
	// 选择优化方法
	typedef g2o::BlockSolver_6_3 SlamBlockSolver;
	typedef g2o::LinearSolverCSparse<SlamBlockSolver::PoseMatrixType> SlamLinearSolver;

	// 初始化求解器
	SlamLinearSolver* linearSolver = new SlamLinearSolver();
	linearSolver->setBlockOrdering(false);
	SlamBlockSolver* blockSolver = new SlamBlockSolver(linearSolver);
	g2o::OptimizationAlgorithmLevenberg* solver = new g2o::OptimizationAlgorithmLevenberg(blockSolver);

	g2o::SparseOptimizer globalOptimizer;
	globalOptimizer.setAlgorithm(solver);
	globalOptimizer.setVerbose(false);	// 不要输出调试信息

	// 向globalOptimizer增加第一个顶点
	g2o::VertexSE3* v = new g2o::VertexSE3();
	v->setId(currIndex);
	v->setEstimate(Eigen::Isometry3d::Identity());	// 估计为单位矩阵
	v->setFixed(true);	// 第一个顶点固定，不用优化
	globalOptimizer.addVertex(v);
	int lastIndex = currIndex;	// 上一帧的id
	for(currIndex=startIndex+1;currIndex<endIndex;currIndex++){
		cout<<"Reading files "<<currIndex<<endl;
		FRAME currFrame = readFrame(currIndex,pr);
		computeKeyPointsAndDesp(currFrame,detector,descriptor);
		// 比较currFrame 和 lastFrame
		RESULT_OF_PNP result = estimateMotion(lastFrame,currFrame,camera,good_match_threshold,min_good_match);
		if(result.inliers < min_inliers){	// inliers不够，放弃该帧
			cout<<"Number of Inliers below min_inliers,Abandon."<<endl;
			continue;
		}
		// 计算运动范围是否太大
		double norm = normofTransform(result.rvec,result.tvec);
		//cout<<"norm = "<<norm<<endl;
		if(norm >= max_norm){
			cout<<"Camera motion is fast,Adandon."<<endl;
			continue;
		}
		Eigen::Isometry3d T = cvMat2Eigen(result.rvec,result.tvec);
		cout<<"T = "<<T.matrix()<<endl;

		//cloud = joinPointCloud(cloud,currFrame,T,camera);
		
		// 向g2o中增加这个顶点与上一帧联系的边
		g2o::VertexSE3* v = new g2o::VertexSE3();
		v->setId(currIndex);
		v->setEstimate(Eigen::Isometry3d::Identity());
		globalOptimizer.addVertex(v);
		// 边部分
		g2o::EdgeSE3* edge = new g2o::EdgeSE3();
		// 连接此边的两个顶点id
		edge->vertices()[0] = globalOptimizer.vertex(lastIndex);
		edge->vertices()[1] = globalOptimizer.vertex(currIndex);
		// 信息矩阵
		Eigen::Matrix<double,6,6> information = Eigen::Matrix< double, 6,6 >::Identity();
		// 信息矩阵是协方差矩阵的逆，表示我们对边的精度的预先估计
		// 因为pose为6D的，信息矩阵是6*6的阵，假设位置和角度的估计精度均为0.1且互相独立
		// 那么协方差则为对角为0.01的矩阵，信息阵则为100的矩阵
		information(0,0) = information(1,1) = information(2,2) = 100;
		information(3,3) = information(4,4) = information(5,5) = 100;
		edge->setInformation(information);
		// 边的估计即是pnp求解之结果
		edge->setMeasurement(T);
		// 将此边加入图中
		globalOptimizer.addEdge(edge);
		lastFrame = currFrame;
		lastIndex = currIndex;
		/*
		if(visualize == true){
			viewer.showCloud(cloud);
		}
		*/
	}
	// 优化所有边
	cout<<"optimizing pose graph, vertices: "<<globalOptimizer.vertices().size()<<endl;
	globalOptimizer.save("../pic/result_before.g2o");
	globalOptimizer.initializeOptimization();
	globalOptimizer.optimize(100);
	globalOptimizer.save("../pic/result_after.g2o");
	cout<<"Optimization done."<<endl;
	globalOptimizer.clear();
	//pcl::io::savePCDFile("../pic/result.pcd",*cloud);
	//cloud->points.clear();
	return 0;
}
