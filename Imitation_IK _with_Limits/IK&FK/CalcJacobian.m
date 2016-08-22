function J = CalcJacobian(idx)
global uLINK
jsize = length(idx)-1;
target = uLINK(idx(end)).p;
J = zeros(3,jsize);
for n = 1:jsize
       j = idx(n);
       a = uLINK(j).R * uLINK(j).a;
       J(:,n) = cross(a,target-uLINK(j).p);
end