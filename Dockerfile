FROM openjdk:8
ENV PATH=$PATH:/opt/gradle/gradle-6.8.3/bin \
#    JDBC_DATABASE_URL= \
#    JDBC_DATABASE_USERNAME= \
#    JDBC_DATABASE_PASSWORD= \
    JDBC_DATABASE_DRIVER=com.mysql.cj.jdbc.Driver \
    SPRING_JPA_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
WORKDIR /app
COPY ./src /app/src
COPY ./pom.xml /app
RUN apt-get update && apt-get install maven -y
RUN mvn clean install -DskipTests && cd target
