# 🧩 ms-users

Este projeto é um microsserviço backend desenvolvido em Spring Boot, responsável pelo gerenciamento de usuários dentro da arquitetura do sistema proposto no Tech Challenge - Fase 1 da FIAP.

A aplicação tem como objetivo oferecer suporte ao cadastro, atualização, exclusão, autenticação e validação de usuários, permitindo operações fundamentais para o sistema compartilhado de gestão de restaurantes da região. O backend foi estruturado para ser modular, escalável e seguro, atendendo aos critérios de boas práticas de desenvolvimento.

---

## 📦 Tecnologias utilizadas

- Java 17
- Spring Boot 3.4.4
- PostgreSQL
- Flyway (migração de banco de dados)
- Spring Security
- JWT (Java Web Token)
- Docker
- Docker Compose
- Maven

---

## 🚀 Executando localmente

### Pré-requisitos

- Docker
- Docker Compose

### Variáveis de ambiente

É necessário criar um arquivo ".env" na pasta ms-users, com o acesso ao banco de dados:

```
DB_USER=
DB_PASS=
```

Como o sistema será executado localmente, não há credencial fixa para o projeto. 

### Subindo os serviços

```bash
docker compose up --build
```

---

## 🧪 Executando testes

Você pode executar os testes unitários manualmente com:

```bash
docker run --rm -v $(pwd):/app -w /app -v $HOME/.m2:/root/.m2 maven:3.9.6-eclipse-temurin-17 mvn test
```

Ou usando o script de CI local (explicado abaixo).

---

## 🛠️ Scripts de automação

### `local-continuous-integration.sh`

Este script realiza um processo completo de CI local:

- Para os serviços com Docker Compose
- Executa os testes automatizados com Maven
- Constrói a imagem Docker com versionamento semântico
- Sobe novamente os serviços

#### Uso

```bash
./local-continuous-integration.sh [patch|minor|major] [--skip-tests] [--no-cache]
```

- `patch`, `minor`, `major`: nível de incremento de versão (padrão: `patch`)
- `--skip-tests`: pula os testes Maven
- `--no-cache`: força build do Maven sem cache local (`~/.m2`)

---

### `app-build.sh`

Script responsável por gerar novas imagens Docker com controle de versão semântico:

- Detecta a última versão existente (ex: `0.1.5`)
- Incrementa a versão com base no parâmetro (patch/minor/major)
- Gera imagem com nova tag + `latest`

#### Uso:

```bash
./app-build.sh [patch|minor|major]
```

---

## 🐘 Banco de Dados

- PostgreSQL
- Flyway é usado para migração automática de scripts SQL.
- Scripts de versão ficam em: `src/main/resources/db/migration`

---

## 🔒 Autenticação

- Utiliza JWT (`com.auth0:java-jwt`)
- Spring Security gerencia endpoints seguros

---

## 🧪 Testes

- Testes com Spring Boot Test, JUnit
- Banco de dados H2 em memória usado para testes
- Dependência de teste:

```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>
```

---

## 📄 Documentação e Testes - Postman

Todos os endpoints da API estão documentados seguindo boas práticas REST.

Arquivos de coleções do Postman estão disponíveis para testar os endpoints([Coleção Postman - MS-USERS](./ms-users/guides/postman_collection/ms-users.postman_collection.json)).

Assim como as configurações de ambiente([Ambiente Postman - MS-USERS](./ms-users/guides/postman_collection/MS-USERS.postman_environment.json)).

---

## 🐳 Docker

Imagens Docker seguem o padrão de versionamento semântico:

- `tech-challenge-fiap-01:0.0.1`, `tech-challenge-fiap-01:latest`, etc.

Scripts automatizados cuidam da construção e versionamento.

---

## 🧹 Limpeza de imagens antigas

O CI local remove automaticamente imagens **dangling**:

```bash
docker image prune -f
```

---

## 🧪 Endpoints

Base path: `/users`

---

## 🔹 GET `/users/{id}`

### 📥 Path Parameter:
- `id` (UUID): ID do usuário.

### 📤 Response:
- **200 OK**: `UsersResponseDTO`
```json
{
  "id": "UUID",
  "name": "string",
  "email": "string",
  "login": "string",
  "role": "OWNER | CLIENT",
  "address": []
}
```

---

## 🔹 GET `/users?size={size}&page={page}`

### 📥 Query Parameters:
- `size` (int): Número de usuários por página.
- `page` (int): Número da página.

### 📤 Response:
- **200 OK**: `List<UsersResponseDTO>`

---

## 🔹 POST `/users`

### 📥 Request Body: `UsersRequestDTO`
```json
{
  "name": "string",
  "email": "string",
  "login": "string",
  "password": "string",
  "role": "OWNER | CLIENT",
  "address": [
    {
      "zipcode": "string",
      "street": "string",
      "number": 123,
      "complement": "optional",
      "neighborhood": "string",
      "city": "string",
      "state": "SP"
    }
  ]
}
```

### 📤 Response:
- **200 OK**: `CreateUserDTO`
```json
{
  "user": {
    "id": "UUID",
    "name": "string",
    "email": "string",
    "login": "string",
    "role": "OWNER | CLIENT",
    "address": []
  },
  "tokenJWT": "string"
}
```

---

## 🔹 POST `/users/login`

### 📥 Request Body: `AuthenticationDataDTO`
```json
{
  "login": "string",
  "password": "string"
}
```

### 📤 Response:
- **200 OK**: `TokenJWTInfoDTO`
```json
{
  "tokenJWT": "string"
}
```

---

## 🔹 PATCH `/users/{id}?activate={true|false}`

### 📥 Path Parameter:
- `id` (UUID)

### 📥 Query Parameter:
- `activate` (boolean)

### 📤 Response:
- **200 OK**:
    - `"Usuário ativado com sucesso!"` ou `"Usuário desativado com sucesso!"`

---

## 🔹 PATCH `/users/{id}/password`

### 📥 Path Parameter:
- `id` (UUID)

### 📥 Request Body: `ChangePasswordRequestDTO`
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### 📤 Response:
- **200 OK**: `"Senha alterada com sucesso!"`

---

## 🔹 PUT `/users/{id}`

### 📥 Path Parameter:
- `id` (UUID)

### 📥 Request Body: `UpdateUserDTO`
```json
{
  "name": "string",
  "email": "string",
  "login": "string",
  "address": [
    {
      "zipcode": "string",
      "street": "string",
      "number": 123,
      "complement": "optional",
      "neighborhood": "string",
      "city": "string",
      "state": "SP"
    }
  ]
}
```

### 📤 Response:
- **200 OK**: `UsersResponseDTO`

---

## 📄 Licença

Este projeto é parte de um desafio educacional da FIAP. Uso livre para fins acadêmicos.

## Java Docs

[Documentação Java Docs](https://anacarolcortez.github.io/tech-challenge-fiap-parte1/)

## Open API

Após executar o sistema localmente, via Docker, é possível acessar a documentação das APIs também pelo Open API, em adição ao Postman.
O link de acesso é:
http://localhost:8080/swagger-ui/index.html