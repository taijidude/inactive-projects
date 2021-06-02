import shutil, os, pathlib

#Erzeugt eine Datei und schreibt den Content rein
def createFile(path, content):
    with open(path, 'x', encoding="utf-8") as file: 
         file.write(content)

#Liest die erste Zeile aus einer Datei
def getFirstLine(toRead):
    result = ''
    with open(toRead, 'rt', encoding="utf-8") as toRead: 
        result = toRead.readline()
    return result    

#Liest die komplette Datei in einen String
def readFile(toRead):
    result = ''
    with open(toRead, 'rt', encoding="utf-8") as toRead: 
        result = toRead.read()
    return result   

#schreibt den Text in die uebergebene Datei
#wirft einen Fehler, wenn die Datei existiert
def writeToFile(toWrite, text):
    with open(toWrite, 'x') as target:
        target.write(text)

#Kopiert einen kompletten Ordner rekursiv    
def copyFolder(source, target):
    shutil.copytree(source, target)

def delDir(dir):
    shutil.rmtree(dir)

#prueft ob ein Verzeichnis leer ist
def isDirEmpty(toCheck):
    if len(os.listdir(toCheck)) == 0:
        return True
    else: 
        return False

#prueft ob eine Datei leer ist
def isFileEmpty(toCheck): 
    return len(readFile(toCheck)) == 0

#Loescht alle Files im Ausgabeverzeichnis, die dem Pattern entsprechen
def cleanDir(dir):
    shutil.rmtree(dir)
    os.mkdir(dir)

def mkdir(dir):
    os.mkdir(dir)