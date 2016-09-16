%计算当前关节位姿目标关节位姿的偏差
function err = CalcVWerr(Cref,Cnow)
err = Cref.p - Cnow.p;