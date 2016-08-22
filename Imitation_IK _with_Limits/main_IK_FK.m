addpath(genpath(pwd));
Joint;
global uLINK

%利用正向运动学求各关节位姿，只要提前知道BODY的初始位姿和各关节角度，其他关节位姿可求,双膝微弯，防止奇异矩阵
uLINK(BODY).p = [0,0,0]';   %333.8
uLINK(BODY).R = rpy2rot(0,0,0);

uLINK(HEAD_J0).q = 0.0*ToRad;
uLINK(HEAD_J1).q = -9.74028269513*ToRad;
uLINK(RARM_J0).q = 82.7212822826*ToRad;
uLINK(RARM_J1).q = -12.8386648474*ToRad;
uLINK(RARM_J2).q = 68.8745557842*ToRad;
uLINK(RARM_J3).q = 23.8800183348*ToRad;
uLINK(RARM_J4).q = 5.7295772273*ToRad;
uLINK(LARM_J0).q = 82.7212822826*ToRad;
uLINK(LARM_J1).q = 12.8386648474*ToRad;
uLINK(LARM_J2).q = -68.8745557842*ToRad;
uLINK(LARM_J3).q = -23.8800183348*ToRad;
uLINK(LARM_J4).q = 5.72957893484*ToRad;
uLINK(RLEG_J0).q = -9.74028269513*ToRad;
uLINK(RLEG_J1).q = -5.72957808107*ToRad;
uLINK(RLEG_J2).q = 7.44845112119*ToRad;
uLINK(RLEG_J3).q = -5.15662040103*ToRad;
uLINK(RLEG_J4).q = 5.15662040103*ToRad;
uLINK(RLEG_J5).q = 7.44845112119*ToRad;
uLINK(LLEG_J0).q = -9.74028269513*ToRad;
uLINK(LLEG_J1).q = 5.72957808107*ToRad;
uLINK(LLEG_J2).q = 7.44845112119*ToRad;
uLINK(LLEG_J3).q = -5.15662040103*ToRad;
uLINK(LLEG_J4).q = 5.15662040103*ToRad;
uLINK(LLEG_J5).q= -7.44845112119*ToRad;

ForwardKinematics(1);
%fprintf('----------=====关节打印=====--------\n\n');
%Print(1);

%----- 测试 ------
%左脚目标位姿
%Lfoot.p = [0.00025308411568403244, 0.10126101970672607, -0.2811056077480316]';
%result = InverseKinematics(LLEG_J4,Lfoot);

%----- 测试 ------
%双手目标位姿
%{
RSE = [0.2119092391,-0.9727676668,-0.0939017613]';
REH = [0.1303369217,0.0392816007,0.9906912954]';
LSE = [0.2397818027,0.9698632085,-0.0432440051]';
LEH = [0.3061409362,0.0195272649,0.9517859072]';

RSE = [-0.2053370991,-0.9783176506,0.0270416378]';
REH = [-0.0511503480,-0.0466031495,0.9976030214]';
LSE = [-0.0988890766,0.9928882710,-0.0662859848]';
LEH = [0.2957810910,0.0838696843,0.9515668249]';

RSE = [-0.1097656241,-0.8894646789,0.4436260731]';
REH = [0.0464285629,-0.7753341674,0.6298422956]';
LSE = [0.7346973679,0.5886393812,-0.3372290861]';
LEH = [0.6144020307,-0.7394287498,0.2752367503]';
%}
RSE = [-0.2157720332,-0.5547894219,-0.8035241920]';
REH = [0.5465965510,-0.7353266263,0.4006581624]';
LSE = [-0.1423027315,0.3721703250,-0.9171909189]';
LEH = [0.3784450582,0.7073547701,-0.5970163877]';
RHK = [0.8660986027,-0.0844778380,-0.4926831693]';
RKA = [0.3699345018,0.0612411075,-0.9270372113]';
LHK = [0.8660986027,0.0844778380,-0.4926831693]';
LKA = [0.3699345018,-0.0612411075,-0.9270372113]';

RHand.p = uLINK(RARM_J1).p + RSE*(sqrt(power(0.015,2)+power(0.105,2)));
result1 = InverseKinematics(RARM_J2,RHand);

LHand.p = uLINK(LARM_J1).p + LSE*(sqrt(power(0.015,2)+power(0.105,2)));
result2 = InverseKinematics(LARM_J2,LHand);

RHand.p = uLINK(RARM_J3).p + REH*0.05595;
result3 = InverseKinematics(RARM_J4,RHand);

LHand.p = uLINK(LARM_J3).p + LEH*0.05595;
result4 = InverseKinematics(LARM_J4,LHand);

RFoot.p = uLINK(RLEG_J2).p + RHK*0.100 + RKA*0.1029;
result5 = InverseKinematics(RLEG_J4,RFoot);

LFoot.p = uLINK(LLEG_J2).p + LHK*0.100 + LKA*0.1029;
result6 = InverseKinematics(LLEG_J4,LFoot);
%{
com = calcCoM();
rankle_com = (uLINK(RLEG_J5).R)'*(com - uLINK(RLEG_J5).p);
%}
result = result1 + result2 + result3 + result4 + result5 + result6;

fprintf('==============该链的关节配置=============\n');
if result < 0.01            %误差大小为1cm
    fprintf('关节配置误差小于1cm,err = %0.6f\n',result);
else
    fprintf('关节配置误差大于1cm,err = %0.6f\n',result);
end

fprintf('RShoulderPitch = %0.3f\n',uLINK(RARM_J0).q/ToRad);
fprintf('RShoulderRoll = %0.3f\n',uLINK(RARM_J1).q/ToRad);
fprintf('RElbowYaw = %0.3f\n',uLINK(RARM_J2).q/ToRad);
fprintf('RElbowRoll = %0.3f\n',uLINK(RARM_J3).q/ToRad);
fprintf('LShoulderPitch = %0.3f\n',uLINK(LARM_J0).q/ToRad);
fprintf('LShoulderRoll = %0.3f\n',uLINK(LARM_J1).q/ToRad);
fprintf('LElbowYaw = %0.3f\n',uLINK(LARM_J2).q/ToRad);
fprintf('LElbowRoll = %0.3f\n',uLINK(LARM_J3).q/ToRad);
fprintf('RHipRoll = %0.3f\n',uLINK(RLEG_J1).q/ToRad);
fprintf('RHipPitch = %0.3f\n',uLINK(RLEG_J2).q/ToRad);
fprintf('RKneePitch = %0.3f\n',uLINK(RLEG_J3).q/ToRad);
fprintf('LHipRoll = %0.3f\n',uLINK(LLEG_J1).q/ToRad);
fprintf('LHipPitch = %0.3f\n',uLINK(LLEG_J2).q/ToRad);
fprintf('LKneePitch = %0.3f\n',uLINK(LLEG_J3).q/ToRad);
