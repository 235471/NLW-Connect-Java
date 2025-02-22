# NLW Connect - Spring Boot API

Este projeto é uma API REST desenvolvida em **Spring Boot** para gerenciamento de **eventos** e **inscrições** com um sistema de rankings baseado em indicações. A aplicação permite o cadastro de eventos, inscrições de usuários e acompanhamento do ranking de inscrições por evento e usuário.

## Funcionalidades

- **Cadastro de eventos**: Permite adicionar novos eventos.
- **Inscrição de usuários**: Usuários podem se inscrever em eventos, podendo ser indicados por outros usuários.
- **Ranking de inscrições**: Mostra o ranking de inscrições por evento e também o ranking de um usuário específico.

## Estrutura

- **Services**: Contém a lógica de negócios para gerenciar eventos, inscrições e rankings.
- **Repositories**: Interface de acesso a dados, utilizando Spring Data JPA.
- **Controllers**: Exposição das funcionalidades via REST API.

### Endpoints da API

#### Eventos

- **POST /events**: Cria um novo evento.
    - Corpo: `{ "title": "Nome do Evento", "description": "Descrição do Evento" }`
- **GET /events**: Retorna a lista de todos os eventos cadastrados.
- **GET /events/{prettyName}**: Retorna os detalhes de um evento baseado no seu nome legível (`prettyName`).

#### Inscrições

- **POST /subscription/{prettyName}**: Cria uma inscrição para o usuário no evento especificado.
    - Corpo: `{ "email": "email@exemplo.com", "name": "Nome do Usuário" }`
- **POST /subscription/{prettyName}/{userId}**: Cria uma inscrição com indicação de um usuário (referrer).
    - Corpo: `{ "email": "email@exemplo.com", "name": "Nome do Usuário" }`
- **GET /subscription/{prettyName}/ranking**: Retorna o ranking de inscrições do evento especificado.
- **GET /subscription/{prettyName}/ranking/{userId}**: Retorna o ranking de inscrições de um evento para um usuário específico.

### Exceções

A API trata as seguintes exceções:

- **EventNotFoundException**: Evento não encontrado.
- **SubscriptionConflictException**: Confirmação de inscrição já existente.
- **UserReferrerNotFoundException**: Usuário indicante não encontrado.

## Tecnologias

- **Spring Boot**: Framework para construção da API.
- **Spring Data JPA**: Para persistência de dados com bancos relacionais.
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes.
- **Docker**: Opcional, para executar o MySQL via Docker.

## Como rodar o projeto

### 1. Clone o repositório:

```bash
git clone https://github.com/your-username/your-repository-name.git
```

### 2. Vá para o diretório do projeto:

```bash
cd NLW-Connect-Java/workspace
```

### 3. Configure o banco de dados no `application.properties` (se necessário):

Se você quiser usar um banco de dados MySQL local, altere as configurações de banco de dados no arquivo `src/main/resources/application.properties`.

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nlwconnect
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
```

Se for usar o `docker-compose.yml` para rodar o banco de dados, certifique-se de ter o Docker instalado e execute:

```bash
docker-compose up
```

### 4. Execute a aplicação:

Se você estiver utilizando o Maven:

```bash
mvn spring-boot:run
```

Se estiver utilizando o Gradle:

```bash
./gradlew bootRun
```

### 5. Teste os endpoints:

Após a aplicação iniciar, você pode acessar os endpoints via **Postman** ou qualquer cliente HTTP de sua escolha. Exemplos:

- **POST** `/events`
- **POST** `/subscription/{prettyName}`
- **GET** `/events`
- **GET** `/subscription/{prettyName}/ranking`
