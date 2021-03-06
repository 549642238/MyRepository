import socket
import string  
from naoqi import ALProxy
import argparse
import almath
import motion
import time
import thread

robot_ip = '192.168.1.100'
robot_port = 9559
motionProxy=ALProxy("ALMotion",robot_ip,robot_port)
motionProxy.wakeUp()
enable = True
chainName = "Arms"
isSuccess = motionProxy.setCollisionProtectionEnabled(chainName, enable)
time.sleep(1)


def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period):
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]
	fractionMaxSpeed  = 0.3
	
	frame           = motion.FRAME_TORSO
    	useSensorValues = True
	result1         = motionProxy.getPosition("RElbowRoll", frame, useSensorValues)
	result2         = motionProxy.getPosition("RWristYaw", frame, useSensorValues)
	result3         = motionProxy.getPosition("LElbowRoll", frame, useSensorValues)
	result4         = motionProxy.getPosition("LWristYaw", frame, useSensorValues)
		
	motionProxy.setAngles(names, angles, fractionMaxSpeed)
	time.sleep(0.1)
    	result11        = motionProxy.getPosition("RElbowRoll", frame, useSensorValues)
	result22        = motionProxy.getPosition("RWristYaw", frame, useSensorValues)
	result33        = motionProxy.getPosition("LElbowRoll", frame, useSensorValues)
	result44        = motionProxy.getPosition("LWristYaw", frame, useSensorValues)
    	
	result = sqrt((result11[0]-result1[0])*(result11[0]-result1[0])+(result11[1]-result1[1])*(result11[1]-result1[1])+(result11[2]-result1[2])*(result11[2]-result1[2]))+sqrt((result22[0]-result2[0])*(result22[0]-result2[0])+(result22[1]-result2[1])*(result22[1]-result2[1])+(result22[2]-result2[2])*(result22[2]-result2[2]))+sqrt((result33[0]-result3[0])*(result33[0]-result3[0])+(result33[1]-result3[1])*(result33[1]-result3[1])+(result33[2]-result3[2])*(result33[2]-result3[2]))+sqrt((result44[0]-result4[0])*(result44[0]-result4[0])+(result44[1]-result4[1])*(result44[1]-result4[1])+(result44[2]-result4[2])*(result44[2]-result4[2]))

	while result>0.005:
		result1 = result11
		result2 = result22
		result3 = result33
		result4 = result44
		time.sleep(0.1)
		result11        = motionProxy.getPosition("RElbowRoll", frame, useSensorValues)
		result22        = motionProxy.getPosition("RWristYaw", frame, useSensorValues)
		result33        = motionProxy.getPosition("LElbowRoll", frame, useSensorValues)
		result44        = motionProxy.getPosition("LWristYaw", frame, useSensorValues)
		result = sqrt((result11[0]-result1[0])*(result11[0]-result1[0])+(result11[1]-result1[1])*(result11[1]-result1[1])+(result11[2]-result1[2])*(result11[2]-result1[2]))+sqrt((result22[0]-result2[0])*(result22[0]-result2[0])+(result22[1]-result2[1])*(result22[1]-result2[1])+(result22[2]-result2[2])*(result22[2]-result2[2]))+sqrt((result33[0]-result3[0])*(result33[0]-result3[0])+(result33[1]-result3[1])*(result33[1]-result3[1])+(result33[2]-result3[2])*(result33[2]-result3[2]))+sqrt((result44[0]-result4[0])*(result44[0]-result4[0])+(result44[1]-result4[1])*(result44[1]-result4[1])+(result44[2]-result4[2])*(result44[2]-result4[2]))
	#time.sleep(period)
	print "---------------- Human Motion Imitation -----------------"

#Read File & Execute Orders
def execute(fp):
	i = 0
	while 1:
		try:
			line = (fp.readline()).strip("\00")
			if line:
				print "\n=============== New Joints's Settings ==============="
				data = line.split(',')
				print data
				#RARM:
				RShoulderPitch = string.atof(data[0])
				RShoulderRoll = string.atof(data[1])
				RElbowRoll = string.atof(data[2])
				RElbowYaw =  string.atof(data[3])
				#LARM:
				LShoulderPitch = string.atof(data[4])
				LShoulderRoll = string.atof(data[5])
				LElbowRoll = string.atof(data[6])
				LElbowYaw = string.atof(data[7])
				#RLEG
				RHipYawPitch = string.atof(data[8])
				RHipRoll = string.atof(data[9])
				RHipPitch = string.atof(data[10])
				RKneePitch = string.atof(data[11])
				#LLEG
				LHipYawPitch = string.atof(data[12])
				LHipRoll = string.atof(data[13])
				LHipPitch = string.atof(data[14])
				LKneePitch = string.atof(data[15])
				#Period
				period = string.atof(data[16])
				setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period)
		except Exception,e:
			continue				#Somtimes some errors occur while reading file in Python,just ignore them 
			print "File Error!"
			print e
			fp.close()
			break
	thread.exit_thread()


fp = open("orders.txt", "r")
execute(fp)
f.close()
fp.close()
