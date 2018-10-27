FROM openjdk:11-jdk-slim
MAINTAINER Markku Korkeala <markku.korkeala@iki.fi>

ADD target/uberjar/twitter-rss.jar /twitter-rss/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/twitter-rss/app.jar"]
