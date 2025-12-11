# 基础镜像
FROM openjdk:17-jdk-slim

# 作者
LABEL maintainer="yong"

# 配置
ENV JAVA_OPTS=""
ENV PARAMS=""

# 时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /log/xybjz

# 添加应用
ADD target/xybjz-app.jar /xybjz-app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /xybjz-app.jar $PARAMS"]