FROM maven:3.6.0-jdk-8-alpine

WORKDIR /usr/src/bot
COPY . /usr/src/bot

RUN mvn clean package
CMD ["sh", "-c", "java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=8989 -Dcom.sun.management.jmxremote.rmi.port=8989 -jar /usr/src/bot/jar/aesthethicc-bot-1.0-SNAPSHOT.jar"]
