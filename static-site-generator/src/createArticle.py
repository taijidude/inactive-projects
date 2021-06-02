#!/usr/bin/env python3
from string import Template
from pathlib import Path
from datetime import datetime
from sys import argv

templateText = """\
article-section: 
title=$title
date=$date
article-section:
"""

titleInput = argv[1]
#prüfen, ob es eine Datei mit diesem Namen schon gibt
#WIe ist es mit den Dateinamen, die muss ich in eine passende Form bringen. 
#Ich muss sonderzeichen beachten
#Ich muss das aktuelle Datum als Timestamp setzen
print('Artikelrumpf wird erstellt!...' + titleInput)
creationDate = datetime.now()
creationDate = creationDate.strftime('%d.%m.%Y %H:%M')

fileText = Template(templateText).substitute(title=titleInput, date=creationDate)
with open('in/'+titleInput+'.md', 'w+') as newFile:
        newFile.write(fileText)




