import argparse
import unittest
from peewee import *
from datetime import datetime
from datetime import timedelta

class TrackedSession(): 
    id = IntegerField(primary_key=True)
    sessionStart = DateTimeField(default=datetime.now) 
    sessionStop = DateTimeField()

    def stop(self):
        self.sessionStop = datetime.now

    def getDuration(self):
        return self.sessionStop - self.sessionStart

class Task():
    id = IntegerField(primary_key=True)
    name = CharField()
    trackedSessions = []
    activeSession = None
    def __init__(self, name):
        self.name = name
        self.activeSession = TrackedSession()

    def stopActiveSession(self):
        self.activeSession.stop()
        self.trackedSessions.append(self.activeSession)
        self.activeSession = None



class TestTask(unittest.TestCase):
    task = None
    def setUp(self):
        self.task = Task(name='Task testen')
    
    def testCreate(self):
        self.assertEqual('Task testen', self.task.name) 
        activeSession = self.task.activeSession
        self.assertTrue(activeSession != None)

    def testStop(self):
        self.task.stopActiveSession()
        self.assertEqual(len(self.task.trackedSessions), 1)
        trackedSession = self.task.trackedSessions[0]
        self.assertTrue(trackedSession != None)
        self.assertTrue(trackedSession.sessionStop != None)

if __name__ == '__main__':
    unittest.main()
