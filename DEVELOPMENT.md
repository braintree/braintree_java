Development Notes
=================

Notes for developing the Braintree Java SDK.

## Table of Contents

* [Building](#building)
* [Linting](#linting)
* [Testing](#testing)

## Building

The `Makefile` and `Dockerfile` will build an image containing the dependencies and drop you to a terminal where you can run tests.

```
make
```

## Linting

This project uses Checkstyle with the Google Java Style configuration.

To run Checkstyle:

```
mvn site
```

View the results in the browser by visiting `file:///path/to/braintree-java/target/site/checkstyle.html`.

## Testing

The unit tests can be run by anyone on any system, but the integration tests are meant to be run against a local development server of our gateway code. These integration tests are not meant for public consumption and will likely fail if run on your system. To run unit tests use rake(`rake test:unit`) or Maven(`mvn verify -DskipITs`).

