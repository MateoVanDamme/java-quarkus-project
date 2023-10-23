from keycloak import KeycloakOpenID

def authorize(containerMode = False):
    endpoint = "http://localhost:8180"
    if containerMode:
        endpoint = "http://keycloak.localhost:8180"
        
    keycloak_openid = KeycloakOpenID(server_url=endpoint, client_id="quarkus-app", realm_name="quarkus", client_secret_key="secret")
    token = keycloak_openid.token("alice", "alice")
    print(f"Token: {token['access_token']}")
    return token['access_token']