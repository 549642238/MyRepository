#include <iostream>
#include <string>
#include <pcl/io/pcd_io.h>
#include <pcl/point_types.h>
#include <octomap/octomap.h>
#include <octomap/ColorOcTree.h>

using namespace std;

int main(int argc,char** argv){
	if(argc != 3){
		cout<<"Usage pcd2octomap <input_file> <output_file>"<<endl;
		return -1;
	}
	string input_file = argv[1],output_file = argv[2];
	pcl::PointCloud<pcl::PointXYZRGBA> cloud;
	pcl::io::loadPCDFile<pcl::PointXYZRGBA> (input_file,cloud);
	cout<<"point cloud loaded, point size = "<<cloud.points.size()<<endl;
	cout<<"copy data into octomap..."<<endl;
	octomap::ColorOcTree tree(0.05);
	for(int i=0;i<cloud.points.size();i++){
		tree.updateNode(octomap::point3d(cloud.points[i].x,cloud.points[i].y,cloud.points[i].z),true);
	}
	for(int i=0;i<cloud.points.size();i++){
		tree.integrateNodeColor(cloud.points[i].x,cloud.points[i].y,cloud.points[i].z,cloud.points[i].r,cloud.points[i].g,cloud.points[i].b);
	}
	tree.updateInnerOccupancy();
	//tree.writeBinary(output_file);	// .bt file
	tree.write(output_file);			// .ot file
	cout<<"done"<<endl;
	return 0;
}
