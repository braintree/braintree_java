FROM debian:stretch

ENV LANG C.UTF-8

RUN apt-get update
RUN apt-get -y install --force-yes rake ant ant-optional maven procps \
  openjdk-8-jre-headless \
  openjdk-8-jre \
  openjdk-8-jdk

WORKDIR /braintree-java
