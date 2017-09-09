FROM java:8-jre
COPY ./target/uberjar/ /usr/src/app
WORKDIR /usr/src/app
EXPOSE 3000
CMD java -jar session-command.jar
