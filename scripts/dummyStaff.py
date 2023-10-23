import time
import requests
import random
from authorize import authorize

# This is a dummy that mimics the staff service and posts updates to the ticket service 
    
def postTicket(token: str, date:str, amount:int):
    headers = {
    'Authorization': f"Bearer {token}"
    }
    print("Sending post")
    data = {
        "date": date,
        "amount": amount
    }
    r = requests.post(f"http://staff.localhost/tickets/availability", headers=headers, json=data)
    return r.status_code


def random_date(days:int, time_format):
    stime = time.time() #current time in seconds
    ptime = stime + random.random() * days * 60*60*24 #get random secon between now and now+days
    return time.strftime(time_format, time.localtime(ptime)) #return as format


    
# Keycloak authorization
token = authorize()

while True: 
    date = random_date(10, '%Y-%m-%d')
    amount = random.randint(-5,10) # random number between 0 and 5
    print(f"Adding {amount} extra tickets for {date}")
    r = postTicket(token, date, amount)
    print(r)
    time.sleep(random.randint(0,5)) # prevent spamming

