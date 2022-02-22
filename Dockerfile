FROM openjdk:11

MAINTAINER martin-petersen

COPY target/the-resident-zombie-latest.jar the-resident-zombie-latest.jar
COPY start.sh ./start.sh
RUN chmod +x ./start.sh

ENTRYPOINT ./start.sh