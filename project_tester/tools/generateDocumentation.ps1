
project_tester
$completeOutPut = @()
$completeOutPut = $completeOutPut + "URL;Methode;Parameter;Rueckgabewert;Beschreibung"
$controllerFiles = ls -r -i *Controller.java
foreach($file in $controllerFiles) {
    $matches = ([regex]'(\n\s+/\*\*)(\s+\*.+\r\n)+(\s+\*/\r\n)(\s+@RequestMapping.+\r\n)').Matches((gc $file -Raw));
    foreach ($match in $matches) {
        
        $paramExist = $match.toString() -like "*@param*"    
        $returnExist = $match.toString() -like "*@return*"
        $throwsExist = $match.toString() -like "*@throws*"

        $preconditions = $paramExist -and $returnExist -and $throwsExist

        if($preconditions -eq $FALSE) {
            throw "$file Dieser Beschreibungstext enthält nicht die erforderlichen @param @return und @throws: $match"
        }

        $output = $match
        $output = $output -replace '@RequestMapping\(value="', '|'
        $output = $output -replace '", method=RequestMethod.', '|'
        $output = $output -replace '\)',  ''
        $parts = $output -split '\|'
        
        $description = $parts[0]
        $description = $description -replace '/', ''
        $description = $description -replace '\* ', ''
        $description = $description -replace '\*', ''
        $description = $description -replace '\t', ''
        $description = $description.Trim()
        
        $url = $parts[1]
        $method = $parts[2] -replace '\r\n',''

        #Die Parameter aus der Beschreibung extrahieren
        $parameter=([regex]'@param(.+\n)+(.+@return)').Matches($description)[0]
        if($parameter) {
            $parameter = $parameter -replace '@return', '' 
            $parameter = $parameter -replace '@param', ''
            $parameter = $parameter -replace '\r\n', ''
            $parameter = $parameter.Trim()
        } else {
            $parameter = '---'
        }
        
        #Return Wert aus der Beschreibung extrahieren
        $returnValue=([regex]'@return(.+\n)+(.+@throws)').Matches($description)[0]
        if($returnValue) {
            $returnValue = $returnValue -replace '@return', '' 
            $returnValue = $returnValue -replace '@param', ''
            $returnValue = $returnValue -replace '\r\n', ''
            $returnValue = $returnValue -replace '@throws.*$', ''
            $returnValue = $returnValue.Trim()
        } else {
            $returnValue = '---'
        }

        $description = $description -replace '\r\n', ''
        $description = $description -replace '@param.+$',''
        $description = $description -replace '@return.+$', ''
        $completeOutPut = $completeOutPut + ($url+";"+$method+";"+$parameter+";"+$returnValue+";"+$description)
    }
}
$completeOutPut | Out-File ./dokumentation/REST-Methoden.csv -Force
scriptsPS
