setlocal

.\gradlew --parallel clean :hub:bootBuildImage

SET DOCKER_REPO=755953011917.dkr.ecr.ap-northeast-2.amazonaws.com
SET DOCKER_IMAGE=roadeye/hub
SET DOCKER_TAG=latest-bp-tomcat

docker tag roadeye/hub:latest %DOCKER_REPO%/%DOCKER_IMAGE%:%DOCKER_TAG%

docker push %DOCKER_REPO%/%DOCKER_IMAGE%:%DOCKER_TAG%

endlocal