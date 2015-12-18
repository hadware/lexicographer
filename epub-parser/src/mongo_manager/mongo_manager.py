import json
from pymongo import MongoClient

DATABASE_NAME = "epub"
client = MongoClient()


# Database connexion
def connect():
    global client
    client = MongoClient('192.168.1.2', 27017)


# Database disconnexion
def disconnect():
    global client
    client.close()


# Add Epub in the collection Epub of the EpubDatabase database
def add_epub(stringJSON):
    connect()
    db = client[DATABASE_NAME]
    d = json.loads(stringJSON)
    db.books.insert_one(d)
    disconnect()


# Remove all the Epub of the collection Epub of the EpubDatabase database
def remove_all_epub():
    connect()
    db = client[DATABASE_NAME]
    db.books.remove({})
    disconnect()
