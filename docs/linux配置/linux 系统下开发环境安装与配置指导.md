# linux 系统下开发环境安装与配置指导



**源配置**

阿里云源配置官网：https://opsx.alibaba.com/mirror



todo ：暂时不配置阿里云的源



**安装java环境**

[在linux服务器上安装jdk](https://www.jianshu.com/p/10949f44ce9c)



**卸载 mysql**

https://blog.csdn.net/li_wei_quan/article/details/78549891



**安装 mysql（一）**

```
rpm -qa | grep mysql // 先检查系统是否装有mysql
wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm // 下载mysql的repo源
sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm // 安装mysql-community-release-el7-5.noarch.rpm包
```

安装这个包后，会获得两个mysql的yum repo源：

```
/etc/yum.repos.d/mysql-community.repo
/etc/yum.repos.d/mysql-community-source.repo
```

```
sudo yum install mysql-server 安装mysql
mysql -u root
```

**注意：**

If you run into this error message while trying to connect to a local MySQL Server:

```
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/lib/mysql/mysql.sock'
```

It means either the MySQL server is not installed/running, or the file `mysql.sock` doesn’t exist in `/var/lib/mysql/`.

参考链接：

[CentOS7 64位安装mysql教程](https://blog.csdn.net/a774630093/article/details/79270080)

[Can’t connect to local MySQL server through socket [Solved]](https://tableplus.io/blog/2018/08/solved-cant-connect-to-local-mysql-server-through-socket.html)

```
service mysqld restart // 修复 Can’t connect to local MySQL server through socket [Solved] 后， 重启服务
```



**安装 mysql（二）**

https://blog.csdn.net/liang19890820/article/details/81672538



**配置 mysql**

```bash
sudo vim /etc/my.cnf   // 修改 mysql 配置文件，添加默认字符集
character-set-server=utf8 // 在 symbolic-links=0 下面添加默认字符集配置
default-character-set = utf8 // 在 symbolic-links=0 下面添加默认字符集配置

```

**todo mysql 开机自动重启**

```
sudo systemctl enable mysqld.service // 设置 mysql 开机启动

sudo vim /etc/sysconfig/iptables
```



**todo 修改 mysql 登录信息**

```
mysql -u root
select user,host,password from mysql.user
```



**创建数据库**

```bash
create database `mmall` default character set utf8 COLLATE utf8_general_ci;
```



todo 把用户赋予数据库权限



导入数据

```bash
sudo wget http://learning.happymmall.com/mmall.sql
show databases;
use mmall;
show tables;
source /developer/mmall.sql
select * from mmall_user\G;
```





**安装和配置 tomcat**

```bash
cd /developer
sudo wget http://learning.happymmall.com/tomcat/apache-tomcat-7.0.73.tar.gz
sudo tar -zxvf apache-tomcat-7.0.73.tar.gz
mkdir setup
sudo mv apache-tomcat-7.0.73.tar.gz setup/
cd setup/apache-tomcat-7.0.73
cd conf
sudo vim server.xml
/8080 // 在末尾添加 URIEncoding="UTF-8"
添加后如下所示：
 <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8" />
               
               
cd ..
cd bin
sudo ./startup.sh
```



**安装和配置 maven**

```
cd /developer
sudo wget http://learning.happymmall.com/maven/apache-maven-3.0.5-bin.tar.gz
sudo tar -zxvf apache-maven-3.0.5-bin.tar.gz
sudo mv apache-maven-3.0.5-bin.tar.gz setup/

sudo vim /etc/profile

添加下面配置到环境变量中
export M2_HOME=/developer/apache-maven-3.0.5
export MAVEN_OPTS="-Xms256m -Xmx512m"
export PATH=$PATH:$M2_HOME/bin

source /etc/profile
mvn -v
```



**安装 vsftpd**

```
sudo yum -y install vsftpd
cd /
sudo mkdir product
cd product
sudo mkdir ftpfile
sudo useradd ftpuser -d /product/ftpfile -s /sbin/nologin
sudo chown -R ftpuser.ftpuser ./ftpfile/
sudo passwd ftpuser
cd /etc/vsftpd/
sudo vim chroot_list
ftpuser // 空文件，写入 ftpuser，保存

sudo vim /etc/selinux/config
SELINUX=disabled // 检查是否为 disabled
sudo setsebool -P ftp_home_dir 1 

sudo mv vsftpd.conf vsftpd.conf.bak

sudo wget http://learning.happymmall.com/vsftpdconfig/vsftpd.conf
```



**安装 nginx**

```bash
cd /developer/setup
sudo wget http://learning.happymmall.com/nginx/linux-nginx-1.10.2.tar.gz
sudo yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel 
sudo tar -zxvf linux-nginx-1.10.2.tar.gz
cd nginx-1.10.2/

sudo ./configure
sudo make
sudo make install

whereis nginx
cd /usr/local/nginx
cd conf
sudo vim nginx.conf

include vhost/*.conf;  // 添加,在 # HTTPS server
sudo mkdir vhost
sudo wget http://learning.happymmall.com/nginx/linux_conf/vhost/admin.happymmall.com.conf
sudo wget http://learning.happymmall.com/nginx/linux_conf/vhost/happymmall.com.conf
sudo wget http://learning.happymmall.com/nginx/linux_conf/vhost/img.happymmall.com.conf
sudo wget http://learning.happymmall.com/nginx/linux_conf/vhost/s.happymmall.com.conf

sudo /usr/local/nginx/sbin/nginx // 启动 nginx
```



**安装 git**

```
cd /developer/setup
sudo wget http://learning.happymmall.com/git/git-v2.8.0.tar.gz

// 安装 git 依赖
sudo yum -y install zlib-devel openssl-devel cpio expat-devel gettext-devel curl-devel perl-ExtUtils-CBuilder perl-ExtUtils- MakeMaker 
sudo tar -zxvf git-v2.8.0.tar.gz
sudo make prefix=/usr/local/git all
sudo make prefix=/usr/local/git install
git --version
git config --global user.name "wgy"
git config --global user.email "wgy952046097@gmail.com"
git config --global core.autocrlf false
git config --global core.quotepath off
git config --global gui.encoding utf-8
ssh-keygen -t rsa -C "wgy952046097@gmail.com"
```



**配置防火墙**

配置参考链接：https://www.cnblogs.com/kreo/p/4368811.html

```bash
cd /etc/sysconfig
sudo mv iptables iptables.bak
sudo wget http://learning.happymmall.com/env/iptables
sudo vim iptables
注释掉，3306 5005 8080端口
service iptables save
systemctl restart iptables.service
```





**配置自动化发布脚本**

```bash
cd /developer
sudo wget http://learning.happymmall.com/deploy/deploy.sh
sudo vim deploy.sh
sudo mkdir git-repository
cd git-repository
cd /
sudo chown -R wgy /developer
sudo chmod u+w -R /developer/
sudo chmod u+r -R /developer/
sudo chmod u+x -R /developer/
cd /developer/git-repository
git clone git@github.com:shuiseng/mmall.git
cd ..
sudo vim deploy.bash
./depl
```



