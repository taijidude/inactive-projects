article-section: 
title=StringBuilder statt normaler String Verkettung
date=06.07.2019
article-section:
Die String Verkettung in Java ist ziemlich imperformant. Das man stattdessen eher einen StringBuilder nutzen
sollte, ist denke ich einigermaßen bekannt.

Also statt sowas

```Java
String output = "Das ist "+username+"! Unser neuer Mitarbeiter. Er hat am "+startDate+" bei uns angefangen!";
```

lieber sowas

```Java
String output = new StringBuilder("Das ist ")
                        .append(username)
                        .append("! Unser neuer Mitarbeiter. Er hat am ")  
                        .append(startDate)
                        .append("bei uns angefangen!").toString();
```

**Aber...** ein StringBuilder kann auch mit einer bestimmten Länge initiatlisiert werden. 
Laut Joshua Bloch (siehe Effective Java) bringt das nochmal einen x2 Faktor an Performance. 
Der String Builder ohne feste Längenangabe ist ca x5 performanter als die normale String Verkettung. 
Wenn ein StringBuilder mit einer passenden Länge initialisiert wird, kann das bis zu einer 7x Performance Verbesserung führen.