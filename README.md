Desafio NEXDOM: WebApp JSP + Servlets (Jakarta EE) para autorização de procedimentos médicos.

## Conteúdo
- Aplicação Java (WAR) com Servlet e JSPs
- PostgreSQL + Liquibase changelog para criar tabela `rules_procedure` e `authorization_request`
- Dockerfile para construir a imagem da aplicação
- docker-compose com serviços: wildfly (app) e postgreSQL (banco persistente)

## Requisitos
- Docker & Docker Compose (ou apenas WildFly + Maven)
- JDK 11+, Maven 3.8+

## Como usar (com Docker Compose)
1. Fazer build do projeto e criar imagem (docker-compose faz build automaticamente):
   ```bash
   docker-compose up --build
   ```

2. Acesse a aplicação:
   `http://localhost:8080/procedure_authorization`

3. Caso queira visualizar o banco basta abrir o PgAdmin ou DBeaver e configurar as variaveis:

A configuração do liquibase está em `src/main/resources/liquibase.properties`.


