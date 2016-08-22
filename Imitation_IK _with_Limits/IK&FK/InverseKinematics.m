%{
    IK：已知各个杆件位姿求关节角度,DLS(阻尼最小二乘算法)
%}
function res = InverseKinematics(to,Target)
global uLINK
ForwardKinematics(1);
idx = FindRoute(to);
number = length(idx)-1;
updateJ=1;                                          %是否需要重新计算雅克比矩阵
n_iters = 10;                                       %迭代最大次数?
lamda = 0.03;                                       %LM算法的阻尼系数初值
v1 = 10;                                            %放大因子
v2 = 0.1;                                           %缩小因子
err0 = 0.001;                                       %误差精度0.001m
H1 = zeros(number,1);
H2 = zeros(number,1);
for nn = 1:number
    H1(nn) = power(uLINK(idx(nn)).maxq - uLINK(idx(nn)).minq,2)*(2*uLINK(idx(nn)).q-uLINK(idx(nn)).maxq-uLINK(idx(nn)).minq)/(4*power(uLINK(idx(nn)).maxq-uLINK(idx(nn)).q,2)*power(uLINK(idx(nn)).q-uLINK(idx(nn)).minq,2));
end
W = eye(number);
for it = 1:n_iters
    if updateJ==1                                   %根据当前估计值，计算雅克比矩阵
        for nn = 1:number
            H2(nn) = power(uLINK(idx(nn)).maxq - uLINK(idx(nn)).minq,2)*(2*uLINK(idx(nn)).q-uLINK(idx(nn)).maxq-uLINK(idx(nn)).minq)/(4*power(uLINK(idx(nn)).maxq-uLINK(idx(nn)).q,2)*power(uLINK(idx(nn)).q-uLINK(idx(nn)).minq,2));
            if abs(H2(nn)) - abs(H1(nn)) > 0
                W(nn,nn) = 1+abs(H2(nn));
            else
                W(nn,nn) = 1;
            end
        end
        J = CalcJacobian(idx);
        err = CalcVWerr(Target,uLINK(to));          %当前关节位姿目标关节位姿的偏差
        H=J*pinv(W)*J';                             %计算(拟)海塞矩阵
        if it==1                                    %若是第一次迭代，计算误差
            e = norm(err);
        end
    end
    fprintf('当前误差大小：%0.10f\n',e);             %偏差大小
    if e < err0
        break;
    end
    Ji = pinv(W)*J'*pinv(H+lamda*eye(3,3));
    dq = Ji*err;                                    %计算步长dq
    for nn = 1:number
        j = idx(nn);
        uLINK(j).q = uLINK(j).q + dq(nn);
    end
    ForwardKinematics(1);
    err_lm = CalcVWerr(Target,uLINK(to));
    if norm(err_lm) < e
        lamda = lamda*v2;
        e = norm(err_lm);
        H1 = H2;
        updateJ = 1;
    else
        for nn = 1:number
            j = idx(nn);
            uLINK(j).q = uLINK(j).q - dq(nn);
        end
        ForwardKinematics(1);
        updateJ = 0;
        lamda = lamda*v1;
    end
end
for nn = 1:number
    j = idx(nn);
    if uLINK(j).q > uLINK(j).maxq
        uLINK(j).q = uLINK(j).maxq;
    end
    if uLINK(j).q < uLINK(j).minq
        uLINK(j).q = uLINK(j).minq;
    end
end
ForwardKinematics(1);
err = CalcVWerr(Target,uLINK(to));
res = norm(err);