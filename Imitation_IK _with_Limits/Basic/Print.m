%�ݹ��ӡ�����ؽڵ�λ��
function Print(j)
global uLINK
if j == 0 
    return;
end
fprintf('============j = %d: %s============\n',j,uLINK(j).name);
fprintf('λ��\n');
uLINK(j).p
fprintf('��̬\n');
uLINK(j).R
fprintf('------------�ؽ�%d��ӡ����---------\n\n',j);
Print(uLINK(j).child);
Print(uLINK(j).sister);