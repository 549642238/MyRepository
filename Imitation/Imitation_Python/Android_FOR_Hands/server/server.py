from naoqi import ALProxy
import string  
import argparse
import almath
import socket
import motion
import time
import sys

robot_ip = sys.argv[1]
robot_port = 9559
motionProxy =ALProxy("ALMotion",'192.168.1.100',robot_port)
motionProxy.wakeUp()
#Control Hands' Angles
def setUpAngles(Hand,State):
	if Hand == 1:
		names  = ["LHand"]
	else:
		names = ["RHand"]
    	angles = [State]
    	fractionMaxSpeed  = 0.3
    	motionProxy.setAngles(names, angles, fractionMaxSpeed)
    	time.sleep(0.1)
#Socket Communications
address = (robot_ip,8888)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(address)  
s.listen(5)  
ss, addr = s.accept()
while 1:
	try:
        	rec = ss.recv(10)
		print "recv:"+rec
        	data = rec.split(',')
		Hand = string.atoi(data[0])
        	State = string.atof(data[1])
		setUpAngles(Hand,State)
	except:
        	print "client exit"
        	ss.close()
        	s.close()
        	break
ss.close()
s.close()
