echo start to copy elife-mybatis.jar to eservice_api
@echo off
mvn deploy:deploy-file -Dfile=.\target\elife-mybatis.jar -DgroupId=com.twfhclife -DartifactId=eservice -Dversion=mergeProj -Dpackaging=jar -Durl=file:../eservice_api/mvn-repo/ -DrepositoryId=maven-repo -DupdateReleaseInfo=true
echo finished.