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
motionProxy.wakeUp()
enable = True
chainName = "Arms"
isSuccess = motionProxy.setCollisionProtectionEnabled(chainName, enable)
time.sleep(1)


def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period):
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]
	fractionMaxSpeed  = 0.3
	motionProxy.setAngles(names, angles, fractionMaxSpeed)
	time.sleep(0.5*period)
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

address = ('192.168.1.108',8080)  
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(address)  
s.listen(5)  
ss, addr = s.accept()
f = open("orders.txt", "w")
fp = open("orders.txt", "r")
thread.start_new_thread(receive, (f,fp,s,ss))
execute(f,fp,s,ss)
ss.close()
s.close()
f.close()
fp.close()
