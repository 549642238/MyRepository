from naoqi import ALProxy
import time



robot_ip = '192.168.1.100'
robot_port = 9559
motion=ALProxy("ALMotion",robot_ip,robot_port)
motion.killAll()
motion.wakeUp()
time.sleep(1)
motion.rest()
time.sleep(1)
