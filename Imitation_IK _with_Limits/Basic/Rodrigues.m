%{
    求解关节相对于母连杆的姿态，存在公式可直接求得
%}
function R = Rodrigues(a,q)
E = eye(3);
A = zeros(3);
A(1,2) = -a(3);
A(2,1) = a(3);
A(1,3) = a(2);
A(3,1) = -a(2);
A(2,3) = -a(1);
A(3,2) = a(1);
R = E + sin(q)*A + A^2*(1-cos(q));