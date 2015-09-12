 @call mvn clean install package -Dmaven.test.skip=true
#xcopy deployments\ROOT.war E:\Software\office\wildfly-9.0.1.Final\wildfly-9.0.1.Final\standalone\deployments /Y
xcopy deployments\ROOT.war E:\Software\office\jboss-as-7.1.1.Final\standalone\deployments /Y
#xcopy deployments\service.war E:\Software\office\wildfly-9.0.1.Final\wildfly-9.0.1.Final\standalone\deployments /Y
pause