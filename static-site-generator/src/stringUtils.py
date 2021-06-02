#Erstellt eine Liste von Zeilen, die den FilterKriterien entsprechen
def filterLines(text, filterCriteria):
      return [line for line in text.split('\n') if filterCriteria(line)]