import sys
from argparse import ArgumentParser
from persistence import DataHandler
from model import Task, TrackedSession, ReportRow
from output import OutputUtils, Column


"""
Todo's:
- Peewee sollte ins Virtual Environment mit aufgenommen werden 
- Es sollte nur eine activeSession geben
- Ich will für die activeSession die Startzeit sehen

* Testen: 
2. Auf dieser neuen Task kann das Tracking das gestartet werden
3. Auf der neuen Task kann das Tracking gestoppt werden.
4. Ein weiteres Tracking kann gestartet und gestoppt werden. 
5. Beide Sessions werden korrekt gelistet
6. Es kann eine weitere Task angelegt werden
7. Auf dieser Task kann das Tracking gestartet und gestoppt werden
8. Die getrackten Sessions für beide Tasks werden korrekt aufgelistet
* Ein Api Modul entwerfen. Cmd und UI nutzen diese Api
"""

parser = ArgumentParser(description='Timetracking for the Commandline')
parser.add_argument('-a','--activeTask', action='store_true', help='Gibt die aktuell getrackte Task aus')
parser.add_argument('-t', '--listTasks', action='store_true', help='Listet alle Tasks auf')
parser.add_argument('-s','--listSessions', action='store_true', help='Listet alle getrackten Sessions auf')
parser.add_argument('-n','--newTask', nargs=1, help='Legt eine neue Tasks an')
parser.add_argument('-d', '--deleteTask', nargs=1, type=int, help='Löscht eine Task. Die Id der Task muss als Parameter übergeben werden')
parser.add_argument('-on','--start', nargs=1, type=int, help='Startet das Tracking einer existierenden Task. Die Id der Task muss als Parameter übergeben werden')
parser.add_argument('-off', '--stop',action='store_true', help='Stopt das Tracking der aktuellen Task.' )
parser.add_argument('--schema', choices=['clear', 'create', 'reset'], help='Hilfsfunktion um die Datenbank zurück zu setzen')
parser.add_argument('-db', nargs=1, help='Über diese Option kann eine alternative Datenbank angegeben werden')
args = parser.parse_args()
print('Timemaster...')

DataHandler.initDatabase()
datahandler = DataHandler()
out = OutputUtils()

if args.db:
    altDbName = args.db[0]
    DataHandler.initDatabase(altDbName)
    
if args.activeTask:
    print(out.getTaskTable([datahandler.getCurrentTask()]))

if args.newTask: 
    print('Die neue Task '+args.newTask[0]+' wird angelegt und gestartet!')
    name = args.newTask[0]
    task = Task(name)
    task.start()
    datahandler.create(task)
 
if args.deleteTask:

    datahandler.delete(args.deleteTask[0])

if args.listTasks: 
    allTasks = datahandler.getAllTasks()
    print(out.getTaskTable(allTasks))

if args.listSessions:
    tasksByDay = datahandler.listByDay()
    out.getTasksByDayTable(tasksByDay)

if args.start:
    newSession = TrackedSession()
    id = args.start[0]
    datahandler.startSession(id, newSession)

if args.stop: 
    datahandler.stopCurrentSession()

if args.schema: 
    command = args.schema
    if command == 'clear':
        print('Schema wird gelöscht...')
        datahandler.clearSchema()

    elif command == 'create':
        print('Schema wird erzeugt...')
        datahandler.createSchema()

    elif command == 'reset':
        print('Schema wird gelöscht und erzeugt...')
        datahandler.clearSchema()
        datahandler.createSchema()

print('...Fertig!')
