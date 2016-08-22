%{
    FK：已知关节角度求各个杆件位姿
    递归函数按照从BODY到各个child关节的顺序依次计算每个子关节对应连杆的位姿
%}
function ForwardKinematics(j)
global uLINK
if j == 0 
    return;
end
if j ~= 1
    i = uLINK(j).mother;
    uLINK(j).p = uLINK(i).R * uLINK(j).b + uLINK(i).p;
    uLINK(j).R = uLINK(i).R * Rodrigues(uLINK(j).a,uLINK(j).q);
end
ForwardKinematics(uLINK(j).sister);
ForwardKinematics(uLINK(j).child);