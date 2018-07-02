FROM debian:jessie

ENV LANG C.UTF-8

RUN apt-get update
RUN apt-get -y install --force-yes rake ant ant-optional maven2 procps \
  openjdk-7-jre-headless \
  openjdk-7-jre \
  openjdk-7-jdk

WORKDIR /braintree-java
