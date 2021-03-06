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
smooth_coefficient = 0.05				#Smooth processing,the larger smooth_coefficient correspons to a more smoothy motion,but the motion maybe not on position,the smaller smooth_coefficient corresponds to a mechanical motion
errFile = open("err.txt", "w")
'''
rhandx = open("rhandx.txt", "w")
rhandy = open("rhandy.txt", "w")
rhandz = open("rhandz.txt", "w")
'''

#Standard Pose of Lower Body
def lowerReference():
	RHipRoll = -5.72957808107;
	RHipPitch = 1.44845112119;
	RKneePitch = -5.15662040103;
	RAnklePitch = 5.15662040103;
	RAnkleRoll = 7.44845112119;
	LHipRoll = 5.72957808107;
	LHipPitch = 1.44845112119;
	LKneePitch = -5.15662040103;
	LAnklePitch = 5.15662040103;
	LAnkleRoll = -7.44845112119;
	names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
	angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    	fractionMaxSpeed  = 0.1
    	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

#Standard Pose of Left Support
def LLegRef():
	names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
	angles = [0.1,0.13,-0.09,0.09,-0.13,0.13,-0.1,-0.09,0.09,0.13]
	fractionMaxSpeed  = 0.1
	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

#Standard Pose of Left Support transporting to Double support 
def LLegtoDouble():
	names  = ["RHipPitch","RKneePitch","RHipRoll","RAnklePitch","RAnkleRoll","LAnkleRoll","LHipRoll","LKneePitch","LAnklePitch"]
	angles = [0*almath.TO_RAD,-5*almath.TO_RAD,5*almath.TO_RAD,0.05,0.1,-0.13,0.1,10*almath.TO_RAD,-0.09]
	fractionMaxSpeed  = 0.1
	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

#Standard Pose of Reft Support
def RLegRef():
	names  = ["LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll","RHipPitch","RHipRoll","RKneePitch","RAnklePitch","RAnkleRoll"]
	angles = [0.3,0.23,0.23,-0.23,-0.3,0.1,0.13,-0.09,0,-0.23]
	fractionMaxSpeed  = 0.1
	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

#Standard Pose of Reft Support transporting to Double support 
def RLegtoDouble():
	names  = ["LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll","RHipPitch","RHipRoll","RKneePitch","RAnklePitch","RAnkleRoll"]
	angles = [0.2,0.13,-0.09,0.09,-0.23,0.1,0.13,-0.09,0.09,-0.2]
	fractionMaxSpeed  = 0.1
	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll,LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll,period,supportMode,supportOrder,doubleSupportConfig):
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]	
	fractionMaxSpeed  = 0.3
	motionProxy.setAngles(names, angles, fractionMaxSpeed)
	time.sleep(smooth_coefficient*period)
	'''
	jointName = "RHand"
	jointFrame = motion.FRAME_TORSO
    	useSensorValues = True
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	rhandx.write(str(jointPosition[0])+",")
	rhandy.write(str(jointPosition[1])+",")
	rhandz.write(str(jointPosition[2])+",")
	rhandx.flush()
	rhandy.flush()
	rhandz.flush()
	'''
	'''
	if (supportMode == 0 and doubleSupportConfig == 1):
		names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
		angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    		fractionMaxSpeed  = 0.1
    		motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
	'''	
	if supportOrder == 0:
		if (supportMode == 0 and doubleSupportConfig == 1) or (supportMode != 0):
			names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
			angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    			fractionMaxSpeed  = 0.1
    			motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
	else:
		lowerReference()
		if supportOrder == 1:
			motionProxy.wbEnable(True)
			supportLeg = "LLeg"
    			duration   = 1.0
    			motionProxy.wbGoToBalance(supportLeg, duration)
			motionProxy.wbEnable(False)
			LLegRef()
		elif supportOrder == 2:
			motionProxy.wbEnable(True)
			supportLeg = "RLeg"
    			duration   = 1.0
    			motionProxy.wbGoToBalance(supportLeg, duration)
			motionProxy.wbEnable(False)
			RLegRef()
		elif supportOrder == 3:
			#LLegtoDouble()
			#lowerReference()
			pass
		elif supportOrder == 4:
			#RLegtoDouble()
			#lowerReference()
			pass
	'''
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll","RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD,RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]
	'''
	'''
	jointName = "LElbowRoll"
    	jointFrame = motion.FRAME_TORSO
    	useSensorValues = True
    	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "RElbowRoll"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "LHand"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "RHand"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "LKneePitch"
    	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "RKneePitch"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "LAnklePitch"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])
	jointName = "RAnklePitch"
	jointPosition = motionProxy.getPosition(jointName, jointFrame, useSensorValues)
	location = location+" "+str(jointPosition[0])+" "+str(jointPosition[1])+" "+str(jointPosition[2])+"\r\n"
	errFile.write(location)					#Actual position of each joint
	errFile.flush()
	'''
	print "---------------- Human Motion Imitation -----------------"

#Receive Thread
def receive(fw,fr,s,ss):
	while 1: 
		try:
			rec = ss.recv(1024)
			fw.write(rec)
			fw.flush()
		except:
			print "Socket Error!"
			continue				#Maybe the collecting system close right,but the data not be executed.
			fw.close()
			fr.close()
			ss.close()  
			s.close() 
			break
	thread.exit_thread()

#Read File & Execute Orders
def execute(fw,fr,s,ss):
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
				#RLEG
				RHipRoll = string.atof(data[8])
				RHipPitch = string.atof(data[9])
				RKneePitch = string.atof(data[10])
				RAnklePitch = string.atof(data[11])
				RAnkleRoll = string.atof(data[12])
				#LLEG
				LHipRoll = string.atof(data[13])
				LHipPitch = string.atof(data[14])
				LKneePitch = string.atof(data[15])
				LAnklePitch = string.atof(data[16])
				LAnkleRoll = string.atof(data[17])
				#Period
				period = string.atof(data[18])
				#Support
				supportMode = string.atoi(data[19])
				supportOrder = string.atoi(data[20])
				doubleSupportConfig = string.atoi(data[21])
				setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll,LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll,period,supportMode,supportOrder,doubleSupportConfig)
		except Exception,e:
			continue				#Somtimes some errors occur while reading file in Python,just ignore them 
			print "File Error!"
			print e
			fw.close()
			fr.close()
			ss.close()  
			s.close() 
			break
	thread.exit_thread()

fw = open("orders.txt", "w")
fr = open("orders.txt", "r")
address = ('192.168.1.105',8080)  
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(address)  
s.listen(5)  
print "Server Socket Established!"
ss, addr = s.accept()
print "Socket Connected!"
thread.start_new_thread(receive, (fw,fr,s,ss))
execute(fw,fr,s,ss)
ss.close()
s.close()
fw.close()
fr.close()
