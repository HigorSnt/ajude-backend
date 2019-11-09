import requests
import json

'''
Para executar o script é preciso ter a biblioteca 'requests' instalada.
Comando: pip3 install requests
'''

url = "http://localhost:8080/api"
      
json_campanhas = open('campanhas.json').read()
json_usuarios = open("usuarios.json").read()

lista_campanhas = json.loads(json_campanhas)
lista_usuarios = json.loads(json_usuarios)

# Registrando os usuarios
for user in lista_usuarios:
    try:
        r = requests.post(url+"/user", json=user)
        print("Usuario", user['email'], "cadastrado")
        print("Status code:", r.status_code)
    except:
        print("Erro ao processar requisição")
        print("Usuario: " + user['email'])
    

print()

# Logando com os usuarios
tokens = []
for user in lista_usuarios:
    try:
        login = {"email": user["email"], "password": user["password"]}
        r = requests.post(url+"/auth/login", json=login)
        
        # Pegar o token
        tokens.append(r.json()['token'])
        print("Usuario", user['email'], "logado")
    except:
        print("Erro ao processar requisição")
        print("Login: " + user['email'])

print()

#Registrando campanhas
for i in range(len(lista_campanhas)):
    # Alterna entre os usuarios que cadastram cada uma das campanhas
    auth = tokens[i % len(tokens)]
    campaign = lista_campanhas[i]
    header = {'Authorization': 'Bearer ' + auth}
        
    try:
        r = requests.post(url+"/campaign/register", json=campaign, headers=header)
        print("Campanha", campaign['shortName'], "cadastrada")

    except:
        print("Erro ao processar requisição")
        print("Campanha", campaign['shortName'])

