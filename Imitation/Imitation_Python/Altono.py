from naoqi import ALProxy
robot_ip = "192.168.1.100"
robot_port = 9559
State = ALProxy("ALAutonomousLife",robot_ip,robot_port)
State.setState("disabled") #Turn off AutoIntelligent
currentState = State.getState()
