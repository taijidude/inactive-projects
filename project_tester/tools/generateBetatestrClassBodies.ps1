<#
    Dieses Skript kann verwendet werden um leere Klasserümpfe in project_tester zu erzeugen.
    Es braucht den Namen des Objekts als Parameter hinter -name. 
    Es werden folgende Klassen erzeugt: 
    * Controller (autowired Service )
    * Service, ServiceIF (autowired Dao)
    * Dao, DaoIF
    * optional: dto
    TODO: 
    - Diese Klassen müssen noch die richtigen Imports bekommen

#>
Param(
    [Parameter(Mandatory=$True)]
    [string]$name
)
$sourceDir = "F:\data\projects\project_tester\project_tester\src\main\java"
$classNameGeneration = {param($name,$type)Return "$name"+($type.Substring(0,1)).ToUpper()+($type.Substring(1))}
$interfaceNameGeneration = {param($name,$type)Return "$name"+($type.Substring(0,1)).ToUpper()+($type.Substring(1))+"IF"}

$controllerName = & $classNameGeneration -name $name -type controller
$serviceInterfaceName = & $interfaceNameGeneration -name $name -type service
$serviceName = & $classNameGeneration -name $name -type service
$daoInterfaceName = & $interfaceNameGeneration -name $name -type dao
$daoName = & $classNameGeneration -name $name -type dao

function create($name, $type, $content) {
   $dir = $sourceDir+"\$type"
   $fileName = $name+".java"
   $filePath = "$dir\$fileName"
   cd $dir
   New-Item $filePath -ItemType file
   Set-Content $filePath $content
}

"[INFO]create controller class..."
$controllerContent = @"
package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@EnableWebMvc
public class {0} {{
    
    @Autowired
    private {1} service;

}}
"@
$controllerContent = $controllerContent -f $controllerName,$serviceInterfaceName
create $controllerName controller $controllerContent  

"[INFO]Create service interface..."
$serviceInterfaceContent = @"
package service;

public interface {0} {{

}}
"@
$serviceInterfaceContent = $serviceInterfaceContent -f $serviceInterfaceName
create $serviceInterfaceName service $serviceInterfaceContent 

"[INFO]Create service class..."
$serviceContent = @"
package service;

import org.springframework.beans.factory.annotation.Autowired;
@Service
public class {0} implements {1} {{
    
    @Autowired
    private {2} dao;
}}
"@
$serviceContent = $serviceContent -f $serviceName,$serviceInterfaceName,$daoInterfaceName
create $serviceName service $serviceContent 
"[INFO]Create dao interface..."
$daoInterfaceContent = @"
package dao;

public interface {0} {{

}}
"@
$daoInterfaceContent = $daoInterfaceContent -f $daoInterfaceName
create $daoInterfaceName dao $daoInterfaceContent
"[INFO]Create dao class..."
$daoContent = @"
package dao;
@Respository
public class {0} implements {1} {{

}}
"@
$daoContent = $daoContent -f $daoName,$daoInterfaceName
create $daoName dao $daoContent
#TODO: Hier müssen auch gleich die EntityKlasse, das DTO und die Testklassen generiert werden