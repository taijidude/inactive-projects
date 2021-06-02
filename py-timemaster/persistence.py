import argparse
import sqlite3
from datetime import datetime
from peewee import Proxy, Model, IntegerField, CharField, IntegerField, DateTimeField, ForeignKeyField, SqliteDatabase, PrimaryKeyField, SmallIntegerField
from model import Task, TrackedSession, ReportRow
from output import OutputUtils

out = OutputUtils()
db = Proxy()   

class BaseModel(Model):    
    class Meta:
        database = db

class Task_e(BaseModel):
    id = PrimaryKeyField(null=False)
    name = CharField()
    
class TrackedSession_e(BaseModel): 
    id = PrimaryKeyField(null=False)
    task = ForeignKeyField(Task_e, backref='trackedSessions')
    sessionStart = DateTimeField() 
    sessionStop = DateTimeField(null=True)
    currentSession = SmallIntegerField(default=0)

class DataHandler(): 

    databaseFileName = ''

    #Das gefällt mir nicht... 
    @staticmethod
    def initDatabase(dbname='database.db'):
        db.initialize(SqliteDatabase(dbname))
        DataHandler.databaseFileName = dbname

    def create(self, dto):
        if isinstance(dto, Task):
            task = Task_e(name=dto.name)
            task.save()
            if dto.currentSession is not None: 
                self.saveTrackedSession(dto.currentSession, task, True)
            for session in dto.trackedSessions:
                self.saveTrackedSession(session, task, False)
    
    def delete(self, id):
        Task_e.delete().where(Task_e.id == id).execute()

    def saveTrackedSession(self, session, task, isCurrentSession):
        tsession=TrackedSession_e(task=task,sessionStart=session.sessionStart, sessionStop=session.sessionStop, currentSession = int(isCurrentSession))
        tsession.save()

    def startSession(self, id, trackedSession):
        self.stopCurrentSession()
        taskToStart = Task_e.get(Task_e.id==id)
        self.saveTrackedSession(trackedSession, taskToStart, True)

    def stopCurrentSession(self):
        stopTime = datetime.now()
        update = TrackedSession_e.update({TrackedSession_e.sessionStop:stopTime}).where(TrackedSession_e.currentSession==1)
        update.execute()
        update = TrackedSession_e.update({TrackedSession_e.currentSession:0}).where(TrackedSession_e.currentSession==1)
        update.execute()
        print('Die aktuelle Session wird beendet')

    def getCurrentSession(self):
        currentSession = TrackedSession_e.get(TrackedSession_e.currentSession == 1)
        currentTask = currentSession.task
        return ReportRow(currentSession.id, out.getDate(currentSession.sessionStart),currentTask.name, currentSession.sessionStart, ' - ')

    def getCurrentTask(self):
        currentSession = TrackedSession_e.get(TrackedSession_e.currentSession == 1)
        return currentSession.task

    def getAllTasks(self):
        tasks = Task_e.select()
        if tasks is None: 
            return []

        return [(Task(name=task_e.name,id=task_e.id) for task_e in tasks)]

    def listByDay(self):
        result = []
        for task_e in Task_e.select():
            sessions = task_e.trackedSessions
            for session in sessions: 
                sessionStartDay = out.getDate(session.sessionStart)
                result.append(ReportRow(session.id, sessionStartDay, task_e.name, out.getTime(session.sessionStart), out.getTime(session.sessionStop)))
        return result

    def clearData(self):
        self.executeOnAllTables('delete from ')
        
    def clearSchema(self):
        self.executeOnAllTables('drop table ')
    
    def executeOnAllTables(self, query):
        tables = ['Task_e', 'TrackedSession_e']	
        conn = sqlite3.connect(DataHandler.databaseFileName)	
        dbcursor = conn.cursor()
        for table in tables:
            dbcursor.execute(query+table)
        conn.commit()
        conn.close()

    def createSchema(self):
        db.create_tables([Task_e, TrackedSession_e])    