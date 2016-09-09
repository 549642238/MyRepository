/**
  * This tutorial demonstrates simple receipt of messages over the
	ROS system.
*/
#include <iostream>
#include "ros/ros.h"
#include "my_message/Num.h"
using namespace std;

void chatterCallback(const my_message::Num::ConstPtr& msg){
	ROS_INFO("I heard:%d - [%s]", msg->num,msg->name.c_str());
}

int main(int argc, char **argv){
	ros::init(argc, argv, "listener");
	ros::NodeHandle n;
	ros::Subscriber sub = n.subscribe("chatter", 1000, chatterCallback);
	ros::spin();
	return 0;
}
