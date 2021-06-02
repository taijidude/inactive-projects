article-section: 
title=Feststellen ob ein bestimmtes Item in einem Stream enthalten ist
date=06.07.2019
article-section:
Um festzustellen, ob ein Element in einem Stream enthalten ist, war mein erster Ansatz folgender: 

```Java
return customers
            .stream()
            .filter(customer -> customer.getName().equals("Möller"))
            .findFirst()
            .isPresent();
```

Durch eine Sonar Qube Analyse bin ich auf eine bessere Variante aufmerksam geworden:

```Java
    return customers
        .stream()
        .anyMatch(
            customer -> customer.getName().equals("Möller")
        );
```
