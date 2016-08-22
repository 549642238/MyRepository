% ���㺯��f���ſ˱Ⱦ����ǽ���ʽ 
syms a b y x real; 
f=a*exp(-b*x);  
Jsym=jacobian(f,[a b]);                         % ��������ݡ�
data_1=[0.25 0.5 1 1.5 2 3 4 6 8];  
obs_1=[19.21 18.15 15.36 14.10 12.89 9.32 7.45 5.24 3.01];   
% 2. LM�㷨 
% ��ʼ�²�s
a0=10; 
b0=0.5;  
y_init = a0*exp(-b0*data_1);
Ndata=length(obs_1);                            %?���ݸ���?
Nparams=2;                                      %?����ά��
n_iters=50;                                     %?����������?
lamda=0.01;                                     %?LM�㷨������ϵ����ֵ?
%?step1:?������ֵ?
updateJ=1;
a_est=a0;
b_est=b0;
%?step2:?����?
for it=1:n_iters
    if updateJ==1                               %?���ݵ�ǰ����ֵ�������ſ˱Ⱦ���
        J=zeros(Ndata,Nparams);
        for i=1:length(data_1)
            J(i,:)=[exp(-b_est*data_1(i)),-a_est*data_1(i)*exp(-b_est*data_1(i))];
        end
        y_est = a_est*exp(-b_est*data_1);       %?���ݵ�ǰ�������õ�����ֵ
        d=obs_1-y_est;                          %?�������
        H=J'*J;                                 %?���㣨�⣩��������?
        if it==1                                %?���ǵ�һ�ε������������
            e=dot(d,d);
        end
    end
    H_lm=H+(lamda*eye(Nparams,Nparams));        %?��������ϵ��lamda��ϵõ�H����
    dp=H_lm\(J'*d(:));                          %?���㲽��dp�������ݲ��������µĿ��ܵ�\��������ֵ
    g = J'*d(:);
    a_lm=a_est+dp(1);
    b_lm=b_est+dp(2);
    y_est_lm = a_lm*exp(-b_lm*data_1);          %?�����µĿ��ܹ���ֵ��Ӧ��y
    d_lm=obs_1-y_est_lm;
    e_lm = dot(d_lm,d_lm);                      %  �����µĿ��ܹ���ֵ��Ӧ�Ĳв�e
    %  ������������θ��²���������ϵ��
    if e_lm<e
        lamda = lamda/10;
        e_est = a_lm;
        b_est = b_lm;
        e = e_lm;
        disp(e);
        updateJ = 1;
    else
        updateJ = 0;
        lamda = lamda*10;
    end
end
a_est
b_est