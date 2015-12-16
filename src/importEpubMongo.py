# -*- coding: utf-8 -*-

import os
from epub_to_json import epub_to_json
from databaseManager import addEpubDB
from databaseManager import removeAllEpub


def store_epubs(epub_directory):
    epub_list = os.listdir(epub_directory)
    for file in epub_list:
        if file.endswith(".epub"):
            nameFile = os.path.join(epub_directory, file)
            print(nameFile)
            jsonObject = epub_to_json(nameFile)
        #addEpubDB(jsonObject)


#removeAllEpub()
store_epubs(os.path.join(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)), "res/epub"))
