from modules import clientCatalogus
from modules import authorize

token = authorize.authorize()

# Add exhibitions
clientCatalogus.postExhibition(token,"Leanardo Da Vinvi Exhibition", "Renaissance", "2022-03-10", "2022-03-10")
clientCatalogus.postExhibition(token,"René Magritte Exhibition", "Surrealisme", "2022-03-10", "2022-03-10")

# Get exhibitions
clientCatalogus.getExhibitions()

# Add paintings
clientCatalogus.postPainting(token, "Ceci n'est pas une pipe", "René Magritte", "Totally not a pipe", "Surrealisme", "René Magritte Exhibition", "45A")
clientCatalogus.postPainting(token, "The Son of Man", "René Magritte", "Apple being thrown at a man", "Surrealisme", "René Magritte Exhibition", "45B")
clientCatalogus.postPainting(token, "Golconda", "René Magritte", "Raining men", "Surrealisme", "René Magritte Exhibition", "12A")

clientCatalogus.postPainting(token, "Mona Lisa", "Leonardo Da Vinci", "Mid", "Renaissance", "Leanardo Da Vinvi Exhibition", "5c")
clientCatalogus.postPainting(token, "Last Supper", "Leonardo Da Vinci", "Last Supper", "Renaissance", "Leanardo Da Vinvi Exhibition", "5D")

# Get paintings
clientCatalogus.getPaintings()
