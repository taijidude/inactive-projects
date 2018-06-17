#Setzt das Properties File für die Produktion und baut die jar
param(
[switch]$prod
)
$setConfigPropertiesFile = {param($old,$new) ((Get-Content .\src\main\java\config\DaoConfig.java) -replace  $old, $new | Set-Content .\src\main\java\config\DaoConfig.java)}
bt
if($prod) {
    & $setConfigPropertiesFile -old 'config_dev' -new 'config_prod'
}
mvn package -DskipTests
bt-mvntarget
cp rest-api.war C:\tools\tomcat\apache-tomcat-8.0.38\webapps
if($prod) {
    & $setConfigPropertiesFile -old 'config_prod' -new 'config_dev'
}