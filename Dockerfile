From tomcat:10.1.48-jdk21

COPY ./target/api.war /usr/local/tomcat/webapps/api.war
CMD ["catalina.sh","run"]