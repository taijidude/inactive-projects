import os, mistune, shutil, datetime
import stringUtils as stu
import fileUtils as fu
import HtmlGenerator as gen
from datetime import datetime
from pathlib import Path
from string import Template

class CustomRenderer(mistune.Renderer):
   CONTENT_ITEM_TMPL = """
       <div class="item">
            $toReplace
       </div>
      """  
        
   def image(self, src, title, alt_text):
      imgTemplate = '<img src="$toReplace" />'
      imgCode = Template(imgTemplate).substitute(toReplace=src)
      return Template(CustomRenderer.CONTENT_ITEM_TMPL).substitute(toReplace=imgCode)

   def block_html(self, html):
      return Template(CustomRenderer.CONTENT_ITEM_TMPL).substitute(toReplace=html)
    
   def block_code(self, code, lang=None):
      tmpl = """
      <div id="content-code">
            $toReplace
       </div>
      """
      return Template(tmpl).substitute(toReplace=super().block_code(code, lang=lang))

if __name__ == '__main__':
   abspath = os.path.abspath(__file__)
   dname = os.path.dirname(abspath)
   os.chdir(dname)
   inDir = Path('../content') 
   inFiles = inDir.joinpath('mdfiles').glob('*.md')
   resourceFiles = inDir.joinpath('resources')
   outDir = Path('../out')
   fu.cleanDir(outDir)
   fu.copyFolder(resourceFiles, outDir.joinpath('resources'))
   tmplDir = Path('../templates')
   articleTemplate =  tmplDir.joinpath('article.html')
   pageTemplate = tmplDir.joinpath('page.html')
   indexTemplate = tmplDir.joinpath('index.html')
   gen.generateOutput(indexTemplate, pageTemplate, articleTemplate, inFiles, outDir, CustomRenderer()) 


    
