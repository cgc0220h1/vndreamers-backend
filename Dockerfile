FROM openjdk:8
ENV PATH=$PATH:/opt/gradle/gradle-6.8.3/bin
WORKDIR /app
COPY ./src /app/src
COPY ./build.gradle /app
RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.8.3-bin.zip
RUN mkdir /opt/gradle
RUN unzip -d /opt/gradle gradle-6.8.3-bin.zip
RUN rm gradle-6.8.3-bin.zip
#RUN gradle build
