import sys
from epub import open_epub
import simplejson as json
from bs4 import BeautifulSoup, Tag


class SimpleChapter(object):
    def __init__(self, name, text):
        self.name = name
        self.text = text


class Parser(object):
    def __init__(self, epub_path):
        self.epub_file = open_epub(epub_path, 'r')

        # current item used for navigation
        self.current_item = None
        # soup for the current item
        self.item_data_soup = None

    def _get_metadata_(self, metadata):
        dict = {}
        # get metadata
        dict['titles'] = [x for x in metadata.titles[0] if x]
        dict['creators'] = [x for x in metadata.creators[0] if x]
        dict['subjects'] = [x for x in metadata.subjects if x]
        dict['identifiers'] = [x for x in metadata.identifiers[0] if x]
        dict['dates'] = [x for x in metadata.dates[0] if x]
        dict['right'] = metadata.right
        # return filled dict
        return dict

    def _get_text_chapter_(self, current_tag, next_tag=None, first_item=False):
        if first_item:
            chapter_text = current_tag.get_text()
        else:
            chapter_text = ''

        for elem in current_tag.next_siblings:
            # if next tag
            if next_tag is not None and isinstance(elem, Tag) and elem == next_tag:
                break
            # else, append text
            elif isinstance(elem, Tag):
                text = elem.get_text()
                # if end of ebook
                if "Project Gutenberg" in text:
                    break
                else:
                    chapter_text += text

        # sanitize text
        chapter_text = chapter_text.replace('\n', ' ').replace('*', '')
        chapter_text = chapter_text.strip()

        return chapter_text

    def _switch_item_(self, item):
        # if new file or first read
        if self.current_item != item or self.item_data_soup is None:
            # we change the current item
            self.current_item = item
            # we read the file
            self.item_data_soup = BeautifulSoup(self.epub_file.read_item(item), 'lxml')

    def _iterate_chapter_(self, chapters, current_nav, next_nav):
        # get chapter name
        chapter_name = current_nav.labels[0][0]

        # get chapter id & file
        split_src = current_nav.src.rsplit('#', 1)
        item = self.epub_file.get_item_by_href(split_src[0])

        self._switch_item_(item)

        # get tag by id
        current_tag = self.item_data_soup.find(id=split_src[1])

        # determine which tag is next
        if current_nav.nav_point:
            direct_next = current_nav.nav_point[0]
        else:
            if next_nav is not None:
                direct_next = next_nav
            else:
                direct_next = None

        if direct_next is not None:
            next_split = direct_next.src.rsplit('#', 1)
            # if next is on same file
            if split_src[0] == next_split[0]:
                next_tag = self.item_data_soup.find(id=next_split[1])
                chapter_text = self._get_text_chapter_(current_tag, next_tag)
            else:
                # get text remaining on current page
                chapter_text = self._get_text_chapter_(current_tag)

                # get next item
                item = self.epub_file.get_item_by_href(next_split[0])
                self._switch_item_(item)

                current_tag = self.item_data_soup.body.contents[0]
                next_tag = self.item_data_soup.find(id=next_split[1])

                chapter_text += self._get_text_chapter_(current_tag, next_tag, True)
        else:
            chapter_text = self._get_text_chapter_(current_tag)

        # add chapter to array if not empty
        if chapter_text != '' and "CONTENT" not in chapter_name.upper() and "CHAPTERS" not in chapter_name.upper():
            chapters.append(SimpleChapter(chapter_name, chapter_text).__dict__)

        # if nav point has subchild
        if current_nav.nav_point:
            it = iter(current_nav.nav_point)

            current_nav = next(it)
            for child in it:
                self._iterate_chapter_(chapters, current_nav, child)
                current_nav = child

            self._iterate_chapter_(chapters, current_nav, next_nav)

    def epub_to_json(self):
        epub = {}
        chapters = []

        it = iter(self.epub_file.toc.nav_map.nav_point)
        current_nav = next(it)
        for next_nav in it:
            self._iterate_chapter_(chapters, current_nav, next_nav)
            current_nav = next_nav
        self._iterate_chapter_(chapters, current_nav, None)

        # assemble parts
        epub['metadatas'] = self._get_metadata_(self.epub_file.opf.metadata)
        epub['chapters'] = chapters

        # create json object
        json_obj = json.dumps(epub, separators=(',', ':'), ensure_ascii=False)

        self.epub_file.close()

        return json_obj


if __name__ == '__main__':
    # need one argument
    parser = Parser(sys.argv[1])
    parser.epub_to_json()
