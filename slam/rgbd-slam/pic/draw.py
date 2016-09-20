# -*- coding: UTF-8 -*-
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
import string

fr = open("result_after.g2o", "r")
line = (fr.readline()).strip("\00")
line = (fr.readline()).strip("\00")
i = 1
x = [0];
y = [0];
z = [0];
while 1:
	try:
		line = (fr.readline()).strip("\00")
		data = line.split(' ')
		if(data[0] == "EDGE_SE3:QUAT"):
			break
		x.append(string.atof(data[2]))
		y.append(string.atof(data[3]))
		z.append(string.atof(data[4]))
		i = i+1
	except Exception,e:
		continue
draw = plt.subplot(111,projection='3d')	#创建一个三维的绘图工程
draw.scatter(x[:i],y[:i],z[:i],c='b')
draw.set_zlabel('Z') #坐标轴
draw.set_ylabel('Y')
draw.set_xlabel('X')
plt.show()
