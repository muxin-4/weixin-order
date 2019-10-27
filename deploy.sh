# 打包文件
mvn clean package -Dmaven.test.skip=true

# 上传文件到服务器
scp target/sell.jar root@106.15.233.81:/opt/javaapps

# 启动 jar 包
# 启动
ssh root@106.15.233.81 `java -jar /opt/javaapps/sell.jar`

# 在 8090 端口启动
#java -jar -Dserver.port=8090 sell.jar

# 启动生产环境
#java -jar -Dserver.port=8090 -Dspring.profiles.active=prod sell.jar

# 在后台一直运行
ssh root@106.15.233.81 `nohup java -jar sell.jar > /dev/null 2>&1 &`

# 查看启动的项目
ps -ef |grep sell.jar
