import os
from epub_to_json import epub_to_json
from databaseManager import addEpubDB
from databaseManager import removeAllEpub


def storeEpubs(epubDirectory):
    print(epubDirectory)
    epubList = os.listdir(epubDirectory)
    for file in epubList:
        print(file)
        nameFile = os.path.join(epubDirectory, file)
        print(nameFile)
        jsonObject = epub_to_json(nameFile)
        addEpubDB(jsonObject)

removeAllEpub()
storeEpubs(os.path.join(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)), "Epubs"))