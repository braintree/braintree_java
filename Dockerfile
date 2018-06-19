FROM debian:jessie

ENV LANG C.UTF-8
ENV JAVA_DEBIAN_VERSION 7u111-2.6.7-1~deb8u1

RUN apt-get update
RUN apt-get -y install --force-yes rake ant ant-optional maven2 procps \
  openjdk-7-jre-headless="$JAVA_DEBIAN_VERSION" \
  openjdk-7-jre="$JAVA_DEBIAN_VERSION" \
  openjdk-7-jdk="$JAVA_DEBIAN_VERSION"

WORKDIR /braintree-java
