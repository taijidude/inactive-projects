<# Work with That... https://social.technet.microsoft.com/wiki/contents/articles/30562.powershell-accessing-sqlite-databases.aspx
Diese Datei enth�lt ein paar Funktionen, die den Zugriff auf die SQlite Datenbank kapseln, folgende Funktionen gibt es: 
1. 
2. 
3. 
4. 
TODO's
- Die Ausgabe des in der Function execute-query muss formatiert werden
- Die Methode init-con und cleanup d�rfen nur von Functionen in diesem Framework benutzt werden. 
- Das Script muss ein Modul werden
- Wir brauchen eine Print Methode um die Ausgabe der Tabellen zu formatieren
- Wir m�ssen die Pfad setzen 
- Es muss eine Methode geben, die pr�ft ob die Datenbank vorhanden ist.
#>


function init-con {
    $config  = [xml](gc .\config.xml)
	#Ich könnte auf jedem Rechner eine Umgebunsvarible einrichten? Und diese Variabe steuert dann die Konfiguration
	$config = $config.SelectSingleNode('//configurations/config[@name="netbook"]')	
	Add-Type -Path $config.provider
    $con = New-Object -TypeName System.Data.SQLite.SQLiteConnection
    $con.ConnectionString = $config.database
    $con.Open()
    return $con
}

function cleanup {
    $sql.Dispose()
    $con.Close()
}

function execute-query {
    param(
        [parameter(Mandatory=$True)][string]$text
    )
    $con = init-con
    $sql = $con.CreateCommand()
    $sql.CommandText = $text
    $adapter = New-Object -TypeName System.Data.SQLite.SQLiteDataAdapter $sql
    $data = New-Object System.Data.DataSet
    [void]$adapter.Fill($data)    
    cleanup $sql $con
    return $data
}

function execute-singleResultQuery {
    param(
        [parameter(Mandatory=$True)][string]$text
    )
    $data = execute-query "select id from trackingdata where active = 1"
    $rows = $data.Tables.rows
    $length = $data.Tables.rows.Length
    if($length -gt 1) {
        throw "More than one row returned"

    } elseif($length -eq 0) {
        return $false;
    } else { 
        return $rows[0]
    }
}

function execute-command {
    param(
        [parameter(Mandatory=$True)][string]$text
    )
    $con = init-con
    $sql = $con.CreateCommand()
    $sql.CommandText = $text
    [void]$sql.ExecuteNonQuery()
    cleanup $sql $con
}