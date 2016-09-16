import string 
f = open("orders.txt","a+")
f.write("hah\n")
f.write("nmb\n")
f.flush()
fp = open("orders.txt")
line = fp.readline()
while line:
	print line
	line = fp.readline()
f.close()
fp.close()
