%{
    �ݹ��ҵ���BODY��Ŀ��ؽڵ����йؽ���ţ�Ȼ���ŵ�һ��������
%}
function idx = FindRoute(to)
if to == 16
    idx = [14,15,16];
elseif to == 18
    idx = [16,17,18];
elseif to == 21
    idx = [19,20,21];
elseif to == 23
    idx = [21,22,23];
elseif to == 6
    idx = [3,4,5,6];
elseif to == 12
    idx = [9,10,11,12];
else
    fprintf('Error Configuration\n');
end