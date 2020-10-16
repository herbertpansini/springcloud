FROM openjdk:8
VOLUME /tmp
RUN mvn clean install -DskipTests
ADD ./target/springboot-servicio-productos-0.0.1-SNAPSHOT.jar servicio-productos.jar
ENTRYPOINT ["java","-jar","/servicio-productos.jar"]