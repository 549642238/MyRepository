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
stateName  = "Plane"
supportLeg = "Legs"
motionProxy.wbFootState(stateName, supportLeg)
time.sleep(1)

def lowerReference():
	RHipRoll = -5.72957808107;
	RHipPitch = 2.44845112119;
	RKneePitch = -5.15662040103;
	RAnklePitch = 5.15662040103;
	RAnkleRoll = 7.44845112119;
	LHipRoll = 5.72957808107;
	LHipPitch = 2.44845112119;
	LKneePitch = -5.15662040103;
	LAnklePitch = 5.15662040103;
	LAnkleRoll = -7.44845112119;
	names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
	angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    	fractionMaxSpeed  = 0.1
    	motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)

def setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll,LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll):
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
    '''
    names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll","RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
    angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD,RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    fractionMaxSpeed  = 0.1    
    motionProxy.setAngles(names, angles, fractionMaxSpeed)
    time.sleep(2)
    '''
    names = ["RShoulderPitch","RShoulderRoll","RElbowYaw","RElbowRoll","LShoulderPitch","LShoulderRoll","LElbowYaw","LElbowRoll"]
    angles =[RShoulderPitch*almath.TO_RAD,RShoulderRoll*almath.TO_RAD,RElbowYaw*almath.TO_RAD,RElbowRoll*almath.TO_RAD,LShoulderPitch*almath.TO_RAD,LShoulderRoll*almath.TO_RAD,LElbowYaw*almath.TO_RAD,LElbowRoll*almath.TO_RAD]                        
    fractionMaxSpeed  = 0.3   
    motionProxy.setAngles(names, angles, fractionMaxSpeed)
    time.sleep(2)
    names =["RHipRoll","RHipPitch","RKneePitch","RAnklePitch","RAnkleRoll","LHipRoll","LHipPitch","LKneePitch","LAnklePitch","LAnkleRoll"]
    angles =[RHipRoll*almath.TO_RAD,RHipPitch*almath.TO_RAD,RKneePitch*almath.TO_RAD,RAnklePitch*almath.TO_RAD,RAnkleRoll*almath.TO_RAD,LHipRoll*almath.TO_RAD,LHipPitch*almath.TO_RAD,LKneePitch*almath.TO_RAD,LAnklePitch*almath.TO_RAD,LAnkleRoll*almath.TO_RAD]                        
    fractionMaxSpeed  = 0.1
    motionProxy.angleInterpolationWithSpeed(names, angles, fractionMaxSpeed)


RShoulderPitch = -31.341
RShoulderRoll = -61.270
RElbowYaw = 119.500
RElbowRoll = 2.000
LShoulderPitch = 113.617
LShoulderRoll = 4.420
LElbowYaw = -103.917
LElbowRoll = -35.201
RHipRoll = -5.729578
RHipPitch = 7.544204
RKneePitch = -5.156620
RAnklePitch = 2.413832
RAnkleRoll = 8.275593
LHipRoll = 5.729578
LHipPitch = 7.543314
LKneePitch = -5.156620
LAnklePitch = 2.413832
LAnkleRoll = -8.275593
setUpAngles(RShoulderPitch,RShoulderRoll,RElbowYaw,RElbowRoll,LShoulderPitch,LShoulderRoll,LElbowYaw,LElbowRoll,RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll,LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll)


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

name = "Body"
frame = motion.FRAME_WORLD
useSensors = True
com = motionProxy.getCOM(name, frame, useSensors)
LAnkle = motionProxy.getPosition("LAnkleRoll",frame, useSensorValues)
RAnkle = motionProxy.getPosition("RAnkleRoll",frame, useSensorValues)
LHip = motionProxy.getPosition("LHipPitch",frame, useSensorValues)
print "ankle-com = "+str(com[0]-LAnkle[0])+","+str(com[1]-LAnkle[1])+","+str(com[2]-LAnkle[2])
print "ankle-hip = "+str(LHip[0]- LAnkle[0])+","+str(LHip[1]- LAnkle[1])+","+str(LHip[2]- LAnkle[2])
print "LAnkle Roll = "+str(LAnkle[3]/almath.TO_RAD)+"RAnkle Roll = "+str(RAnkle[3]/almath.TO_RAD)
print com
