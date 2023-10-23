# Keycloak authorization
token = authorize()

# Add ticket
# return 201 created on success with the uri location of the ticket
# return 500 internal server error with message 'This date is not available for booking online, please contact the Museum for this booking.' if tickets for this date are not yet available
r = postTicket(token, "5", "2022-03-10", "testemail", True)

#Get ticket status
# return 200 ok with ticket data if it exists
# return 404 not found if ticket does not exist
getTicket(r, token)

r = postTicket(token, "5", "2020-03-10", "testemail", True)
getTicket(r, token)