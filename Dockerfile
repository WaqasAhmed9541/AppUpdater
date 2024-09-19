# Stage 1: Build the APK
FROM openjdk:17-jdk-slim AS build-env

# Install system dependencies
RUN apt-get update && \
    apt-get install -y wget unzip && \
    rm -rf /var/lib/apt/lists/*

# Set environment variables for Android SDK
ENV ANDROID_SDK_ROOT=/sdk
ENV PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Download and install Android SDK command line tools
RUN mkdir -p ${ANDROID_SDK_ROOT} && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O /sdk.zip && \
    unzip /sdk.zip -d ${ANDROID_SDK_ROOT} && \
    rm /sdk.zip && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/* ${ANDROID_SDK_ROOT}/cmdline-tools/latest

# Accept SDK licenses and install SDK components
RUN yes | sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --licenses && \
    sdkmanager --update && \
    sdkmanager "platform-tools" "platforms;android-30" "build-tools;30.0.3"

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the APK
RUN ./gradlew assembleDebug

# Stage 2: Final image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the APK from the build stage
COPY --from=build-env /app/app/build/outputs/apk/debug/app-debug.apk .

# Define default command
CMD ["sh", "-c", "echo 'APK built successfully. Use it as needed.'"]
