from modules import clientRatings
from modules import authorize

token = authorize.authorize()

# Add paintings
for i in range(3):
    clientRatings.postRating(token, "fakePaintingID1", "mooi schilderij idk", i%5)
for i in range(3):
    clientRatings.postRating(token, "fakePaintingID2", "mooi schilderij idk", i%5)
# Get paintings
clientRatings.getRatings()