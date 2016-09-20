#include "slamBase.h"

int main(int argc,char** argv){
	ParameterReader* pr = new ParameterReader("../parameters.txt");
	CAMERA_INTRINSIC_PARAMETERS camera;
	if(strcmp((pr->getData("camera.cx")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("camera.cy")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("camera.fx")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("camera.fy")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("camera.scale")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("detector")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("descriptor")).c_str(),"NOT_FOUND")==0 || strcmp((pr->getData("good_match_threshold")).c_str(),"NOT_FOUND")==0){
		cout<<"Parameter lost."<<endl;
		return -1;
	}
	camera.cx = atof((pr->getData("camera.cx")).c_str());
	camera.cy = atof((pr->getData("camera.cy")).c_str());
	camera.fx = atof((pr->getData("camera.fx")).c_str());
	camera.fy = atof((pr->getData("camera.fy")).c_str());
	camera.scale = atof((pr->getData("camera.scale")).c_str());
	int min_good_match = atoi((pr->getData("min_good_match")).c_str());
	/*
	cv::Mat rgb,depth;
	rgb = cv::imread("../pic/color.png");
	depth = cv::imread("../pic/depth.png",-1);
	PointCloud::Ptr cloud = image2PointCloud(rgb,depth,camera);
	cloud->points.clear();
	cv::Point3f point;
	point.x = 50;
	point.y = 100;
	point.z = 1200;
	cv::Point3f p = point2dTo3d(point,camera);
	cout<<"2D point ("<<point.x<<","<<point.y<<") to "<<"3D point ("<<p.x<<","<<p.y<<","<<p.z<<")"<<endl;
	*/
	double good_match_threshold = atof((pr->getData("good_match_threshold")).c_str());
	FRAME frame1,frame2;
	frame1.rgb = cv::imread("../pic/color1.png");
	frame1.depth = cv::imread("../pic/depth1.png");
	frame2.rgb = cv::imread("../pic/color2.png");
	frame2.depth = cv::imread("../pic/depth2.png");
	string detector = pr->getData("detector");
	string descriptor = pr->getData("descriptor");
	computeKeyPointsAndDesp(frame1,detector,descriptor);
	computeKeyPointsAndDesp(frame2,detector,descriptor);
	RESULT_OF_PNP motion = estimateMotion(frame1,frame2,camera,good_match_threshold,min_good_match);
	cout<<"Inliers = "<<motion.inliers<<endl;
	cout<<"R = "<<motion.rvec<<endl;
	cout<<"t = "<<motion.tvec<<endl;
	//joinPointCloud(frame1,frame2,camera,motion);
	return 0;
}
