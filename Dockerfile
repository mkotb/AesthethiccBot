FROM maven:3.6.0-jdk-8-alpine

ENV JVM_ARGS

WORKDIR /usr/src/bot
COPY . /usr/src/bot

RUN mvn clean package
CMD ["sh", "-c", "java -jar /usr/src/bot/jar/aesthethicc-bot-1.0-SNAPSHOT.jar ${JVM_ARGS}"]
