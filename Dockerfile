FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/session-command.jar /session-command/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/session-command/app.jar"]
