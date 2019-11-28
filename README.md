# ajude

[![Language](https://img.shields.io/badge/java->=8-brown?style=flat&logo=appveyor)](https://www.java.com/)
[![Framework](https://img.shields.io/badge/spring%20boot-2.2.1-red?style=flat&logo=appveyor)](https://spring.io/)

Repositório do backend do projeto _AJuDE - AquiJuntosDoandoEsperança_ da disciplina Projeto de Software 2019.2.  

## Tópicos

- [ajude](#ajude)
  - [Tópicos](#t%c3%b3picos)
    - [Caso de uso 1](#caso-de-uso-1)
    - [Caso de uso 1A](#caso-de-uso-1a)
    - [Caso de uso 2](#caso-de-uso-2)
    - [Caso de uso 3](#caso-de-uso-3)
    - [Caso de uso 4](#caso-de-uso-4)
    - [Caso de uso 4A](#caso-de-uso-4a)
    - [Caso de uso 4B](#caso-de-uso-4b)
    - [Caso de uso 4C](#caso-de-uso-4c)
    - [Caso de uso 5](#caso-de-uso-5)
    - [Caso de uso 6](#caso-de-uso-6)
    - [Caso de uso 7](#caso-de-uso-7)
    - [Caso de uso 7A](#caso-de-uso-7a)
    - [Caso de uso 8](#caso-de-uso-8)
    - [Caso de uso 9](#caso-de-uso-9)
    - [Caso de uso 10](#caso-de-uso-10)
    - [Links](#links)
    - [Grupo](#grupo)

### Caso de uso 1

> Rota pública

Neste caso de uso torna possível cadastrar um usuário e realizar o login. Inicialmente, para criar um usuário é utilizada a rota `api/user`, com o método http `POST`, onde o body da requisição possui um json com todas as informações necessárias para o cadastro do mesmo. Um exemplo de como seria é o corpo da requisição:
```
{
    "firstName": "Higor",
    "lastName": "Dantas",
    "creditCardNumber": "00000000-00",
    "email": "higorsantos600@gmail.com",
    "password": "Senha123"
}
```
Caso a criação do usuário ocorra da maneira esperada é retornado o código HTTP 201.  
Já para realizar o login é necessário realizar um `POST` para a rota `api/auth/login`, onde no corpo é passado as credenciais do usuário.
```
{
    "email": "higorsantos600@gmail.com",
    "password": "Senha123"
}
```
Como resposta da requisição é obtido um token que possui validade de 30 minutos, e o username do usuário que está sendo logado. Quanto ao tempo escolhido para a validade, o grupo considerou que 30 minutos seria o ideal, pois é um tempo razoavelmente alto considerando que dificilmente um usuário chegaria a mais tempo utilizando a aplicação, e dessa forma garantiria um pouco de segurança durante o uso da mesma.

### Caso de uso 1A

> Rota pública

Para alterar a senha é realizado uma requisição `POST` para a rota `api/auth/forgotPassword`, onde o corpo da requisição é um json com o email de quem está solicitando:
```
{
    "email": "higorsantos600@gmail.com"
}
```
Em seguida, é enviado um email para o usuário com o link que ele poderá realizar a alteração. E, da mesma forma, é feita uma requisição `POST` utilizando a rota `api/resetPassword/{token}`, onde `{token}` é um token gerado com duração de 1 minuto e que dá permissão ao mesmo realizar a alteração da senha.

### Caso de uso 2

> Rota privada

É acessada após o login, sendo feita uma requisição `POST` para a rota `api/campaign/register`.
```
{
    "shortName": "Cadeira para Maria filha de Joao",
    "urlIdentifier": "cadeira-para-maria-filha-de-joao",
    "description": "Cadeira de rodas",
    "deadline": "2020-12-04 23:59:59",
    "goal": "2000.00"
}
```

Para que ocorra as atualizações dos status das campanhas foi criado um agendamento que executa essa verificação a cada 20 minutos.

Por fim, cada usuário pode cadastrar muitas campanhas, gerando um relacionamento `@ManyToOne` (do ponto de vista das campanhas).

### Caso de uso 3

> Rota privada

É acessada após o login, sendo feita uma requisição `POST` para a rota `api/campaign/search`.  
A busca pelas campanhas é solicitada diretamente ao banco de dados, que retorna todos os resultados que se encaixam na solicitação.

```
{
    "substring": "cadeira"
}
```

### Caso de uso 4

> Rota privada

É realizado uma requisição `GET` para a rota `api/campaign/{campaignUrl}`, onde `{campaignUrl}` é a url gerada para uma campanha.

### Caso de uso 4A

> Rota privada

É realizado uma requisição `PUT` para a rota `api/campaign/{campaignUrl}/closeCampaign`, onde `{campaignUrl}` é a url gerada para uma campanha.

### Caso de uso 4B

> Rota privada

É realizado uma requisição `PUT` para a rota `api/campaign/{campaignUrl}/setDeadline`, onde `{campaignUrl}` é a url gerada para uma campanha. No corpo é passado o novo deadline.
```
{
    "date": "25-12-2019"
}
```

### Caso de uso 4C

> Rota privada


É realizado uma requisição `PUT` para a rota `api/campaign/{campaignUrl}/setGoal`, onde `{campaignUrl}` é a url gerada para uma campanha. No corpo é passado a nova meta.
```
{
    "goal": 200
}
```

### Caso de uso 5

> Rota privada

É realizado uma requisição `POST` para a rota `api/campaign/{campaignUrl}/comment`, onde `{campaignUrl}` é a url gerada para uma campanha. No corpo da requisição é passado um json contendo o comentário realizado.
```
{
    "comment":"MUITO BOM!"
}
```
Cada comentário possui um comentário que é a sua resposta, uma relação `@OneToOne`. E, cada campanha pode possuir muitos comentários, o que torna uma relação `@OneToMany` (do ponto de vista das campanhas).

### Caso de uso 6

> Rota privada
> 
Para cada comentário é realizado uma deleção lógica, ou seja, ele não deixa de existir e para realizá-la é necessário ser o usuário que criou o comentário.  
É uma requisição `DELETE` para a rota `api/campaign/{campaignUrl}/comment/{id}`, onde `{campaignUrl}` é a campanha que possui o comentário e `{id}` é o id do comentário a ser apagado.

### Caso de uso 7

> Rota privada

A partir deste caso de uso o usuário pode dar ou retirar um like.  
É uma requisição `POST`, para a rota `api/campaign/{campaignUrl}/like`, onde `{campaignUrl}` é a campanha que receberá o like e é necessário passar no corpo um json vazio, utilizado para facilitar a captura de informações do usuário.

### Caso de uso 7A

> Rota privada

Além de dar like, o usuário também poderá dar dislike... Porém apenas um das duas opções ocorrerá. Ou seja, se caso o usuário tiver dado um like, o like será retirado, enquanto que o dislike será alocado e vice-versa.  
É uma requisição `POST`, para a rota `api/campaign/{campaignUrl}/dislike`, onde `{campaignUrl}` é a campanha que receberá o dislike e é necessário passar no corpo um json vazio, utilizado para facilitar a captura de informações do usuário.

### Caso de uso 8

> Rota privada

O usuário pode realizar uma doação e isso é feito por uma requisição `POST` para a rota `api/campaign/{campaignUrl}/donate`, onde `{campaignUrl}` é a campanha que receberá a doação.  
No corpo da requisição é passado um json informando o valor doado, por exemplo:
```
{
    "value": 10
}
```

### Caso de uso 9

> Rota pública

É possível acessar as informações das 5 primeiras campanhas ordenadas por likes, deadline e pelo que resta para atingir a meta.  
Ambas requisições são `GET`, com as rotas, respectivamente, `api/home/like`, `api/home/date`, `api/home/remaining`. É importante destacar que em todos os 3 casos as informações já são puxadas do banco de dados da forma que interessa para a aplicação, ou seja, ordenadas e filtradas para as 5 primeiras.

### Caso de uso 10

> Rota pública

Qualquer um pode acessar as informações públicas (campanhas criadas, nome, email e doações realizadas) de um usuário cadastrado realizando uma requisição `GET` para a rota `api/profile`.

### Links

1. Deploy feito no Heroku: [apiajude](https://apiajude.herokuapp.com)
2. Documentação Swagger: [link](https://apiajude.herokuapp.com/api/swagger-ui.html#/)

### Grupo
> [Izaquiel Cordeiro](https://github.com/IzaquielCordeiro)  
> [Higor Santos](https://github.com/HigorSnt)  
> [Mateus Alves](https://github.com/mateustranquilino)  

<p align="center">
  <img src="http://alumni.computacao.ufcg.edu.br/static/logica/images/logo.png"/>  
</p>