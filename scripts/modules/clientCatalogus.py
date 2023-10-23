import json
import requests
from modules.tools import print_dict

def postExhibition(token: str, name:str, description:str, startData:str, endData:str, containerMode = False):
    endpoint = "http://localhost:8080/catalogus/exhibitions"
    if containerMode:
        endpoint = "http://catalogus.localhost/catalogus/exhibitions"

    headers = {
    'Authorization': f"Bearer {token}"
    }
    print("sending post")
    data = {
        "name": name,
        "description": description,
        "startDate": startData,
        "endDate": endData
    }
    
    r = requests.post(endpoint, headers=headers, json=data)
    print(f"{r.status_code} -> {r.headers['location']}")
    return r.headers['location']

def postPainting(token: str, name:str, artist:str, description:str, style:str, exhibitionID:str, location:str, containerMode = False):
    endpoint = "http://localhost:8080/catalogus/paintings"
    if containerMode:
        endpoint = "http://catalogus.localhost/catalogus/paintings"

    headers = {
    'Authorization': f"Bearer {token}"
    }
    print("sending post")
    data = {
    "name": name,
    "artist": artist,
    "description": description,
    "style": style,
    "exhibitionID": exhibitionID,
    "location": location
    }
    r = requests.post(endpoint, headers=headers, json=data)
    print(f"{r.status_code} -> {r.headers['location']}")
    return r.headers['location']

def getExhibitions(containerMode = False):
    endpoint = 'http://localhost:8080/catalogus/exhibitions'
    if containerMode:
        endpoint = 'http://catalogus.localhost/catalogus/exhibitions'
    print(f"Sending get to {endpoint}")
    r = requests.get(endpoint)
    try:
        response = json.loads(r.text)
        print_dict(response)
    except:
        print("Error occured")
        print(r)

def getPaintings(containerMode = False):
    endpoint = 'http://localhost:8080/catalogus/paintings'
    if containerMode:
        endpoint = 'http://catalogus.localhost/catalogus/paintings'
    print(f"Sending get to {endpoint}")
    r = requests.get(endpoint)
    try:
        response = json.loads(r.text)
        print_dict(response)
    except:
        print("Error occured")
        print(r)