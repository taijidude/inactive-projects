article-section: 
title=Skripte lassen sich leichter in der Shell ausführen...
date=01.08.2019
article-section:
Python skripte lassen sich in Shells mit etwas weniger Tipperei ausführen, wenn ihr folgendes macht: 

* In der ersten Zeile des Skripte muss die sogenannte shebang Zeile untergebracht sein. (Keine Ahnung warum das so heißt...)

```Python
#!/usr/bin/env python
```
gs
Beispiel: 

* Ihr müsst in der  Shell die Datei ausführbar machen: 

```shell
chmod +x generate.py
```

