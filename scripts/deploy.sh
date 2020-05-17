#!/bin/sh
./gradlew publishReleasePublicationToMavenLocal bintrayUpload -PdeployVersion="$0"