#include <iostream>
#include <vector>

// octomap 
#include <octomap/octomap.h>
#include <octomap/ColorOcTree.h>
#include <octomap/math/Pose6D.h>

// opencv 用于图像数据读取与处理
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

// 使用Eigen的Geometry模块处理3d运动
#include <Eigen/Core>
#include <Eigen/Geometry> 

// pcl
#include <pcl/common/transforms.h>
#include <pcl/point_types.h>

// boost.format 字符串处理
#include <boost/format.hpp>

float camera_scale  = 1000;
float camera_cx     = 325.5;
float camera_cy     = 253.5;
float camera_fx     = 518.0;
float camera_fy     = 519.0;

using namespace std;

int main(int argc,char** argv){
	ifstream fin("../data/keyframe.txt");
	vector<int> keyframes;
	vector<Eigen::Isometry3d> poses;
	while(fin.peek() != EOF){
		int index_keyframe;
		fin>>index_keyframe;
		if(fin.fail()){
			break;
		}
		keyframes.push_back(index_keyframe);
	}
	fin.close();
	cout<<"load total"<<keyframes.size()<<" keyframes."<<endl;
	fin.open("../data/trajectory.txt");
	while(fin.peek() != EOF){
		int index_keyframe;
		float data[7];
		fin>>index_keyframe;
		for(int i=0;i<7;i++){
			fin>>data[i];
			cout<<data[i]<<" ";
		}
		cout<<endl;
		if(fin.fail()){
			break;
		}
		Eigen::Quaterniond q(data[6],data[3],data[4],data[5]);
		Eigen::Isometry3d t(q);
		t(0,3) = data[0];
		t(1,3) = data[1];
		t(2,3) = data[2];
		poses.push_back(t);
	}
	fin.close();
	octomap::ColorOcTree tree(0.05);
	for(size_t i=0;i<keyframes.size();i++){
		pcl::PointCloud<pcl::PointXYZRGBA> cloud;
		cout<<"converting "<<i<<"th keyframe ..."<<endl;
		int k = keyframes[i];
		Eigen::Isometry3d& pose = poses[i];
		boost::format fmt("../data/rgb_index/%d.ppm");
		cv::Mat rgb = cv::imread((fmt%k).str().c_str());
		fmt = boost::format("../data/dep_index/%d.pgm");
		cv::Mat depth = cv::imread((fmt%k).str().c_str(),-1);
		for(int m=0;m<depth.rows;m++){
			for(int n=0;n<depth.cols;n++){
				unsigned short d = depth.ptr<unsigned short> (m)[n];
				if(d == 0){
					continue;
				}
				float z = (float)d/camera_scale;
				float x = (n-camera_cx)*z/camera_fx;
				float y = (m-camera_cy)*z/camera_fy;
				pcl::PointXYZRGBA p;
				p.x = x;
				p.y = y;
				p.z = z;
				unsigned char* rgbdata = &rgb.ptr<unsigned char>(m)[n*3];
				unsigned char b = rgbdata[0];
				unsigned char g = rgbdata[1];
				unsigned char r = rgbdata[2];
				p.r = r;
				p.g = g;
				p.b = b;
				cloud.points.push_back(p);
			}
		}
		pcl::PointCloud<pcl::PointXYZRGBA>::Ptr temp(new pcl::PointCloud<pcl::PointXYZRGBA>());
		pcl::transformPointCloud(cloud,*temp,pose.matrix());
		octomap::Pointcloud cloud_octo;
		for(int i=0;i<temp->points.size();i++){
			cloud_octo.push_back(temp->points[i].x,temp->points[i].y,temp->points[i].z);
		}
		tree.insertPointCloud(cloud_octo,octomap::point3d(pose(0,3),pose(1,3),pose(2,3)));
		for(int i=0;i<temp->points.size();i++){
			tree.integrateNodeColor(temp->points[i].x,temp->points[i].y,temp->points[i].z,temp->points[i].r,temp->points[i].g,temp->points[i].b);
		}
	}
	tree.updateInnerOccupancy();
	tree.write("../data/map.ot");
	cout<<"Done."<<endl;
	return 0;
}
