'''
Multi-threads,one thread used to socket receiving and write file,the other used to read file and execute orders.The motions of NAO look like more smoothy
''' 
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
postureProxy = ALProxy("ALRobotPosture", robot_ip,robot_port)
motionProxy.wakeUp()
time.sleep(1)
enable = True
chainName = "Arms"
isSuccess = motionProxy.setCollisionProtectionEnabled(chainName, enable)

def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll):
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]	
	fractionMaxSpeed  = 0.3
	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
	print "---------------- Human Motion Imitation -----------------"


#Read File & Execute Orders
def execute(fr):
	while 1:
		try:
			line = (fr.readline()).strip("\00")
			if line:
				print "\n=============== New Joints's Settings ==============="
				data = line.split(',')
				print data
				#RARM
				RShoulderPitch = string.atof(data[0])
				RShoulderRoll = string.atof(data[1])
				RElbowRoll = string.atof(data[2])
				RElbowYaw =  string.atof(data[3])
				#LARM
				LShoulderPitch = string.atof(data[4])
				LShoulderRoll = string.atof(data[5])
				LElbowRoll = string.atof(data[6])
				LElbowYaw = string.atof(data[7])
				setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll)
		except Exception,e:
			continue				#Somtimes some errors occur while reading file in Python,just ignore them 
			print "File Error!"
			print e
			fr.close()
			break

fr = open("posture.txt", "r")
execute(fr)
fr.close()
