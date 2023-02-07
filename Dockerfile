# Docker example using fatjar
# - docker build -t example/starter-vertx-example .
# - docker run -t -i -p 8888:8888 example/starter-vertx-example

#docker run amazoncorretto:17 java -version
FROM amazoncorretto:17

ENV FAT_JAR starter-1.0.0-SNAPSHOT-fat.jar

ENV APP_HOME /Users/nathalieghalayini/Desktop/docker-app/

EXPOSE 8888

WORKDIR $APP_HOME
COPY build/libs/$FAT_JAR $APP_HOME

ENTRYPOINT ["sh","-c"]
CMD ["exec java -jar $FAT_JAR"]


