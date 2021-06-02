from datetime import datetime
from datetime import timedelta

class Task():
    id = -1
    name = ""
    trackedSessions = []
    currentSession = None
    def __init__(self, name, id): 
        self.name = name
        self.id = id

    def start(self):
        self.currentSession = TrackedSession()

    def stop(self):
        self.currentSession.stop()
        self.trackedSessions.append(self.currentSession)
        self.currentSession = None

    def __eq__(self, other):
        if other is None:
            return False
        return self.name == other.name

class TrackedSession():
    sessionStart = datetime.now()
    sessionStop = None

    def stop(self):
        self.sessionStop = datetime.now()

    def getDuration(self):
        if(self.sessionStop == None):
            return None
        else: 
            return self.sessionStop - self.sessionStart    

    def isRunning(self):
        return self.sessionStop == None

class ReportRow(): 
    
    def __init__(self, id, day, name, start, stop): 
        self.id = id
        self.day = day
        self.name = name
        self.start = start
        self.stop = stop
    
    def __str__(self):
        return str(self.name) + " | " + str(self.start) + " | " + str(self.stop)

class Table():
    header = None
    columns = {
            'Id':(lambda task : len(str(task.id)) ,2),
            'Name':(lambda task : len(task.name),4),
            'Start':(lambda task : len(task.start),8),
            'Stop':(lambda task : len(task.stop),8)
    }
    
    rowData = []
    def __init__(self, rowData):
        #Die Tasks dürfen nicht leer sein.
        #Die Tasks dürfen nicht 
        self.rowData.extend(rowData)


        