
insert into user(userid,username,userpasswd,usergrade,userbirth,usersex,useremail,userphone,userregistertime) values
(13121143,'李献涛','admin','大三','19930826','男','shineneo1@qq.com','18629608829',now()),
(13121175,'程志浩','admin','大三','','男','10000@qq.com','','');
$


insert into window(windowname,buildname,layer) values
("铁板炒饭系列",'竹园',1),#1
("兰州正宗牛肉拉面",'竹园',1),#2
("优质牛肉泡馍",'竹园',1),#3
("营养套餐南方锅仔",'竹园',1),#4
("南方风味快餐",'竹园',2),#5
("特色套餐",'竹园',2),#6
("锅巴饭",'竹园',2),#7
("木桶饭",'竹园',2),#8
("湖南百度蒸菜",'竹园',2),#9
("湖南米粉",'竹园',2),#10
("笼仔饭",'竹园',2),#11
("美味铁锅饭",'竹园',2),#12
("北京烤鸭套餐",'竹园',2),#13
("陕西凉皮",'竹园',2),#14
("兰州正宗牛肉面",'丁香',1),#15
("优质牛肉馍",'丁香',1),#16
("南方风快餐",'丁香',2),#17
("兰州宗牛肉拉面",'海棠',2),#18
("优牛肉泡馍",'海棠',1),#19
("方风味快餐",'海棠',2)#20
$


insert into recipe(windowid,dishname) values 
(1,"铁板牛肉炒饭"),
(1,"铁板鸡肉炒饭"),
(1,"铁板鸡蛋炒饭"),
(1,"铁板牛肉炒饭"),
(1,"铁板鸭肉炒饭"),
(1,"铁板大肉炒饭"),
(1,"铁板火腿炒饭"),
(1,"铁板猪肉炒饭"),
(1,"铁板炒米饭"),
(2,"葱油肉拌面"),
(2,"西红柿鸡蛋面"),
(2,"牛肉拉面"),
(2,"香菇肉拌面"),
(2,"老坛酸菜肉丝面"),
(2,"土豆肉拌面"),
(2,"西宁肉拌面"),
(2,"纯肉干拌面"),
(2,"香辣鸡块拌面"),
(2,"新疆肉拌面"),
(3,"优质牛肉泡馍"),
(4,"两菜+狮子头"),
(4,"两菜+里脊肉"),
(4,"两菜+鸡腿"),
(4,"两菜+猪排"),
(4,"两菜+鸡块"),
(4,"两菜+火腿"),
(4,"两菜+鸡排"),
(4,"卤鸭腿"),
(4,"两菜+香辣鱼块"),
(4,"两菜+青椒肉丝"),
(4,"两菜+红烧肉"),
(4,"两菜+排骨"),
(5,"三菜加米饭"),
(5,"四菜加米饭"),
(6,"一荤两素"),
(6,"两素"),
(6,"三荤"),
(6,"两荤一素"),
(7,"鱼香肉丝"),
(7,"脆皮牛排"),
(7,"素菜"),
(7,"大块鸡块"),
(7,"回锅肉"),
(8,"招牌鸡排饭"),
(8,"广式培根饭"),
(8,"西式牛排饭"),
(8,"招牌猪排饭"),
(8,"招牌烧肉饭"),
(8,"台式烤肉饭"),
(8,"迷迭香鸡腿饭"),
(8,"港式鸡柳饭"),
(8,"烤鸭腿饭"),
(8,"咖喱鸭肉饭"),
(9,"梅菜扣肉"),
(9,"小酥肉"),
(9,"豉椒蒸鱼"),
(9,"蒜苔炒肉"),
(9,"外婆菜"),
(9,"泡菜肉丝"),
(9,"宫爆鸡丁"),
(9,"千页豆腐"),
(9,"西芹香干"),
(9,"干煸菜花"),
(9,"手撕包菜"),
(9,"老干妈土豆丝"),
(9,"蒸鸡蛋"),
(9,"西红柿鸡蛋"),
(9,"豆角茄子"),
(9,"海米冬瓜"),
(9,"松仁玉米"),
(9,"炝拌莲菜"),
(9,"香辣日本豆腐"),
(10,"麻辣牛肉粉面"),
(10,"麻辣牛肉粉面"),
(10,"鸡肉粉面"),
(10,"排骨粉面"),
(10,"炒米粉"),
(10,"炒细面"),
(11,"滑蛋肉片"),
(11,"咖喱鸡块"),
(11,"香辣牛肉"),
(11,"红烧肉"),
(12,"铁锅鸭饭"),
(12,"铁锅黄焖鸡"),
(12,"铁锅千页豆腐"),
(13,"北京烤鸭"),
(13,"北京烤鸭"),
(13,"奥尔良烤鸡"),
(13,"迷迭香烤鸡腿"),
(13,"秘制里脊烤肉饭"),
(13,"素菜三种+米饭"),
(13,"烤鸭腿"),
(13,"脆皮鸡排饭"),
(13,"滑蛋火腿"),
(14,"麻酱凉面"),
(14,"麻酱凉皮"),
(14,"凉面"),
(14,"凉皮"),
(14,"牛筋面"),
(14,"醋粉"),
(15,"滑蛋火腿"),
(15,"麻酱凉面"),
(16,"麻酱凉皮"),
(16,"凉面"),
(17,"凉皮"),
(18,"牛筋面"),
(19,"醋粉"),
(19,"老干妈土豆丝"),
(19,"蒸鸡蛋"),
(20,"西红柿鸡蛋"),
(20,"豆角茄子")
$


#now() sql函数仅供测试用 
insert into remark(timeid,userid,recipeid,content,dishscore) values
(now(),13121143,1,'盐放多了',6)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
(now()+1,13121175,2,'能不能给多点阿，实在不够吃啊！',9)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
(now()+2,13121175,3,'也不是很多啊，感觉还好哦',7)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
(now()+3,13121143,10,'也不是很多啊，感觉还好哦',6)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
(now()+4,13121175,1,'越来越难吃',7)$

