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

robot_ip = '192.168.1.101'
robot_port = 9559
motionProxy=ALProxy("ALMotion",robot_ip,robot_port)
motionProxy.wakeUp()
enable = True
chainName = "Arms"
isSuccess = motionProxy.setCollisionProtectionEnabled(chainName, enable)
time.sleep(1)


def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period):
	if RShoulderPitch<=-119.5:
		RShoulderPitch = -119.5
	if RShoulderPitch>=119.5:
		RShoulderPitch = 119.5
	if RShoulderRoll<=-76:
		RShoulderRoll =-76
	if RShoulderRoll>=18:
		RShoulderRoll = 18
	if RElbowYaw<=-119.5:
		RElbowYaw = -119.5
	if RElbowYaw>=119.5:
		RElbowYaw = 119.5
	if RElbowRoll<=2:
		RElbowRoll = 2
	if RElbowRoll>=88.5:
		RElbowRoll = 88.5

	if LShoulderPitch<=-119.5:
		LShoulderPitch =-119.5
	if LShoulderPitch>=119.5:
		LShoulderPitch = 119.5
	if LShoulderRoll<=-18:
		LShoulderRoll = -18
	if LShoulderRoll>=76:
		LShoulderRoll = 76
	if LElbowYaw<=-119.5:
		LElbowYaw = -119.5
	if LElbowYaw>=119.5:
		LElbowYaw = 119.5
	if LElbowRoll>=-2:
		LElbowRoll = -2
	if LElbowRoll<=-88.5:
		LElbowRoll = -88.5
	
	if RHipYawPitch <= -65.62:
		RHipYawPitch = -65.62
	if RHipYawPitch >= 42.44:
		RHipYawPitch = 42.44
	if RHipRoll <= -45.29:
		RHipRoll = -45.29
	if RHipRoll >= 21.74:
		RHipRoll = 21.74
	if RHipPitch <= -88.00:
		RHipPitch = -88.00
	if RHipPitch >= 27.73:
		RHipPitch = 27.73
	if RKneePitch <= -5.90:
		RKneePitch = -5.90
	if RKneePitch >= 121.47:
		RKneePitch = 121.47

	if LHipYawPitch <= -65.62:
		LHipYawPitch = -65.62
	if LHipYawPitch >= 42.44:
		LHipYawPitch = 42.44
	if LHipRoll <= -21.74:
		LHipRoll = -21.74
	if LHipRoll >= 45.29:
		LHipRoll = 45.29
	if LHipPitch <= -88.00:
		LHipPitch = -88.00
	if LHipPitch >= 27.73:
		LHipPitch = 27.73
	if LKneePitch <= -5.29:
		LKneePitch = -5.29
	if LKneePitch >= 121.04:
		LKneePitch = 121.04

	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]
	fractionMaxSpeed  = 0.3
	motionProxy.setAngles(names, angles, fractionMaxSpeed)
	print "---------------- Human Motion Imitation -----------------"

#Receive Thread
def receive(f,fp,s,ss):
	while 1: 
		try:
			rec = ss.recv(1024)
			f.write(rec)
			f.flush()
		except:
			print "Socket Error!"
			f.close()
			fp.close()
			ss.close()  
			s.close() 
			break
	thread.exit_thread()

#Read File & Execute Orders
def execute(f,fp,s,ss):
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
			f.close()
			fp.close()
			ss.close()  
			s.close() 
			break
	thread.exit_thread()

address = ('192.168.1.103',8080)  
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(address)  
s.listen(5)  
ss, addr = s.accept()
f = open("orders.txt", "a+")
fp = open("orders.txt", "r")
thread.start_new_thread(receive, (f,fp,s,ss))
execute(f,fp,s,ss)
ss.close()
s.close()
f.close()
fp.close()
