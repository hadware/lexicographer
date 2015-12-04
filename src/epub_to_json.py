# -*- coding: utf-8 -*-

import sys
from epub import open_epub
import simplejson as json
from bs4 import BeautifulSoup


class SimpleChapter(object):

    def __init__(self, name, text):
        self.name = name
        self.text = text


def epub_to_json(epub_path):

    # open epub file
    epub_file = open_epub(epub_path, 'r')

    epub = {}
    metadata = {}
    chapters = []

    # get metadata
    metadata['titles'] = [x for x in epub_file.opf.metadata.titles[0] if x]
    metadata['creators'] = [x for x in epub_file.opf.metadata.creators[0] if x]
    metadata['subjects'] = [x for x in epub_file.opf.metadata.subjects if x]
    metadata['identifiers'] = [x for x in epub_file.opf.metadata.identifiers[0] if x]
    metadata['dates'] = [x for x in epub_file.opf.metadata.dates[0] if x]
    metadata['right'] = epub_file.opf.metadata.right

    # current item used for navigation
    current_item = None
    # soup for the current item
    item_data_soup = None
    for point in epub_file.toc.nav_map.nav_point:
        # get chapter name
        chapter_name = point.labels[0][0]
        # get chapter id & file
        file = point.src.rsplit('#', 1)
        item = epub_file.get_item_by_href(file[0])

        # no current item
        if current_item is None:
            current_item = item

        # if new file or first read
        if current_item != item or item_data_soup is None:
            # we read the file
            item_data_soup = BeautifulSoup(epub_file.read_item(item), 'lxml')

        # get all associated text
        chapter_soup = item_data_soup.find(id=file[1]).findNextSiblings('p')

        # get all text for current chapter
        chapter_text = ''
        for p in chapter_soup:
            chapter_text += p.get_text()

        # add chapter to array
        chapters.append(SimpleChapter(chapter_name, chapter_text).__dict__)

    # assemble parts
    epub['metadatas'] = metadata
    epub['chapters'] = chapters

    # create json object
    json_obj = json.dumps(epub, separators=(',', ':'), ensure_ascii=False)

    epub_file.close()

    return json_obj

if __name__ == '__main__':
    # need one argument
    if not sys.argv[1]:
        print("usage: extractEpub <epub path>")
        exit(1)

    epub_to_json(sys.argv[1])
