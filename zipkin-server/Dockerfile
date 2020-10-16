FROM openjdk:8
VOLUME /tmp
RUN mvn clean install -DskipTests
EXPOSE 9411
ADD ./zipkin-server-2.21.7-exec.jar zipkin-server.jar
ENTRYPOINT ["java","-jar","/zipkin-server.jar"]
