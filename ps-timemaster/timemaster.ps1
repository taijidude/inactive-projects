<#
Todo: 
* Das DB Framework einbinden
* Es mï¿½ssen Datumswerte gespeichert und passend wieder ausgelesen werden.
* Es muss die Differenz zwischen diesen Datumswerten berechnet werden. 

* Plausi Prï¿½fungen einbauen. 
- Es muss fï¿½r jeden Tag ein Feierabend angegeben werden. Wenn es der Tag danach ist und noch keine Feierabend angegeben wurde wird nach der Zeit gefragt.
* In der Tabelle muss es ein Boolean flag fï¿½r die aktive Aufgabe geben. 
* Wenn tim mit einer Aufgabe gestartet und dann wird tim mit einer 
anderen Aufgabe gestartet, dann wird die erste Aufgabe beendet. 
(Es kann nur eine aktive Aufgabe geben).  

#>
#Diese Function setzt in der config.xml die Angaben zum Modulpfad und zur Datenbank

 . .\db-framework.ps1

function formatOutput {
	param(
		[string]$value
	)
	
	if(!$value) {
		$value = "";
	}

	$value = $value.PadRight(20)
	return $value
} 
 
function track {
    param(
        [parameter(Mandatory=$True)][String]$text
    )
    $currentDatetime = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $activeId = execute-singleResultQuery "select id from trackingdata where active = 1"
    #Es existiert eine active Task
    if($activeId -ne $false) {
        execute-command "update trackingdata set active = 0 where id = $activeId"
        execute-command "update trackingdata set end = '$currentDatetime' where id = $activeId"
    }
    #Die neue aktive Task wird gesetzt 
    $command = "insert into trackingdata(description, start, active) values('$text', '$currentDatetime', 1)"
    execute-command $command
	#Es sollte hier eine RÃ¼ckmeldung geben
	"[INFO]$text wird getrackt..."	
}

function get-data {
	$result = execute-query "select * from trackingdata"
	$header = null;
	$output = New-Object System.Collections.ArrayList
	$startoutput = "==== Timemaster: All recorded data ====";
	$endoutput = "====================================="
	
	$result.tables.rows |  %{   
		$description = formatOutput $_.description 
		$start =  formatOutput $_.start
		$end =  formatOutput $_.end 	
		$output.add("| $description | $start | $end |") 
	}
	
	$startoutput
	foreach ($item in $output) {
		$item
	}
	$endoutput
}

function finish-day {
    #alles inaktiv
    #bei letzte task stop setzen
    #Tagesübersicht aufrufen
}

