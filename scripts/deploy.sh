#!/bin/sh
echo "releasing $1..."
./gradlew publishReleasePublicationToMavenLocal bintrayUpload -PdeployVersion="$1"