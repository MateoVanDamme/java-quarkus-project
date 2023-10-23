import json
import requests
from modules.tools import print_dict

def postTicket(token: str, amount:int, date:str, email:str, guided:bool, containerMode = False):
    endpoint = "http://localhost:8080/tickets/purchase"
    if containerMode:
        endpoint = "http://ticket.localhost/tickets/purchase"
    headers = {
    'Authorization': f"Bearer {token}"
    }
    data = {
    "amount": amount,
    "date": date,
    "email": email,
    "guided": guided
    }
    r = requests.post(endpoint, headers=headers, json=data)
    if(r.status_code == 500):
        return r.text
    else:
        return r.headers['location']


def getTicket(uri:str, token:str):

    if(uri == None):
        return
    headers = {
    'Authorization': f"Bearer {token}"
    }
    r = requests.get(str(uri), headers=headers)
    return r.json()['verified']

