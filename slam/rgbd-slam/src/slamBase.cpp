#include "slamBase.h"

PointCloud::Ptr image2PointCloud(cv::Mat& rgb,cv::Mat& depth,CAMERA_INTRINSIC_PARAMETERS& camera){
	PointCloud::Ptr cloud(new PointCloud);
	for(int m=0;m<depth.rows;m++){
		for(int n=0;n<depth.cols;n++){
			unsigned short d = depth.ptr<unsigned short>(m)[n];
			if(d == 0){
				continue;
			}
			PointT p;
			p.z = double(d)/camera.scale;
			p.x = (n-camera.cx)*p.z/camera.fx;
			p.y = (m-camera.cy)*p.z/camera.fy;

			p.b = rgb.ptr<unsigned char>(m)[n*3];
			p.g = rgb.ptr<unsigned char>(m)[n*3+1];
			p.r = rgb.ptr<unsigned char>(m)[n*3+2];
			cloud->points.push_back(p);
		}
	}
	cloud->height = 1;
	cloud->width = cloud->points.size();
	//cout<<"point cloud size = "<<cloud->points.size()<<endl;
	cloud->is_dense = false;
	//pcl::io::savePCDFile("../pic/pointcluod.pcd",*cloud);
	//cloud->points.clear();
	//cout<<"Point cloud saved."<<endl;
	return cloud;
}

cv::Point3f point2dTo3d(cv::Point3f& point,CAMERA_INTRINSIC_PARAMETERS& camera){
	cv::Point3f p;
	p.z = double(point.z)/camera.scale;
	p.x = (point.x-camera.cx)*p.z/camera.fx;
	p.y = (point.y-camera.cy)*p.z/camera.fy;
	return p;
}

void computeKeyPointsAndDesp(FRAME& frame,string detector,string descriptor){
	// 声明特征提取器与描述子提取器
	cv::Ptr<cv::FeatureDetector> _detector;
	cv::Ptr<cv::DescriptorExtractor> _descriptor;

	// 构建提取器，默认两者都为sift
	// 构建sift, surf之前要初始化nonfree模块
	cv::initModule_nonfree();
	_detector = cv::FeatureDetector::create(detector.c_str());
	_descriptor = cv::DescriptorExtractor::create(descriptor.c_str());
	_detector->detect(frame.rgb,frame.kp);    // 提取关键点
	//cout<<"Key points of image: "<<frame.kp.size()<<endl;
	/*
	// 可视化， 显示关键点
	cv::Mat imgShow;
	cv::drawKeypoints(frame.rgb,frame.kp,imgShow,cv::Scalar::all(-1),cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imshow("keypoints",imgShow);
	cv::imwrite("../pic/keypoints.png",imgShow);
	cv::waitKey(0);     // 暂停等待一个按键
	*/
	// 计算描述子
	_descriptor->compute(frame.rgb,frame.kp,frame.desp);
}

RESULT_OF_PNP estimateMotion( FRAME& frame1, FRAME& frame2, CAMERA_INTRINSIC_PARAMETERS& camera, double good_match_threshold, int min_good_match){
	// 匹配描述子
	vector<cv::DMatch> matches;
	cv::FlannBasedMatcher matcher;
	matcher.match(frame1.desp,frame2.desp,matches);
	//cout<<"Find total "<<matches.size()<<" matches."<<endl;
	/*
	// 可视化：显示匹配的特征
	cv::Mat imgMatches;
	cv::drawMatches(frame1.rgb,frame1.kp,frame2.rgb,frame2.kp,matches,imgMatches);
	cv::imshow("matches",imgMatches);
	cv::imwrite("../pic/matches.png",imgMatches);
	cv::waitKey(0);
	*/
	// 筛选匹配，把距离太大的去掉
	// 这里使用的准则是去掉大于四倍最小距离的匹配
	vector<cv::DMatch> goodMatches;
	double minDis = 9999;
	for(size_t i=0;i<matches.size();i++){
		if(matches[i].distance<minDis){
			minDis = matches[i].distance;
		}
	}
	for(size_t i=0;i<matches.size();i++){
		if(matches[i].distance < good_match_threshold*minDis){
			goodMatches.push_back(matches[i]);
		}
	}
	
	/*
	cv::drawMatches(frame1.rgb,frame1.kp,frame2.rgb,frame2.kp,goodMatches,imgMatches);
	cv::imshow("good matches",imgMatches);
	cv::imwrite("../pic/good_matches.png",imgMatches);
	cv::waitKey(0);
	*/
	// 计算图像间的运动关系
	// 关键函数：cv::solvePnPRansac()
	// 为调用此函数准备必要的参数

	// 第一个帧的三维点
	vector<cv::Point3f> pts_obj;
	// 第二个帧的图像点
	vector<cv::Point2f> pts_img;
	RESULT_OF_PNP result;
	
	if(goodMatches.size() <  min_good_match){
		result.inliers = 0;
	}else{	
		// 显示 good matches
		cout<<"good matches = "<<goodMatches.size()<<endl;
		for(size_t i=0;i<goodMatches.size();i++){
			// query 是第一个, train 是第二个
			cv::Point2f p = frame1.kp[goodMatches[i].queryIdx].pt;
			// 获取d是要小心！x是向右的，y是向下的，所以y才是行，x是列！
			unsigned short d = frame1.depth.ptr<unsigned short>(int(p.y))[int(p.x)];
			if(d == 0){
				continue;
			}
			pts_img.push_back(cv::Point2f(frame2.kp[goodMatches[i].trainIdx].pt));
			// 将(u,v,d)转成(x,y,z)
			cv::Point3f pt(p.x,p.y,d);
			cv::Point3f pd = point2dTo3d(pt,camera);
			pts_obj.push_back(pd);
		}
		if(pts_obj.size() == 0){
			result.inliers = 0;
			return result;
		}
		double camera_matrix_data[3][3] = {
			{camera.fx,0,camera.cx},
			{0,camera.fy,camera.cy},
			{0,0,1}
		};
	
		// 构建相机矩阵
		cv::Mat cameraMatrix(3,3,CV_64F,camera_matrix_data);
		cv::Mat inliers;
		// 求解pnp
		cv::solvePnPRansac(pts_obj,pts_img,cameraMatrix,cv::Mat(),result.rvec,result.tvec,false,100,1.0,100,inliers);
		result.inliers = inliers.rows;
	}
	
	/*
	// 画出inliers匹配
	vector<cv::DMatch> matchesShow;
	for(size_t i=0;i<result.inliers;i++){
		matchesShow.push_back(goodMatches[inliers.ptr<int>(i)[0]]);
	}
	cv::drawMatches(frame1.rgb,frame1.kp,frame2.rgb,frame2.kp,matchesShow,imgMatches);
	cv::imshow("inlier matches",imgMatches);
	cv::imwrite("../pic/inliers.png",imgMatches);
	cv::waitKey(0);
	*/
	return result;
}

Eigen::Isometry3d cvMat2Eigen(cv::Mat& rvec,cv::Mat& tvec){
	cv::Mat R;
	cv::Rodrigues(rvec,R);
	Eigen::Matrix3d r;
	cv::cv2eigen(R,r);
	Eigen::Isometry3d T = Eigen::Isometry3d::Identity();
	Eigen::AngleAxisd angle(r);
	T = angle;
	T(0,3) = tvec.at<double>(0,0);
	T(1,3) = tvec.at<double>(1,0);
	T(2,3) = tvec.at<double>(2,0);
	return T;
}

PointCloud::Ptr joinPointCloud( PointCloud::Ptr original, FRAME& newFrame, Eigen::Isometry3d T, CAMERA_INTRINSIC_PARAMETERS& camera ){
	PointCloud::Ptr newCloud = image2PointCloud(newFrame.rgb,newFrame.depth,camera);
	
	// 合并点云
	PointCloud::Ptr output(new PointCloud());
	pcl::transformPointCloud(*original,*output,T.matrix());
	*newCloud += *output;

	// Voxel grid 滤波降采样
	static pcl::VoxelGrid<PointT> voxel;
	static ParameterReader pd;
	double gridsize = atof(pd.getData("voxel_grid").c_str());
	voxel.setLeafSize(gridsize,gridsize,gridsize);
	voxel.setInputCloud(newCloud);
	PointCloud::Ptr tmp(new PointCloud());
	voxel.filter(*tmp);
	return tmp;
}
