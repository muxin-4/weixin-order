# centos7 安装 mysql5.7

CentOs7 安装 Mysql5.7
1、下载mysql源安装包
wget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
2、安装mysql源
yum localinstall mysql57-community-release-el7-8.noarch.rpm
3、检查mysql源是否安装成功
yum repolist enabled | grep "mysql.*-community.*"
返回

[root@VM_18_105_centos ~]# yum repolist enabled | grep "mysql.*-community.*"
mysql-connectors-community/x86_64    MySQL Connectors Community               63
mysql-tools-community/x86_64         MySQL Tools Community                    69
mysql57-community/x86_64             MySQL 5.7 Community Server              287
看到上图所示表示安装成功。

也可以修改 vim /etc/yum.repos.d/mysql-community.repo源，改变默认安装的mysql版本。比如要安装5.6版本，将5.7源的enabled=1改成enabled=0。然后再将5.6源的enabled=0改成enabled=1即可。改完之后的效果如下所示：

..........
# Enable to use MySQL 5.5
[mysql55-community]
name=MySQL 5.5 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.5-community/el/7/$basearch/
enabled=0 # 这里 0表示不选
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

# Enable to use MySQL 5.6
[mysql56-community]
name=MySQL 5.6 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.6-community/el/7/$basearch/
enabled=0 # 这里 0表示不选
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

[mysql57-community]
name=MySQL 5.7 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.7-community/el/7/$basearch/
enabled=1 # 这里 1 表示 选中
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
..........
4、安装MySQL
yum install mysql-community-server
5、启动MySQL服务
systemctl start mysqld
查看MySQL的启动状态

[root@VM_18_105_centos ~]# systemctl status mysqld
● mysqld.service - MySQL Server
   Loaded: loaded (/usr/lib/systemd/system/mysqld.service; enabled; vendor preset: disabled)
   Active: active (running) since 四 2018-08-23 15:27:28 CST; 1h 26min ago
     Docs: man:mysqld(8)
           http://dev.mysql.com/doc/refman/en/using-systemd.html
  Process: 21453 ExecStart=/usr/sbin/mysqld --daemonize --pid-file=/var/run/mysqld/mysqld.pid $MYSQLD_OPTS (code=exited, status=0/SUCCESS)
  Process: 21432 ExecStartPre=/usr/bin/mysqld_pre_systemd (code=exited, status=0/SUCCESS)
 Main PID: 21457 (mysqld)
   Memory: 202.1M
   CGroup: /system.slice/mysqld.service
           └─21457 /usr/sbin/mysqld --daemonize --pid-file=/var/run/mysqld/mysqld.pid
6、设置开机启动
 systemctl enable mysqld
 systemctl daemon-reload
7、获取root登陆密码
mysql安装完成之后，在/var/log/mysqld.log文件中给root生成了一个默认密码。通过下面的方式找到root默认密码，然后登录mysql进行修改：

[root@VM_18_105_centos ~]#  grep 'temporary password' /var/log/mysqld.log
2018-08-23T06:10:44.014590Z 1 [Note] A temporary password is generated for root@localhost: thI/5wEl_chk
ps:如果没有返回，找不到root密码，解决方案：

# 1删除原来安装过的mysql残留的数据（这一步非常重要，问题就出在这）
rm -rf /var/lib/mysql
# 2重启mysqld服务
systemctl restart mysqld
# 3再去找临时密码
grep 'temporary password' /var/log/mysqld.log
原因有可能是之前安装过一次，没有安装好。

8、登陆
[root@VM_18_105_centos ~]# mysql -uroot -p
---- 输入密码：thI/5wEl_chk
# 修改密码
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '123456Aa!';
MySql 默认密码级别一定要有大小写字母和特殊符号，所以比较麻烦。

9、修改密码策略
在/etc/my.cnf文件添加validate_password_policy配置，指定密码策略

# 0（LOW）：验证 Length
# 1（MEDIUM）：验证 Length; numeric, lowercase/uppercase, and special characters
# 2（STRONG）：验证 Length; numeric, lowercase/uppercase, and special characters; dictionary file
validate_password_policy=0
当然如果不需要密码策略，可以禁用：
在/etc/my.cnf文件添加

validate_password = off
重启生效：

systemctl restart mysqld
Mysql的root用户，只能本地访问，这里在创建一个远程可以访问的 用户。

 GRANT ALL PRIVILEGES ON *.* TO 'its'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
10、忽略大小写
登陆mysql查看

mysql> show variables like "%case%";
+------------------------+-------+
| Variable_name          | Value |
+------------------------+-------+
| lower_case_file_system | OFF   |
| lower_case_table_names | 0     |  ##0区分 1 不区分
+------------------------+-------+
2 rows in set (0.00 sec)
修改配置文件 /etc/my.cnf 添加：

# 0：区分大小写，1：不区分大小写
lower_case_table_names =1
重启后生效：

systemctl restart mysqld
11、用户权限
把数据库迁移到新的服务器上,执行存储过程时出现了如下问题:

execute command denied to user ‘用户名’@’%’ for routine ‘函数名称’
后来一查原来是权限问题,只要用下面的语句改一下相应用户的权限就可以了:

GRANT ALL PRIVILEGES ON *.* TO ‘用户名’@’%’ ;
FLUSH PRIVILEGES;
相应的撤消权限命令:

REVOKE ALL PRIVILEGES ON *.* FROM  ‘用户名’@’%’ ;
FLUSH PRIVILEGES;
12、跑脚本的时候：
ERROR 1067 (42000): Invalid default value for 'FAILD_TIME' （对TIMESTAMP 类型的子段如果不设置缺省值或没有标志not null时候在创建表时会报这个错误）
这是因为sql_mode中的NO_ZEROR_DATE导制的，在strict mode中不允许'0000-00-00'作为合法日期
使用下面的命令查看sql_mode

mysql>show variables like 'sql_mode';

+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
| Variable_name | Value                                                                                                                                     |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
| sql_mode      | ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------+
将上面的NO_ZERO_DATE改为下面的ALLOW_INVALID_DATES

mysql> set sql_mode='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
上面的设置是临时设置，在重新登陆后，该设置又恢复为NO_ZERO_DATE

参考：
[CentOs7 安装 Mysql5.7](https://yq.aliyun.com/articles/637827)
