.PHONY: console build

console: build
	docker run -it -v="$(PWD):/braintree-java" --net="host" braintree-java /bin/bash -l

build:
	docker build -t braintree-java .
