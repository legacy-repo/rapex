###################
# STAGE 1: builder
###################

# Build currently doesn't work on > Java 11 (i18n utils are busted) so build on 8 until we fix this
FROM adoptopenjdk/openjdk8:latest as builder

WORKDIR /app/source

ENV FC_LANG en-US
ENV LC_CTYPE en_US.UTF-8

COPY build/sources.list /etc/apt/sources.list

# bash:    various shell scripts
# wget:    installing lein
# git:     ./bin/version
# make:    backend building
# gettext: translations
RUN apt-get update && apt-get install -y coreutils bash git wget make gettext

# lein:    backend dependencies and building
ADD ./bin/lein /usr/local/bin/lein
RUN chmod 744 /usr/local/bin/lein
RUN lein upgrade

# install dependencies before adding the rest of the source to maximize caching

# backend dependencies
ADD project.clj .
RUN lein deps

# add the rest of the source
ADD . .

# build the app
RUN bin/build

# install updated cacerts to /etc/ssl/certs/java/cacerts
# RUN yum install -y java-cacerts

# ###################
# # STAGE 2: runner
# ###################

FROM graalvm-ce:java11-22.2.0 as runner

LABEL org.opencontainers.image.source https://github.com/rapex-lab/rapex

ENV PATH="$PATH:/app/bin:$GRAALVM_HOME/bin"
ENV PYTHONDONTWRITEBYTECODE=1
ENV FC_LANG en-US
ENV LC_CTYPE en_US.UTF-8

WORKDIR /app

RUN gu install r

# Add rapex script and uberjar
RUN mkdir -p bin target/uberjar
COPY --from=builder /app/source/target/uberjar/rapex.jar /app/target/uberjar/
COPY --from=builder /app/source/bin /app/bin

# Expose our default runtime port
EXPOSE 3000

# Run it
ENTRYPOINT ["/app/bin/start"]