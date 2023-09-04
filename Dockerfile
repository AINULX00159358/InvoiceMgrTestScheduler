FROM openjdk:11
MAINTAINER habib.java
COPY target/invoiceMgrTestScheduler-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]