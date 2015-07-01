delimiter $
drop database if exists roast;
create database roast DEFAULT CHARACTER SET gbk COLLATE gbk_chinese_ci;
use roast;

create table user(
userid varchar(14) not null,
username varchar(20) not null,
userpasswd varchar(20) not null,
usergrade varchar(4),
userbirth varchar(8),#date
usersex char(4),
useremail varchar(20),# not null
userphone varchar(11),
userregistertime datetime default null,
primary key(userid)
);#ENGINE=InnoDB DEFAULT CHARSET=gbk;
$

insert into user(userid,username,userpasswd,usergrade,userbirth,usersex,useremail,userphone,userregistertime) values
(13121143,'李献涛','admin','大三','19930826','男','shineneo1@qq.com','18629608829',now()),
(13121175,'程志浩','admin','大三','','男','10000@qq.com','',now());
$

create table window(
windowid int AUTO_INCREMENT ,
windowname varchar(20),
buildname varchar(20) not null,
layer char(2) not null,
windowscore double DEFAULT null,
primary key (windowid),
constraint window_score check(windowscore > -1 and windowscore < 6)
);#ENGINE=InnoDB DEFAULT CHARSET=gbk;
$
#为了提高窗口推荐的查询性能设置均分为索引
ALTER TABLE window ADD INDEX index_window_score (windowscore) ;$


create table recipe(
recipeid int AUTO_INCREMENT,
windowid int,
dishname varchar(20),
averagedishscore double,
primary key(recipeid),
foreign key(windowid) references window(windowid) on update cascade on delete cascade
);#ENGINE= InnoDB DEFAULT CHARSET=GBK;
$
#为了提高窗口推荐的查询性能设置均分为索引
ALTER TABLE recipe ADD INDEX index_recipe_score (averagedishscore) ;$


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

create table remark(
timeid datetime,#时间作为唯一标示id
userid varchar(14) not null,
recipeid int not null,
content text,
dishscore int ,#给菜打分分数
zancount int DEFAULT 0,#点赞次数
caicount int DEFAULT 0,#不赞成次数
primary key(timeid),
foreign key(recipeid) references recipe(recipeid) on update cascade on delete cascade,
foreign key(userid) references user(userid) on update cascade on delete cascade,
constraint dish_score_1 check(dishscore > -1 and dishscore < 6)
);#ENGINE=InnoDB DEFAULT CHARSET=GBK;
$	

#视图为了方便计算一定窗口菜的平均分而创建
#drop view view_dish_score$
create view view_dish_score as(
select avg(dishscore) as averagedishscore,recipeid  from remark group by (recipeid)
)
$

#触发器自动计算一定窗口菜的均分
drop trigger if exists dish_score_avg$
create trigger dish_score_avg
after insert on remark
for each row
begin 
update recipe set averagedishscore=(select averagedishscore from view_dish_score where new.recipeid=view_dish_score.recipeid)
	where recipe.recipeid = new.recipeid;
end$


#视图为了方便计算一定窗口平均分而创建
#drop view view_window_score$
create view view_window_score as(
select avg(averagedishscore) as averagewindowscore,windowid  from recipe group by (windowid)
)
$


#触发器自动计算一定窗口均分
drop trigger if exists window_score_avg$
create trigger window_score_avg
after update on recipe
for each row
begin 
update window set windowscore=(select averagewindowscore from view_window_score where view_window_score.windowid=new.windowid)
	where window.windowid = new.windowid;
end$




insert into remark(timeid,userid,recipeid,content,dishscore) values
("2015-06-27 01:55:38",13121175,2,'能不能给多点阿，实在不够吃啊！',5)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
("2015-06-28 01:48:42",13121175,3,'也不是很多啊，感觉还好哦',4)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
("2015-06-29 01:48:42",13121143,10,'也不是很多啊，感觉还好哦',3)$

insert into remark(timeid,userid,recipeid,content,dishscore) values
("2015-06-23 01:48:42",13121175,1,'越来越难吃',4)$


#创建支持按菜名和窗口名查询的视图
#drop view view_remark$
create view view_remark as(
	select username,window.windowname,dishname,buildname,layer,timeid,content,dishscore,recipe.recipeid ,zancount,caicount
	from recipe inner join remark on recipe.recipeid=remark.recipeid inner join window on window.windowid=recipe.windowid inner join user on user.userid=remark.userid 
)$



create table zan(
zanid int AUTO_INCREMENT,
remarkid datetime,#remark的标示id -> timeid
userid varchar(14),
zanstate tinyint(1),#赞状态
primary key(zanid),
foreign key(remarkid) references remark(timeid) on update cascade on delete cascade,
foreign key(userid) references user(userid) on update cascade on delete cascade
);#ENGINE=InnoDB DEFAULT CHARSET=GBK;
$

create table cai(
caiid int AUTO_INCREMENT,
remarkid datetime,#remark的标示id -> timeid
userid varchar(14),
caistate tinyint(1),#踩状态
primary key(caiid),
foreign key(remarkid) references remark(timeid) on update cascade on delete cascade,
foreign key(userid) references user(userid) on update cascade on delete cascade
);#ENGINE=InnoDB DEFAULT CHARSET=GBK;
$

#drop view view_remark_zancount; $
create view view_remark_zancount as(
select sum(zanstate) as zancount,remarkid from zan group by(remarkid)
)$

#drop view view_remark_caicount; $
create view view_remark_caicount as(
select sum(caistate) as caicount,remarkid from cai group by(remarkid)
)$


#每当又一次赞的信息更新就更新相应remark中赞总数的值
drop trigger if exists remark_zan_calculate$
create trigger remark_zan_calculate
after update on zan
for each row
begin
update remark set zancount=(select zancount from view_remark_zancount where view_remark_zancount.remarkid=new.remarkid)
where remark.timeid=new.remarkid;
end$

#每当有一次赞的信息插入就 更新相应remark中赞总数的值
drop trigger if exists remark_zan_calculate_0$
create trigger remark_zan_calculate_0
after insert on zan
for each row
begin
update remark set zancount=(select zancount from view_remark_zancount where view_remark_zancount.remarkid=new.remarkid)
where remark.timeid=new.remarkid;
end$

#每当又一次踩的信息更新就更新相应remark中赞总数的值
drop trigger if exists remark_cai_calculate$
create trigger remark_cai_calculate
after update on cai
for each row
begin
update remark set caicount=(select caicount from view_remark_caicount where view_remark_caicount.remarkid=new.remarkid)
where remark.timeid=new.remarkid;
end$

#每当有一次踩的信息插入就 更新相应remark中赞总数的值
drop trigger if exists remark_cai_calculate_0$
create trigger remark_cai_calculate_0
after insert on cai
for each row
begin
update remark set caicount=(select caicount from view_remark_caicount where view_remark_caicount.remarkid=new.remarkid)
where remark.timeid=new.remarkid;
end$

insert into zan (remarkid,userid,zanstate) values ("2015-06-27 01:55:38","13121143","1"); $
insert into zan (remarkid,userid,zanstate) values ("2015-06-28 01:48:42","13121175","1"); $
insert into zan (remarkid,userid,zanstate) values ("2015-06-23 01:48:42","13121143","1"); $
insert into cai (remarkid,userid,caistate) values ("2015-06-29 01:48:42","13121143","1"); $
insert into cai (remarkid,userid,caistate) values ("2015-06-27 01:55:38","13121143","1"); $