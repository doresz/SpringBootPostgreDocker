FROM openjdk:11-jdk 
EXPOSE 8080
ADD target/springbootoauth2.jar springbootoauth2.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","springbootoauth2.jar"]
