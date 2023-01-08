FROM openjdk:17.0.2-slim-bullseye

ARG ANDROID_SDK_VERSION
ARG BUILDTOOLS_VERSION
ARG FLANK_VERSION

ENV SDK_URL="https://dl.google.com/android/repository/commandlinetools-linux-8092744_latest.zip" \
    ANDROID_HOME="/usr/local/android-sdk" \
    FLANK_HOME="/usr/local"

RUN apt-get update
RUN yes | apt-get install wget unzip

RUN mkdir "$ANDROID_HOME" .android \
    && cd "$ANDROID_HOME" \
    && wget --quiet $SDK_URL -O sdk.zip \
    && unzip sdk.zip \
    && rm sdk.zip

RUN yes | $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME --licenses

RUN $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME --update

RUN yes | $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME \
    "build-tools;${BUILDTOOLS_VERSION}" \
    "platforms;android-${ANDROID_SDK_VERSION}"

RUN yes | apt-get install python3.9 python3-pip

RUN wget --quiet https://github.com/Flank/flank/releases/download/v$FLANK_VERSION/flank.jar -O $FLANK_HOME/flank.jar

RUN mkdir /application

WORKDIR /application
