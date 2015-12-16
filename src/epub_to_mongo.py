import os
from epub_to_json import epub_to_json
from mongo_manager import add_epub, remove_all_epub


def store_epubs(epub_directory):
    epub_list = os.listdir(epub_directory)
    for file in epub_list:
        if file.endswith(".epub"):
            nameFile = os.path.join(epub_directory, file)
            print(nameFile)
            jsonObject = epub_to_json(nameFile)
            add_epub(jsonObject)


remove_all_epub()
store_epubs(os.path.join(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)), "res/epub"))
