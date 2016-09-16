import string  
from naoqi import ALProxy
import argparse
import almath
import motion
import time

robot_ip = '192.168.1.100'
robot_port = 9559
motionProxy =ALProxy("ALMotion",robot_ip,robot_port)
motionProxy.wakeUp()
enable = True
chainName = "Arms"
isSuccess = motionProxy.setCollisionProtectionEnabled(chainName, enable)
time.sleep(1)


def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll):
    while(RShoulderPitch>=360):
	RShoulderPitch = RShoulderPitch - 360
    while(RShoulderPitch<=-360):
	RShoulderPitch = RShoulderPitch + 360
    while(RShoulderRoll>=360):
	RShoulderRoll = RShoulderRoll - 360
    while(RShoulderRoll<=-360):
	RShoulderRoll = RShoulderRoll + 360
    while(RElbowYaw>=360):
	RElbowYaw = RElbowYaw - 360
    while(RElbowYaw<=-360):
	RElbowYaw = RElbowYaw + 360
    while(RElbowRoll>=360):
	RElbowRoll = RElbowRoll - 360
    while(RElbowRoll<=-360):
	RElbowRoll = RElbowRoll + 360
    while(LShoulderPitch>=360):
	LShoulderPitch = LShoulderPitch - 360
    while(LShoulderPitch<=-360):
	LShoulderPitch = LShoulderPitch + 360
    while(LShoulderRoll>=360):
	LShoulderRoll = LShoulderRoll - 360
    while(LShoulderRoll<=-360):
	LShoulderRoll = LShoulderRoll + 360
    while(LElbowYaw>=360):
	LElbowYaw = LElbowYaw - 360
    while(LElbowYaw<=-360):
	LElbowYaw = LElbowYaw + 360
    while(LElbowRoll>=360):
	LElbowRoll = LElbowRoll - 360
    while(RElbowRoll<=-360):
	LElbowRoll = LElbowRoll + 360
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
	
    names  = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
    angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]
    fractionMaxSpeed  = 0.1
    motionProxy.setAngles(names, angles, fractionMaxSpeed)
    time.sleep(2)
    '''
    timeLists  = [period, period, period, period, period, period, period, period,period, period, period, period, period, period, period, period]
    isAbsolute = True
    motionProxy.angleInterpolation(names, angles, timeLists, isAbsolute)
    '''
RShoulderPitch = 187.188
RShoulderRoll = -70.195
RElbowYaw = -23121.273
RElbowRoll = 83.289
LShoulderPitch = 142.556
LShoulderRoll = 75.121
LElbowYaw = -215.585
LElbowRoll = -95.439
setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll)

frame = motion.FRAME_TORSO
useSensorValues = True
RShoulder = motionProxy.getPosition("RShoulderPitch",frame, useSensorValues)
RElbow = motionProxy.getPosition("RElbowRoll",frame, useSensorValues)
RHand = motionProxy.getPosition("RWristYaw",frame, useSensorValues)

LShoulder = motionProxy.getPosition("LShoulderPitch",frame, useSensorValues)
LElbow = motionProxy.getPosition("LElbowRoll",frame, useSensorValues)
LHand = motionProxy.getPosition("LWristYaw",frame, useSensorValues)

print "RSE = ["+str(RElbow[0]-RShoulder[0])+","+str(RElbow[1]-RShoulder[1])+","+str(RElbow[2]-RShoulder[2])+"]';"
print "REH = ["+str(RHand[0]-RElbow[0])+","+str(RHand[1]-RElbow[1])+","+str(RHand[2]-RElbow[2])+"]';"
print "LSE = ["+str(LElbow[0]-LShoulder[0])+","+str(LElbow[1]-LShoulder[1])+","+str(LElbow[2]-LShoulder[2])+"]';"
print "LEH = ["+str(LHand[0]-LElbow[0])+","+str(LHand[1]-LElbow[1])+","+str(LHand[2]-LElbow[2])+"]';"
