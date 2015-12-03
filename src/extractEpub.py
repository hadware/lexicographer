# -*- coding: utf-8 -*-

import epub
import simplejson as json
from bs4 import BeautifulSoup

book = epub.open_epub("../res/RobinsonCrusoe.epub", 'r')

data_struct = {}

# current item used for navigation
current_item = None
# soup for the current item
item_data_soup = None
for point in book.toc.nav_map.nav_point:
    # get chapter name
    name_chapter = point.labels[0][0]
    # get chapter id & file
    file = point.src.rsplit('#', 1)
    item = book.get_item_by_href(file[0])

    # no current item
    if current_item is None:
        current_item = item

    # if new file or first read
    if current_item != item or item_data_soup is None:
        # we read the file
        item_data_soup = BeautifulSoup(book.read_item(item), 'lxml')

    text_chapter = item_data_soup.find(id=file[1]).findNextSiblings('p')

    # get all text for current chapter
    data_struct[name_chapter] = ''
    for p in text_chapter:
        data_struct[name_chapter] += p.get_text()

# write json file
f = open('data.json', 'w', encoding='utf8')
json.dump(data_struct, f, ensure_ascii=False)
f.close()

book.close()
