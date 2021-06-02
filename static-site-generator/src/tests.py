#!/usr/bin/env python3
import HtmlGenerator as gen 
from pathlib import Path
from config import timeStampPattern
import app
import os, shutil, unittest, datetime, glob
import fileUtils as fu

testPath = Path('tests')
outPath = testPath.joinpath('out')
resourcesPfad = testPath.joinpath('content')

class GeneratorTest(unittest.TestCase): 

    def setUp(self):
        os.mkdir(testPath)
        os.mkdir(outPath)
              

    def tearDown(self):
        fu.delDir(testPath)
        
    def test_createStaticPages(self): 
        contentPath = Path('../content')
        fu.copyFolder(contentPath, resourcesPfad)
        inputFiles = contentPath.joinpath('static').glob('*.md')
        gen.createStaticPages(inputFiles, outPath)
        self.assertTrue(outPath.joinpath('static').exists())


        

        #Ist Im Testpath alles wesenliche vorhanden? 
        #Wenn ja, dann sollte

if __name__ == '__main__':
    unittest.main()
