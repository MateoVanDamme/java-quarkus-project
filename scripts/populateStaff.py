from modules import clientStaff
from modules import authorize

token = authorize.authorize()

# Add Staffmembers
clientStaff.postStaff(token, "Jan", "2021-03-10", "Guard", "2022-21-12")
clientStaff.postStaff(token, "Piet", "2021-03-10", "Guard", "2022-21-12")
clientStaff.postStaff(token, "Klaas", "2021-03-10", "Guard", "2022-21-12")

clientStaff.postStaff(token, "Joeri", "2020-05-21", "Cleaning", "2022-21-12")
clientStaff.postStaff(token, "Jeroen", "2020-05-21", "Cleaning", "2022-21-12")
clientStaff.postStaff(token, "Luc", "2020-05-21", "Cleaning", "2022-21-12")

clientStaff.postStaff(token, "Lionel", "2009-03-10", "Guide", "2022-21-12")
clientStaff.postStaff(token, "Ronaldo", "2009-03-10", "Guide", "2022-21-12")
clientStaff.postStaff(token, "Messi", "2009-03-10", "Guide", "2022-21-12")

# Get paintings
clientStaff.getRatings()


