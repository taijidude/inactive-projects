import unittest
from persistence import DataHandler
from model import Task, TrackedSession
from datetime import datetime
from output import Column, OutputUtils, TaskTableConfig
from model import ReportRow

class TestTask(unittest.TestCase):
    
    def setUp(self):
        self.task = Task(name="testtask")
        
    #Testet ob der Name der erstellten Aufgabe den Erwartungen entspricht.
    def testCreation(self):
        self.assertEqual(self.task.name, "testtask")

    #Testet, ob nach dem Aufruf der start Methode, die CurrentSession gesetzt ist
    #und ob das isRunning-Flag der CurrentSession true ist
    def testStart(self):
        task = self.task
        task.start()
        currentSession = task.currentSession
        self.assertIsNotNone(currentSession)
        self.assertIsNone(currentSession.sessionStop)
        self.assertTrue(currentSession.isRunning())

    #Testet ob nach dem Aufruf der Methode Stop
    #ob die Liste der getrackten Sessions um ein 
    #Element ergänzt wurde
    def testStop(self):
        task = self.task
        task.start()
        task.stop()
        self.assertTrue(len(task.trackedSessions) == 1)

    # Der Name einer Task ist eindeutig. Zwei Tasks mit dem selben Namen sind gleich
    # Der Test überprüft das. 
    def testCompareTasks(self):
        t1 = Task(name="t1")
        t2 = Task(name="t1")
        self.assertFalse(t1 == None)
        self.assertTrue(t1 == t2)

class TestTrackedSession(unittest.TestCase):
    def setUp(self):
        self.trackedSession = TrackedSession()

    def testStartSet(self):
        self.assertTrue(self.trackedSession.sessionStart < datetime.now())
        self.assertTrue(self.trackedSession.isRunning())

    def testStop(self):
        tsession = self.trackedSession
        tsession.stop()
        sessionStopTime = tsession.sessionStop.replace(microsecond=0) 
        nowTime = datetime.now().replace(microsecond=0)
        self.assertTrue(sessionStopTime == nowTime)
        
    def testGetDuration(self):
        tsession = self.trackedSession 
        self.assertIsNone(tsession.getDuration())
        tsession.stop()
        self.assertIsNotNone(tsession.getDuration())

class TestDataHandler(unittest.TestCase):
    DataHandler.initDatabase('test-database.db')
    
    def createTask(self):
        self.t1 = Task(name='testtask1')
        self.t1.start()
        self.dataHandler.create(self.t1)

    def checkAllTasks(self, amount, allTasks):
        self.assertIsNotNone(allTasks)
        self.assertTrue(len(allTasks) == amount)

    def setUp(self):
        self.dataHandler = DataHandler()
        self.dataHandler.clearData()
    
    #Legt eine Task an
    #Testet ob sich die angelegte Task auslesen lässt
    def testCreateTask(self):
        self.createTask()
        allTasks = self.dataHandler.getAllTasks()
        self.checkAllTasks(1, allTasks)
        self.assertTrue(self.t1 == allTasks[0])

    #Legt eine Task an
    #Testet ob sich die angelegte Task wieder löschen lässt
    def testDeleteTask(self): 
        self.createTask()
        allTasks = self.dataHandler.getAllTasks()
        self.checkAllTasks(1, allTasks)
        self.dataHandler.delete(allTasks[0].id)
        allTasks = self.dataHandler.getAllTasks()
        self.checkAllTasks(0, allTasks)

    def testDeleteWithWrongId(self):
        self.dataHandler.delete(999)

    #Legt eine Task an
    #Testet ob eine aktive Session angelegt wird
    def testGetCurrentSession(self):
        self.createTask()
        reportRow = self.dataHandler.getCurrentSession()
        self.assertIsNotNone(reportRow)
        self.assertEqual(self.t1.name, reportRow.name)

class TestOutput(unittest.TestCase):
    
    def setUp(self):
        self.outputUtils = OutputUtils()

    # Testet ob die Logik zur Berechnung der Spaltenbreite 
    # einen Wert zurück gibt und dieser Wert der Länge 
    # des längsten Strings entspricht.
    def testGetColumnWidth(self):
        config = TaskTableConfig()
        longestTaskName = 'testtask30'
        t1 = Task(name='testtask1')
        t2 = Task(name='testtask2')
        t3 = Task(name=longestTaskName)
        allTasks = [t1, t2, t3]
        config.calculateColumWidth(allTasks)
        self.assertEqual(len(longestTaskName),config.nameColumn.length)

    def testGetColumnWidthId(self):
        t1 = Task(name='Task1')
        t1.id = 1 
        config = TaskTableConfig()
        config.calculateColumWidth([t1])
        calculatedLength = config.idColumn.length
        self.assertEqual(2, calculatedLength)

    # Sollte testen ob die Überschrift der übergebenen Zeile 
    # korrekt mit Platzhaltern aufgefüllt wird
    def testFillColumn(self):
        column = Column('Name', 4)
        column.length = 60
        self.assertEqual(60, len(column.fillHeader('+')))
    # Sollte den Output der Methode getTasksTable testen. 
    # Das ist eine neue Variante um die Tasks zu printen.
    def testGetTaskTable(self):
        t1 = Task(name='1234')
        t1.id = 1
        t2 = Task(name='5678')
        t2.id = 200
        tasks = [t1, t2]    
        print(self.outputUtils.getTaskTable(tasks)) 

if __name__ == '__main__':
    unittest.main()
