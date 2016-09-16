function m = calcM(j)
global uLINK
if j == 0
    m = 0;
else
    m = uLINK(j).m + calcM(uLINK(j).sister) + calcM(uLINK(j).child);
end