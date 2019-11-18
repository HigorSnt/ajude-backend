# coding: utf-8
import requests
import json

'''
Para executar o script é preciso ter a biblioteca 'requests' instalada.
Comando: pip3 install requests
'''

url = "http://localhost:8080/api"
      
json_campanhas = open('campanhas.json').read()
json_usuarios = open("usuarios.json").read()
json_comentarios = open("comentarios.json").read()
json_respostas = open("respostas.json").read()

lista_campanhas = json.loads(json_campanhas)
lista_usuarios = json.loads(json_usuarios)
lista_comentarios = json.loads(json_comentarios)
lista_respostas = json.loads(json_respostas)

# Registrando os usuarios
for user in lista_usuarios:
    try:
        r = requests.post(url+"/user", json=user)
        print("Usuario", user['email'], "cadastrado")
        print("Status code:", r.status_code)
    except:
        print(r.raise_for_status())
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
        print(r.raise_for_status())
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
        r = requests.post(url + "/campaign/register", json=campaign, headers=header)
        print("Campanha", campaign['shortName'], "cadastrada")
    except:
        print(r.raise_for_status())
        print("Erro ao processar requisição")
        print("Campanha", campaign['shortName'])

print()

comments = []
#Comentando campanhas
for i in range(len(lista_comentarios)):
    auth = tokens[i % len(tokens)]
    campaign = lista_campanhas[i % len(lista_campanhas)]['urlIdentifier']
    comment = lista_comentarios[i]
    header = {'Authorization': 'Bearer ' + auth}

    try:
        r = requests.post(url + f'/campaign/{campaign}/comment/', json=comment, headers=header)
        comments.append(r.json())
        print("Comentario", comment['comment'], "cadastrado")
    except:
        print(r.raise_for_status())
        print("Erro ao processar requisição")
        print("Comentario", comment['comment'])

print()
# Respondendo comentários
for i in range(len(lista_respostas)):
    auth = tokens[i % len(tokens)]
    campaign = lista_campanhas[i % len(lista_campanhas)]['urlIdentifier']
    comment_id = comments[i % len(comments)]['id']
    reply = lista_respostas[i]
    header = {'Authorization': 'Bearer ' + auth}

    try:
        r = requests.post(url + f'/campaign/{campaign}/comment/{comment_id}', json=reply, headers=header)
        print("Comentario", reply['comment'], "cadastrado")
    except:
        print(r.raise_for_status())
        print("Erro ao processar requisição")
        print("Comentario", comment['comment'])
