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

#update user set username="程志浩",userpasswd="admin",usergrade="大三",userbirth="19940521",usersex="男",useremail="54964238@qq.com",userphone="110" where userid="13121175";


create table window(
windowid int AUTO_INCREMENT ,
windowname varchar(20),
buildname varchar(20) not null,
layer char(2) not null,
windowscore double DEFAULT null,
primary key (windowid),
constraint window_score check(windowscore > -1 and windowscore < 11)
);#ENGINE=InnoDB DEFAULT CHARSET=gbk;
$

create table recipe(
recipeid int AUTO_INCREMENT,
windowid int,
dishname varchar(20),
averagedishscore double,
primary key(recipeid),
foreign key(windowid) references window(windowid) on update cascade on delete cascade
);#ENGINE= InnoDB DEFAULT CHARSET=GBK;
$



create table remark(
timeid datetime,
userid varchar(14) not null,
recipeid int not null,
content text,
dishscore int,#给菜打分分数
likecount int,#点赞次数
unlikecount int,#不赞成次数
primary key(timeid),
foreign key(recipeid) references recipe(recipeid) on update cascade on delete cascade,
foreign key(userid) references user(userid) on update cascade on delete cascade,
constraint dish_score_1 check(dishscore > -1 and dishscore < 11)
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
update recipe set averagedishscore=(select averagedishscore from view_dish_score where recipe.recipeid=view_dish_score.recipeid)
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
update window set windowscore=(select averagewindowscore from view_window_score where view_window_score.windowid=window.windowid)
	where window.windowid = new.windowid;
end$



#创建支持按菜名和窗口名查询的视图
#drop view view_remark$
create view view_remark as(
	select username,window.windowname,dishname,buildname,layer,timeid,content,dishscore,recipe.recipeid #,likecount,unlikecount
	from recipe inner join remark on recipe.recipeid=remark.recipeid inner join window on window.windowid=recipe.windowid inner join user on user.userid=remark.userid 
)$


