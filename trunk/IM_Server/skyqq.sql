-- 创建skyqq数据库
create database skyqq;
use skyqq;

-- 创建用户表
create table Users(
  uId int primary key auto_increment,
  password varchar(16),
  nickName varchar(16),
  faceImg varchar(30) default '1.jpg'
);

-- 创建好友分组表
create table friendGroup(
  frId int primary key auto_increment,
  uId int references Users(uId),
  name varchar(16)
);

-- 创建好友列表
create table friendList(
  flId int primary key auto_increment,
  frId int references friendGroup(fRid),
  friendId int references Users(uId)
);

-- 创建消息表
create table Message(
  mId int primary key auto_increment,
  uId int,
  mes text
);

-- 创建QQ群表
create table QQGroup(
  qGid int primary key auto_increment,
  name varchar(20),
  creatorId int references Users(uId)
);

-- 创建QQ群成员
create table QQGroupMember(
   qGmid int primary key auto_increment,
   qGid int references QQGroup(qGid),
   uid int references Users(uId)
);

-- 创建用户登录存储过程
create procedure UP_Users_Login(in $uId int,in $password varchar(16))
begin
  select count(*) as total from Users where uId=$uId and password=$password;
end;

-- 用户注册存储过程
create procedure UP_Users_Add(in $nickName varchar(16),in $password varchar(16))
begin
  insert into Users(nickName,password) values($nickName,$password);
  select AUTO_INCREMENT as qqNum from INFORMATION_SCHEMA.TABLES Where table_schema = 'skyqq' AND table_name = 'users';
end;

-- 获取用户信息
create procedure UP_Users_GetInfo(in $uId int)
begin
  select nickName,faceImg from Users where uId=$uId;
end;

-- 获取用户分组好友信息
create procedure UP_Users_GetFriends(in $id int)
begin
  select fr.frId,fr.name,u.uId,u.nickName,u.faceImg from friendGroup as fr left join friendList as fl on fr.frId=fl.frId left join Users as u on fl.friendId=u.uId where fr.uId=$id;
end;

-- 获取用户离线消息
create procedure UP_Message_Get(in $uId int)
begin
  select mes from Message where uId=$uId;
end;

-- 添加离线消息
create procedure UP_Message_Insert(in $uId int,in $mes text)
begin
  insert into Message(uId,mes) values($uId,$mes);
end;

-- 删除离线消息
create procedure UP_Message_Delete(in $uId int)
begin
  delete from Message where uid=$uId;
end;

-- 某表所有行
create procedure UP_Table_Row(in $tableName varchar(16),in $whereStr varchar(70))
begin
  declare stmt varchar(1000);
  declare sSql varchar(4000);
  set sSql=concat('select count(*) as total from ' , $tableName);
  if ($whereStr <> '') and ($whereStr is not null)   then
      set sSql=concat(sSql,' where ',$whereStr);
  end if;
  set @sQuery = sSql;
  prepare stmt from @sQuery;  
  execute stmt ;
end;

-- 某表分页
create procedure UP_Table_Page(in $tableName varchar(16),in $whereStr varchar(70),in $startI int,in $endI int)
begin
  declare stmt varchar(1000);
  declare sSql varchar(4000);
  set sSql=concat('select * from ' , $tableName);
  if ($whereStr <> '') and ($whereStr is not null)   then
     set sSql=concat(sSql,' where ' , $whereStr , ' LIMIT ', $startI, ', ' ,  $endI);
  end if;
  set @sQuery = sSql;
  prepare stmt FROM @sQuery;  
  execute stmt ;
end;

-- 添加默认好友分组
create procedure UP_FriendGroup_Add_Default(in $uId int)
begin
  insert into friendGroup(uId,name) values($uId,'我的好友');
  insert into friendGroup(uId,name) values($uId,'我的同事');
  insert into friendGroup(uId,name) values($uId,'我的家人');
  insert into friendGroup(uId,name) values($uId,'黑名单');
end;

-- 添加好友分组
create procedure UP_FriendGroup_Add(in $qqNum int,in $groupName varchar(16))
begin
   insert into friendGroup(uId,name) values($qqNum,$groupName);
end;

-- 获取QQ好友分组
create procedure UP_FriendGroup_Get(in $qqNum int)
begin
   select * from friendGroup where uId=$qqNum;
end;

-- 获取分组好友列表
create procedure UP_FriendListByGroupId_Get(in $qqNum int,in $friendGroupId int)
begin
   select u.uId,u.nickName,u.faceImg from friendGroup as fr inner join friendList as fl on fr.frId=fl.frId inner join Users as u on fl.friendId=u.uId where fr.frId=$friendGroupId and fr.uId=$qqNum;
end;

-- 获取所有好友列表
create procedure UP_FriendList_Get(in $qqNum int)
begin
   select u.uId from friendGroup as fr inner join friendList as fl on fr.frId=fl.frId inner join Users as u on fl.friendId=u.uId where fr.uId=$qqNum;
end;

-- 查询好友
create procedure UP_FriendList_Search(in $qqNum int,in $ai int,in $zi int)
begin
  declare sSql varchar(200);
  set sSql=concat('select uId,nickName,faceImg from users where uId not in(select fl.friendId from friendList as fl inner join friendGroup as fg on fl.flId=fg.frId where fg.uId=', $qqNum , ') and uId !=' , $qqNum, ' limit ', $ai , ',',$zi);
  set @sQuery = sSql;
  prepare stmt FROM @sQuery;  
  execute stmt ;
end;

-- 统计好友数量
create procedure UP_FriendList_Total(in $qqNum int)
begin
   select count(uId) as total from users where uId not in(select fl.friendId from friendList as fl inner join friendGroup as fg on fl.flId=fg.frId where fg.uId=$qqNum) and uId !=$qqNum;
end;

-- 插入测试数据
insert into users values (10000, 1, '二愣子','1.jpg');
insert into users values (10001, 1, '王麻子','2.jpg');
insert into users values (10002, 1, '小李子','3.jpg');
insert into users values (10003, 1, '三德子','4.jpg');

insert into friendgroup values (1, 10000, '我的好友');
insert into friendgroup values (2, 10000, '我的同事');
insert into friendgroup values (3, 10000, '我的家人');
insert into friendgroup values (4, 10000, '黑名单');
insert into friendgroup values (5, 10001, '我的好友');
insert into friendgroup values (6, 10001, '我的同事');
insert into friendgroup values (7, 10001, '我的家人');
insert into friendgroup values (8, 10001, '黑名单');
insert into friendgroup values (9, 10002, '我的好友');
insert into friendgroup values (10, 10002, '我的同事');
insert into friendgroup values (11, 10002, '我的家人');
insert into friendgroup values (12, 10002, '黑名单');
insert into friendgroup values (13, 10003, '我的好友');
insert into friendgroup values (14, 10003, '我的同事');
insert into friendgroup values (15, 10003, '我的家人');
insert into friendgroup values (16, 10003, '黑名单');

insert into friendlist values (1, 1, 10001);
insert into friendlist values (2, 1, 10002);
insert into friendlist values (3, 1, 10003);
insert into friendlist values (4, 5, 10000);
insert into friendlist values (5, 5, 10002);
insert into friendlist values (6, 5, 10003);
insert into friendlist values (7, 9, 10000);
insert into friendlist values (8, 9, 10001);
insert into friendlist values (9, 9, 10003);
insert into friendlist values (10, 13, 10000);
insert into friendlist values (11, 13, 10001);