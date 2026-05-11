FROM dockerhub.braintree.tools/bt/java:8-bookworm

RUN apt-get update
RUN apt-get -y install --force-yes rake ant ant-optional maven procps

WORKDIR /braintree-java
