#!/bin/sh
echo "releasing $1..."
./gradlew assemble publishReleasePublicationToMavenLocal bintrayUpload -PdeployVersion="$1"