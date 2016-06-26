@call mvn clean install package -Dmaven.test.skip=true
xcopy deployments\ROOT.war G:\Codewar\Tomcat8\webapps /Y
pause