import json
from pymongo import MongoClient

DATABASE_NAME = "EpubDatabase"
client = MongoClient()


# Database connexion
def connect():
    global client
    client = MongoClient('localhost', 27017);


# Database disconnexion
def disconnect():
    global client
    client.close()


# Add Epub in the collection Epub of the EpubDatabase database
def add_epub(stringJSON):
    connect()
    db = client[DATABASE_NAME]
    d = json.loads(stringJSON)
    db.Epub.insert_one(d)
    disconnect()


# Remove all the Epub of the collection Epub of the EpubDatabase database
def remove_all_epub():
    connect()
    db = client[DATABASE_NAME]
    db.Epub.remove({})
    disconnect()
