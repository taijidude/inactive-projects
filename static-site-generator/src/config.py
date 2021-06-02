#Hier werden Konstanten gesammelt
timeStampPattern = '%d.%m.%Y %H:%M'
#Es sollte hier Werte für Default in und Default out geben
defaultIn = './in/'
defaultOut = './out/'
defaultTemplateFolder = './templates/'

def validateDefaultNotNone(name,value, validator=None):
    if value is None: 
        raise Exception(message=name+' darf nicht None sein!')
             
validateDefaultNotNone('defaultIn', defaultIn)
validateDefaultNotNone('defaultOut', defaultOut)
validateDefaultNotNone('defaultTemplateFolder', defaultTemplateFolder)
validateDefaultNotNone('timeStampPattern', timeStampPattern)

