#!/bin/bash
./gradlew clean && ./gradlew assembleDebug
mv ./app/build/outputs/apk/app-debug.apk ./output
./gradlew clean
