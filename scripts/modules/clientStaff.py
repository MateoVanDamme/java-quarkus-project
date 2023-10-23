import json
import requests
from modules.tools import print_dict

def postStaff(token:str, name: str, accepted:str, t:str, planned:str, containerMode = False):
    endpoint = "http://localhost:8080/staff"
    if containerMode:
        endpoint = "http://staff.localhost/staff"
    headers = {
    'Authorization': f"Bearer {token}"
    }
    data = {
    "name": name,
    "accepted": accepted,
    "type": t,
    "planned": planned
    }
    r = requests.post(endpoint, headers=headers, json=data)
    return r

def getStaff(containerMode = False):
    endpoint = 'http://localhost:8080/staff'
    if containerMode:
        endpoint = 'http://staff.localhost/staff'

    print("Sending get")
    r = requests.get(endpoint)
    try:
        response = json.loads(r.text)
        print_dict(response)
    except:
        print("Error occured")
        print(f"{r.status_code} -> {r.text}")
