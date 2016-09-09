/**
  * This tutorial demonstrates simple sending of messages over the
	ROS system.
*/

#include <iostream>
#include <sstream>
#include "ros/ros.h"
#include "my_message/Num.h"

using namespace std;

int main(int argc, char **argv){
	ros::init(argc, argv, "talker");
	ros::NodeHandle n;
	ros::Publisher chatter_pub = n.advertise<my_message::Num>("chatter", 1000);
	ros::Rate loop_rate(10);
	int count = 0;
	while(ros::ok()){
		my_message::Num msg;
		std::stringstream ss;
		ss << "hello world "<<count;
		msg.name = ss.str();
		msg.num = count;
		ROS_INFO("%s", msg.name.c_str());
		chatter_pub.publish(msg);
		ros::spinOnce();
		loop_rate.sleep();
		++count;
	}
	return 0;
}
