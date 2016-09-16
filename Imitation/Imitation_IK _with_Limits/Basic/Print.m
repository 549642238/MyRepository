%递归打印各个关节的位姿
function Print(j)
global uLINK
if j == 0 
    return;
end
fprintf('============j = %d: %s============\n',j,uLINK(j).name);
fprintf('位置\n');
uLINK(j).p
fprintf('姿态\n');
uLINK(j).R
fprintf('------------关节%d打印结束---------\n\n',j);
Print(uLINK(j).child);
Print(uLINK(j).sister);