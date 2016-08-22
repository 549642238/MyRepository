%{
    IK����֪�����˼�λ����ؽڽǶ�,DLS(������С�����㷨)
%}
function res = InverseKinematics(to,Target)
global uLINK
ForwardKinematics(1);
idx = FindRoute(to);
number = length(idx)-1;
updateJ=1;                                          %�Ƿ���Ҫ���¼����ſ˱Ⱦ���
n_iters = 10;                                       %����������?
lamda = 0.03;                                       %LM�㷨������ϵ����ֵ
v1 = 10;                                            %�Ŵ�����
v2 = 0.1;                                           %��С����
err0 = 0.001;                                       %����0.001m
H1 = zeros(number,1);
H2 = zeros(number,1);
for nn = 1:number
    H1(nn) = power(uLINK(idx(nn)).maxq - uLINK(idx(nn)).minq,2)*(2*uLINK(idx(nn)).q-uLINK(idx(nn)).maxq-uLINK(idx(nn)).minq)/(4*power(uLINK(idx(nn)).maxq-uLINK(idx(nn)).q,2)*power(uLINK(idx(nn)).q-uLINK(idx(nn)).minq,2));
end
W = eye(number);
for it = 1:n_iters
    if updateJ==1                                   %���ݵ�ǰ����ֵ�������ſ˱Ⱦ���
        for nn = 1:number
            H2(nn) = power(uLINK(idx(nn)).maxq - uLINK(idx(nn)).minq,2)*(2*uLINK(idx(nn)).q-uLINK(idx(nn)).maxq-uLINK(idx(nn)).minq)/(4*power(uLINK(idx(nn)).maxq-uLINK(idx(nn)).q,2)*power(uLINK(idx(nn)).q-uLINK(idx(nn)).minq,2));
            if abs(H2(nn)) - abs(H1(nn)) > 0
                W(nn,nn) = 1+abs(H2(nn));
            else
                W(nn,nn) = 1;
            end
        end
        J = CalcJacobian(idx);
        err = CalcVWerr(Target,uLINK(to));          %��ǰ�ؽ�λ��Ŀ��ؽ�λ�˵�ƫ��
        H=J*pinv(W)*J';                             %����(��)��������
        if it==1                                    %���ǵ�һ�ε������������
            e = norm(err);
        end
    end
    fprintf('��ǰ����С��%0.10f\n',e);             %ƫ���С
    if e < err0
        break;
    end
    Ji = pinv(W)*J'*pinv(H+lamda*eye(3,3));
    dq = Ji*err;                                    %���㲽��dq
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