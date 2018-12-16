FROM frekele/ant as builder
MAINTAINER Markus Heene <markus.heene@gmail.com>

COPY Servlet-Version /usr/src/Servlet-Version
WORKDIR /usr/src/Servlet-Version
RUN ant -f build.xml -Dapp.name=dashboard

#FROM tomcat:9.0.10-jre10-slim
FROM tomcat:9-jre11-slim
RUN rm -rf /usr/local/tomcat/webapps/examples* && rm -rf /usr/local/tomcat/webapps/docs
COPY --from=builder /usr/src/Servlet-Version/dist/dashboard.war /usr/local/tomcat/webapps/
RUN echo "<html><head><meta http-equiv=\"refresh\" content=\"0; url=/dashboard\" /></head><body></body></html>" > /usr/local/tomcat/webapps/ROOT/index.html
