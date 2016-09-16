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


def setUpAngles(number,RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period):	
	anglelist = []
	angles = []
	for i in range(0,number):
		angles.append(RShoulderPitch[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(RShoulderRoll[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(RElbowYaw[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(RElbowRoll[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(LShoulderPitch[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(LShoulderRoll[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(LElbowYaw[i]*almath.TO_RAD)
	anglelist.append(angles)
	angles = []
	for i in range(0,number):
		angles.append(LElbowRoll[i]*almath.TO_RAD)
	anglelist.append(angles)
	names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
	timelist = [period,period,period,period,period,period,period,period]
	isAbsolute = True    
	motionProxy.angleInterpolation(names, anglelist, timelist, isAbsolute)
	print "---------------- Human Motion Imitation -----------------\n"

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
		RShoulderPitch = []
		RShoulderRoll = []
		RElbowRoll = []
		RElbowYaw = []
		LShoulderPitch = []
		LShoulderRoll = []
		LElbowRoll = []
		LElbowYaw = []
		RHipYawPitch = []
		RHipRoll = []
		RHipPitch = []
		RKneePitch = []
		LHipYawPitch = []
		LHipRoll = []
		LHipPitch = []
		LKneePitch = []
		period = []
		lines = fp.readlines()
		number = len(lines)
		i = 0
		P = 0
		try:
			for line in lines:
				sig = 0
				if len(line)>10:
					sig = 1
				if sig == 0:
					break
				print "\n=============== New Joints's Settings ==============="
				print "["+line+"]"
				data = line.split(',')
				data0 = string.atof(data[0])
				data1 = string.atof(data[1])
				data2 = string.atof(data[2])
				data3 = string.atof(data[3])
				data4 = string.atof(data[4])
				data5 = string.atof(data[5])
				data6 = string.atof(data[6])
				data7 = string.atof(data[7])
				data8 = string.atof(data[8])
				data9 = string.atof(data[9])
				data10 = string.atof(data[10])
				data11 = string.atof(data[11])
				data12 = string.atof(data[12])
				data13 = string.atof(data[13])
				data14 = string.atof(data[14])
				data15 = string.atof(data[15])
				#Check Angles
				if data0<=-119.5:
					data0 = -119.5
				if data0>=119.5:
 					data0 = 119.5
				if data1<=-76:
					data1 =-76
				if data1>=18:
					data1 = 18
				if data2<=-119.5:
					data2 = -119.5
				if data2>=119.5:
					data2 = 119.5
				if data3<=2:
					data3 = 2
				if data3>=88.5:
					data3 = 88.5

				if data4<=-119.5:
					data4 =-119.5
				if data4>=119.5:
					data4 = 119.5
				if data5<=-18:
					data5 = -18
				if data5>=76:
					data5 = 76
				if data6<=-119.5:
					data6 = -119.5
				if data6>=119.5:
					data6 = 119.5
				if data7>=-2:
					data7 = -2
				if data7<=-88.5:
					data7 = -88.5
	
				if data8 <= -65.62:
					data8 = -65.62
				if data8 >= 42.44:
					data8 = 42.44
				if data9 <= -45.29:
					data9 = -45.29
				if data9 >= 21.74:
					data9 = 21.74
				if data10 <= -88.00:
					data10 = -88.00
				if data10 >= 27.73:
					data10 = 27.73
				if data11 <= -5.90:
					data11 = -5.90
				if data11 >= 121.47:
					data11 = 121.47

				if data12 <= -65.62:
					data12 = -65.62
				if data12 >= 42.44:
					data12 = 42.44
				if data13 <= -21.74:
					data13 = -21.74
				if data13 >= 45.29:
					data13 = 45.29
				if data14 <= -88.00:
					data14 = -88.00
				if data14 >= 27.73:
					data14 = 27.73
				if data15 <= -5.29:
					data15 = -5.29
				if data15 >= 121.04:
					data15 = 121.04
				#RARM:
				RShoulderPitch.append(data0)
				RShoulderRoll.append(data1)
				RElbowRoll.append(data2)
				RElbowYaw.append(data3)
				#LARM:
				LShoulderPitch.append(data4)
				LShoulderRoll.append(data5)
				LElbowRoll.append(data6)
				LElbowYaw.append(data7)
				#RLEG
				RHipYawPitch.append(data8)
				RHipRoll.append(data9)
				RHipPitch.append(data10)
				RKneePitch.append(data11)
				#LLEG
				LHipYawPitch.append(data12)
				LHipRoll.append(data13)
				LHipPitch.append(data14)
				LKneePitch.append(data15)
				#Period
				P = P + string.atof(data[16])
				period.append(P)
				i = i + 1
			if number > 1:
				setUpAngles(number-1,RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipYawPitch,RHipRoll,RHipPitch,RKneePitch,LHipYawPitch,LHipRoll,LHipPitch,LKneePitch,period)
		except Exception,e:
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
f = open("orders.txt", "a+")
fp = open("orders.txt", "r")
thread.start_new_thread(receive, (f,fp,s,ss))
execute(f,fp,s,ss)
ss.close()
s.close()
f.close()
fp.close()
