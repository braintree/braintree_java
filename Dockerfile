FROM debian:wheezy

ENV LANG C.UTF-8
ENV JAVA_DEBIAN_VERSION 7u171-2.6.13-1~deb7u1

RUN apt-get update
RUN apt-get -y install --force-yes rake openjdk-7-jdk="$JAVA_DEBIAN_VERSION" ant=1.8.2-4 ant-optional=1.8.2-4 maven2="2.2.1-12+deb7u1" procps

WORKDIR /braintree-java
