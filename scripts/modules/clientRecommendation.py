import json
import requests

def getRecommendation(token, paintingid:str, ratingScore:int, containerMode = False):
    endpoint = "http://localhost:8080/recommend/simple"
    if containerMode:
        endpoint = "http://analytics.localhost/recommend/simple"

    print("Sending get")
    headers = {
        'Authorization': f"Bearer {token}"
    }
    r = requests.get(f"{endpoint}/{paintingid}?ratingScore={ratingScore}", headers = headers)
    print(f"Error code is {r.status_code} with type {type(r.status_code)}")
    if r.status_code == 200:
        return json.loads(r.text)
    else: 
        print(f"Error occured {r.status_code} -> {r.text}")

