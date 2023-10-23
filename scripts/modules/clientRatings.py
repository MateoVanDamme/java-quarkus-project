import json
import requests
from modules.tools import print_dict

def postRating(token: str, paintingName:str, content:str, rating:int, containerMode=False):
    endpoint  = "http://localhost:8080/ratings"
    if containerMode:
        endpoint  = "http://rating.localhost/ratings"
    headers = {
    'Authorization': f"Bearer {token}"
    }
    print("sending post")
    data = {
    "content": content,
    "rating": rating
    }
    r = requests.post(f"{endpoint}/{paintingName}", headers=headers, json=data)
    if r.status_code == 201:
        print(f"{r.status_code} -> {r.headers['location']}")
        return r.headers['location']
    else:
        print(f"Error occured {r.status_code} -> {r.text}")
        return r.status_code


def getRatings(containerMode = False):
    endpoint = 'http://localhost:8080/ratings'
    if containerMode:
        endpoint = 'http://rating.localhost/ratings'
    print("Sending get")
    r = requests.get(endpoint)
    try:
        response = json.loads(r.text)
        print_dict(response)
    except:
        print("Error occured")
        print(f"{r.status_code} -> {r.text}")