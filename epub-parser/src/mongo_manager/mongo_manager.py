import json
from pymongo import MongoClient, errors
import time

DATABASE_NAME = "epub"
client = MongoClient()


# Database connexion
def connect():
    global client
    client = MongoClient('mongodb://epub1-0u278hoc.cloudapp.net,epub2-a7q4vt06.cloudapp.net,epub3-k16i2rdh.cloudapp.net')

# Database disconnexion
def disconnect():
    global client
    client.close()


# Add Epub in the collection Epub of the EpubDatabase database
def add_epub(stringJSON):
    connect()
    db = client[DATABASE_NAME]
    d = json.loads(stringJSON)

    # add epub data
    try:
        epub_id = db.books.insert_one(d).inserted_id
    except Exception as err:
        try:
            print(err)
        except errors.AutoReconnect:
            print("auto 1")
            time.sleep(5)
            epub_id = db.books.insert_one(d).inserted_id

    # add author
    creator = d["metadatas"]["creators"][0]
    try:
        cursor = db.authors.find({"_id": creator})
        if cursor.count() == 0:
            db.authors.insert_one({"_id": creator, "idRef": [epub_id]})
        else:
            db.authors.update_one({"_id": creator}, {"$push": {"idRef": epub_id}})
    except Exception as err:
        try:
            print(err)
        except errors.AutoReconnect:
            print("auto 2")
            time.sleep(5)
            cursor = db.authors.find({"_id": creator})
            if cursor.count() == 0:
                db.authors.insert_one({"_id": creator, "idRef": [epub_id]})
            else:
                db.authors.update_one({"_id": creator}, {"$push": {"idRef": epub_id}})

    # add subjects
    subjects = d["metadatas"]["subjects"]
    for subject in subjects:
        try:
            cursor = db.subjects.find({"_id": subject})
            if cursor.count() == 0:
                db.subjects.insert_one({"_id": subject, "idRef": [epub_id]})
            else:
                db.subjects.update_one({"_id": subject}, {"$push": {"idRef": epub_id}})
        except Exception as err:
            try:
                print(err)
            except errors.AutoReconnect:
                print("auto 3")
                cursor = db.subjects.find({"_id": subject})
                if cursor.count() == 0:
                    db.subjects.insert_one({"_id": subject, "idRef": [epub_id]})
                else:
                    db.subjects.update_one({"_id": subject}, {"$push": {"idRef": epub_id}})

    disconnect()


# Remove all the Epub of the collection Epub of the EpubDatabase database
def remove_all_epub():
    connect()

    db = client[DATABASE_NAME]
    db.books.remove({})
    db.authors.remove({})
    db.subjects.remove({})

    disconnect()
