#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "chkal/backset" ] &&
    [ "$TRAVIS_BRANCH" == "master" ] &&
    [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
    [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ]; then

  echo "Starting snapshot deployment..."
  mvn -s .travis-settings.xml -DperformRelease -DskipTests -Dgpg.skip=true deploy
  echo "Snapshots deployed!"

else
  echo "Skipping snapshot deployment..."
fi
