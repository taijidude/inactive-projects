import stringUtils as stu
import fileUtils as fu
import mistune  
from datetime import datetime
from pathlib import Path
from jinja2 import Template

class Article(): 
    def __init__(self, date=None, markdown='', metadata='', title=''):
        if date is None:
            self.date = datetime.now()
        else: 
            self.date = date
        self.markdown = markdown
        #Der umgewandelte Markdown Text für eine einzelne Seite
        self.pageHtml = None
        #Der umgewandelte Text für den Artikelfeed
        self.feedHtml = None
        self.title = title
        #Der komplette Inhalt der 
        self.fileContent = None
        self.fileName = None
        
    def dateString(self):
        return self.date.strftime('%d.%m.%Y') 

def readContent(mdFile): 
    fileContent = fu.readFile(mdFile).split('article-section:')
    meta = processLines(fileContent[1])
    return Article(date=meta['date'], markdown=fileContent[2], metadata=fileContent[1], title=meta['title'])

def convertToHtml(markdown, cRenderer):
    return mistune.markdown(markdown, renderer=cRenderer)
    
def fillArticleTemplate(templateFile, convertedText, article, context): 
    templateString = fu.readFile(templateFile)
    return Template(templateString).render(content=convertedText, title=article.title, info=article.date, context=context, fileName=article.fileName)

def fillPageTemplate(templateFile, pageHtml): 
    templateString = fu.readFile(templateFile)
    return Template(templateString).render(content=pageHtml)

#Liest die Zeilen der Metadata Section eines Artikels ein
#und baut mit den Werten ein Dictonary
def processLines(text):
    lines = stu.filterLines(text, lambda line : '=' in line)
    lines = [line.strip() for line in lines]
    return {item.split('=')[0]:item.split('=')[1] for item in lines}

def generateOutput(indexTemplate, pageTemplate, articleTemplate, inputFiles, outDir, cRenderer): 
    completeArticles = []
    for file in inputFiles: 
        article = readContent(file)
        article.fileName = getArticleFile(file)
        convertedText = convertToHtml(article.markdown, cRenderer)        
        article.pageHtml = fillArticleTemplate(articleTemplate, convertedText, article,"page")
        article.feedHtml = fillArticleTemplate(articleTemplate, convertedText, article,"feed")
        article.fileContent = fillPageTemplate(pageTemplate, article.pageHtml)
        fu.writeToFile(article.fileName, article.fileContent)    
        completeArticles.append(article)
    writeArticleFeed(completeArticles, outDir)
    createStaticPages(outDir, cRenderer)

def getArticleFile(mdFile): 
    return Path("../out").joinpath(Path(mdFile.name).stem + ".html")

def writeArticleFeed(articles, target):
        articles = sorted(articles, key=lambda art : art.date, reverse=True)
        indexTemplate = fu.readFile('../templates/index.html')         
        fu.writeToFile(target.joinpath('index.html'), Template(indexTemplate).render(articlesToPrint=articles))

def createStaticPages(outDir, cRenderer):
    print(Path('../templates/staticpage.html').exists())
    staticPageTemplate = '../templates/staticpage.html'
    inputFiles = Path('../content/static').glob('*.md') 
    staticPathOut = outDir.joinpath('static')
    if staticPathOut.exists():
        fu.delDir(staticPathOut)
    fu.mkdir(staticPathOut)
    for file in inputFiles:
        staticPageMd = fu.readFile(file)
        fileName = Path("../out/static").joinpath(Path(file.name).stem + ".html")  
        convertedText = convertToHtml(staticPageMd, cRenderer)        
        completeHtml = fillPageTemplate(staticPageTemplate, convertedText)
        #print(completeHtml)
        #fu.writeToFile(fileName, completeHtml)    