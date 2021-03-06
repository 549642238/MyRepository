from naoqi import ALProxy
import argparse
import almath
import motion
import time

def lowerReference():
	RHipRoll = -5.72957808107;
	RHipPitch = 7.44845112119;
	RKneePitch = -5.15662040103;
	RAnklePitch = 5.15662040103;
	RAnkleRoll = 7.44845112119;
	LHipRoll = 5.72957808107;
	LHipPitch = 7.44845112119;
	LKneePitch = -5.15662040103;
	LAnklePitch = 5.15662040103;
	LAnkleRoll = -7.44845112119;
	names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
	angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    	fractionMaxSpeed  = 0.1
    	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
'''
robot_ip = '192.168.1.100'
robot_port = 9559
motionProxy=ALProxy("ALMotion",robot_ip,robot_port)
postureProxy = ALProxy("ALRobotPosture", robot_ip,robot_port)
motionProxy.wakeUp()
time.sleep(1)
lowerReference()
motionProxy.wbEnable(True)
supportLeg = "LLeg"
duration   = 1.0
motionProxy.wbGoToBalance(supportLeg, duration)
motionProxy.wbEnable(False)
names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
angles = [0.1,0.13,-0.09,0.09,-0.13,0.13,-0.1,-0.09,0.09,0.13]
fractionMaxSpeed  = 0.1
motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
names  = ["RHipPitch","RKneePitch","RHipRoll","RAnklePitch","RAnkleRoll","LAnkleRoll","LHipRoll","LKneePitch","LAnklePitch"]
angles = [0*almath.TO_RAD,-5*almath.TO_RAD,5*almath.TO_RAD,0.05,0.1,-0.13,0.1,10*almath.TO_RAD,-0.09]
fractionMaxSpeed  = 0.1
motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
lowerReference()
'''


robot_ip = '192.168.1.100'
robot_port = 9559
motionProxy=ALProxy("ALMotion",robot_ip,robot_port)
postureProxy = ALProxy("ALRobotPosture", robot_ip,robot_port)
motionProxy.wakeUp()
time.sleep(1)
lowerReference()
motionProxy.wbEnable(True)
supportLeg = "RLeg"
duration   = 1.0
motionProxy.wbGoToBalance(supportLeg, duration)
motionProxy.wbEnable(False)
'''
names  = ["LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll","RHipPitch","RHipRoll","RKneePitch","RAnklePitch","RAnkleRoll"]
angles = [0.2,0.13,-0.09,0.09,-0.23,0.1,0.13,-0.09,0.09,-0.2]
fractionMaxSpeed  = 0.1
motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
'''

names  = ["LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll","RHipPitch","RHipRoll","RKneePitch","RAnklePitch","RAnkleRoll"]
angles = [0.3,0.23,0.23,-0.23,-0.3,0.1,0.13,-0.09,0,-0.23]
fractionMaxSpeed  = 0.1
motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

'''
names  = ["LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll","RHipPitch","RHipRoll","RKneePitch","RAnklePitch","RAnkleRoll"]
angles = [0.2,0.13,-0.09,0.09,-0.23,0.1,0.13,-0.09,0.09,-0.2]
fractionMaxSpeed  = 0.1
motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)
lowerReference()
'''
