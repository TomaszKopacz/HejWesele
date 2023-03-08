FROM openjdk:17.0.2-slim-bullseye

ARG ANDROID_SDK_VERSION
ARG BUILDTOOLS_VERSION
ARG GRADLE_VERSION
ARG GRADLE_DISTRIBUTION_URL

ENV SDK_URL="https://dl.google.com/android/repository/commandlinetools-linux-8092744_latest.zip"
ENV ANDROID_HOME="/usr/local/android-sdk"

RUN apt-get update
RUN yes | apt-get install wget unzip

RUN mkdir "$ANDROID_HOME" .android && cd "$ANDROID_HOME" && wget --quiet $SDK_URL -O sdk.zip && unzip sdk.zip && rm sdk.zip

RUN yes | $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME --licenses

RUN $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME --update

RUN $ANDROID_HOME/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_HOME "build-tools;$BUILDTOOLS_VERSION" "platforms;android-$ANDROID_SDK_VERSION"

RUN mkdir -p /root/.gradle/wrapper/dists

RUN wget --quiet ${GRADLE_DISTRIBUTION_URL} && unzip -qq gradle-${GRADLE_VERSION}-all.zip -d /root/.gradle/wrapper/dists && rm gradle-${GRADLE_VERSION}-all.zip && mv -v /root/.gradle/wrapper/dists/gradle-$GRADLE_VERSION /root/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-all

ENV GRADLE_HOME /root/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-all
ENV PATH $PATH:$GRADLE_HOME/bin
ENV GRADLE_VERSION $GRADLE_VERSION

RUN gradle -v

RUN find / -type d -name "gradle-${GRADLE_VERSION}-all"

RUN mkdir /application

WORKDIR /application
