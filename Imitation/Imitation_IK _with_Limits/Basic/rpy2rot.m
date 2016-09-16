function R = rpy2rot(x,y,z)
%{
Rx = [1,0,0;0,cos(x),-sin(x);0,sin(x),cos(x)];
Ry = [cos(y),0,sin(y);0,1,0;-sin(y),0,cos(y)];
Rz = [cos(z),-sin(z),0;sin(z),cos(z),0;0,0,1];
R = Rz * Ry * Rx;
%}
R = [cos(z)*cos(y),-sin(z)*cos(x)+cos(z)*sin(y)*sin(x),sin(z)*sin(x)+cos(z)*sin(y)*cos(x); sin(z)*cos(y),cos(z)*cos(x)+sin(z)*sin(y)*sin(x),-cos(z)*sin(x)+sin(z)*sin(y)*cos(x); -sin(y),cos(y)*sin(x),cos(y)*cos(x)];