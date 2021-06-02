from model import Task, ReportRow

#Hier sollte die tatsächliche Länge für jede Spalte gesammelt werden
#Hier sollten die Werte dynamisch festgelegt werden
class Column(): 

    def __init__(self, title, minLength): 
        self.title = title
        self.minLength = minLength
        self._length = minLength

    def fillHeader(self, toFill): 
        return self.title+((self.length-len(self.title))*toFill)

    def fillValue(self, toFill, value):
        return value+((self.length-len(value))*toFill)

    @property
    def length(self):
        return self._length

    @length.setter
    def length(self, length):
        if length > self.minLength: 
            self.length = self.minLength
        self._length = length

class BaseTableConfig(object):

    def __init__(self):
        self.header = None
        self.rows = []
        self.footer = None
        self.columns = []

    def __str__(self):
        result = "\n"+self.header+"\n"
        for row in self.rows:
            result += row+"\n"
        result += self.footer
        return result
    
    def calculateHeader(self):
        if len(self.columns) == 0: return
        columHeader = []
        for column in self.columns:
            columHeader.append(column.fillHeader('-'))
        self.header = '|-' +('-|-'.join(columHeader)) + '-|'
        self.footer = '|-'+((len(self.header)-4) * '-')+'-|'

    def getMaxLength(self, column, currentLength): 
        if column.length is None or currentLength > column.length:
            column.length = currentLength

class TaskTableConfig(BaseTableConfig):

    def __init__(self):
        super().__init__()
        self.idColumn = Column('Id', 2)
        self.nameColumn = Column('Name', 4)
        self.columns = [self.idColumn, self.nameColumn]

    def calculateColumWidth(self, tasks):
        for task in tasks:
            self.getMaxLength(self.idColumn, len(str(task.id)))
            self.getMaxLength(self.nameColumn, len(task.name))

    def calculateRows(self, tasks):
        for task in tasks:
            self.rows.append('| '+self.idColumn.fillValue(' ', str(task.id))+' | '+  self.nameColumn.fillValue(' ', task.name) +' |')

class TaskByDayTableConfig(BaseTableConfig):

    def __init__(self):
        super().__init__()
        self.idColumn = Column('Id', 2)
        self.dateColumn = Column('Tag', 6)
        self.nameColumn = Column('Name', 4)
        self.startColumn = Column('Start', 5)
        self.stopColumn = Column('Stop', 5)
        self.columns = [self.idColumn, self.dateColumn, self.nameColumn, self.startColumn, self.stopColumn]

    def calculateColumWidth(self, tasksByDay):
        for row in tasksByDay:
            self.getMaxLength(self.idColumn, len(str(row.id)))
            self.getMaxLength(self.dateColumn, len(row.day))
            self.getMaxLength(self.nameColumn, len(row.name))
            self.getMaxLength(self.startColumn,len(row.start))
            self.getMaxLength(self.stopColumn, len(row.stop))

    def calculateRows(self, taskByDay):
        for session in taskByDay:
            row = '| '
            row += self.idColumn.fillValue(' ', str(session.id)) + ' | '
            row += self.dateColumn.fillValue(' ', session.day) + ' | '
            row += self.nameColumn.fillValue(' ', session.name) + ' | '
            row += self.startColumn.fillValue(' ', session.start) + ' | '
            row += self.stopColumn.fillValue(' ', session.stop) + ' |'
            self.rows.append(row)

class OutputUtils():
       
    def getTime(self, datum): 
        if datum is None:return '-'
        return datum.strftime("%H:%M")

    def getDate(self, datum):
        if datum is None:return '-'
        return datum.strftime("%d-%m-%Y")

    def getTaskTable(self,tasks):
        if len(tasks) == 0: 
            print("Keine Elemente!")
            return       
        if len(tasks)!=0:
            tasks.sort(key=lambda task : task.id)
            config = TaskTableConfig()
            config.calculateColumWidth(tasks)
            config.calculateHeader()
            config.calculateRows(tasks)
            print(config)

    def getTasksByDayTable(self, tasksByDay):
        if len(tasksByDay) == 0:
            print('Keine Elemente')
            return
        if len(tasksByDay)!=0:
            tasksByDay.sort(key=lambda task : (task.day, task.start))
            config = TaskByDayTableConfig()
            config.calculateColumWidth(tasksByDay)
            config.calculateHeader()
            config.calculateRows(tasksByDay)
            print(config)