import os
from epub_to_json import Parser
from mongo_manager import add_epub, remove_all_epub


def store_epubs(epub_directory):
    # get manager content
    manager = os.path.join(epub_directory, '.manager')
    # if file doesn't exist, create it
    if not os.path.exists(manager):
        open(manager, 'x').close()
    # read lines
    with open(manager, 'r') as f:
        manager_content = f.readlines()
    # for each epub in directory
    epub_list = os.listdir(epub_directory)
    for file in epub_list:
        # if file not treated
        if file + '\n' not in manager_content:
            # if epub
            if file.endswith(".epub"):
                namefile = os.path.join(epub_directory, file)
                print("Parsing " + file)
                # extract json
                parser = Parser(namefile)
                json = parser.epub_to_json()
                print("Parsing done")
                # add to database
                add_epub(json)
                # add to manager content
                with open(manager, 'a') as f:
                    f.write(file + '\n')
                    print(file + " done.")


remove_all_epub()
store_epubs(os.path.join(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)), "res/epub"))
