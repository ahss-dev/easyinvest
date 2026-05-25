# EasyInvest - Backend Documentation

## Sobre o Projeto

O EasyInvest é uma aplicação full stack desenvolvida para simulação de gerenciamento de usuários e carteira financeira. O backend foi construído utilizando Java e Spring Boot, seguindo arquitetura em camadas para organização e separação de responsabilidades.

---

# Tecnologias Utilizadas

## Back-end

* Java
* Spring Boot
* Spring Security
* Spring Data JPA / Hibernate
* Maven
* PostgreSQL
* BCrypt

## Ferramentas

* Git
* GitHub
* GitFlow
* REST API

---

# Arquitetura

O projeto segue arquitetura em camadas:

```text
Controller -> Service -> Repository -> Database
```

## Camadas

### Controller

Responsável por receber as requisições HTTP e retornar as respostas da API.

### Service

Responsável pelas regras de negócio da aplicação.

### Repository

Responsável pela comunicação com o banco de dados utilizando Spring Data JPA.

### DTOs

Utilizados para transferência de dados entre cliente e servidor.

---

# Funcionalidades

## Usuários

* Cadastro de usuário
* Atualização de perfil
* Exclusão de conta
* Consulta de usuário autenticado

## Carteira (Wallet)

* Criação automática de carteira ao cadastrar usuário
* Controle de saldo
* Consulta de saldo do usuário autenticado

## Segurança

* Autenticação com Spring Security
* Criptografia de senhas utilizando BCrypt
* Proteção de endpoints autenticados

---

# Estrutura do Projeto

```text
src/main/java/com/easyinvest
│
├── controllers
├── dtos
├── entities
├── repositories
├── security
├── services
└── config
```

---

# Endpoints

## Usuário

### Criar usuário

```http
POST /users
```

### Buscar usuário autenticado

```http
GET /users/me
```

### Atualizar usuário autenticado

```http
PUT /users/me
```

### Deletar usuário autenticado

```http
DELETE /users/me
```

### Consultar saldo da carteira

```http
GET /users/me/wallet
```

---

# Exemplo de Requisição

## Cadastro de Usuário

```json
{
  "name": "Antonio",
  "email": "antonio@email.com",
  "password": "123456",
  "cpf": "00000000000",
  "phone": "38999999999",
  "address": "Minas Gerais",
  "sex": "M"
}
```

---

# Tratamento de Erros

A API utiliza ResponseStatusException para retorno de erros HTTP.

## Exemplos

### 409 - Conflict

Retornado quando email ou CPF já estão cadastrados.

### 401 - Unauthorized

Retornado quando usuário não está autenticado.

---

# Segurança

O projeto utiliza Spring Security para autenticação e proteção de rotas privadas.

As senhas dos usuários são criptografadas utilizando BCryptPasswordEncoder antes de serem armazenadas no banco de dados.

---

# Versionamento

O projeto utiliza GitFlow para organização do fluxo de desenvolvimento.

## Exemplo de branches

```text
feature/auth
feature/wallet
fix/user-validation
```

---

# Melhorias Futuras

* Integração com API de ativos financeiros
* Sistema de compra e venda de ativos
* Histórico de transações
* Dashboard financeiro
* Autenticação JWT
* Testes automatizados
* Dockerização da aplicação

---

# Autor

Antonio Henrique Soares Santos

GitHub:
[https://github.com/ahss-dev](https://github.com/ahss-dev)
