# 🏥 Sistema de Autorização de Procedimentos Médicos

Sistema web para controle e autorização de procedimentos médicos baseado em regras de idade e sexo do paciente.


## 🎯 Sobre o Projeto

Sistema desenvolvido para gerenciar autorizações de procedimentos médicos em planos de saúde, validando automaticamente se um procedimento é permitido com base em critérios de **idade** e **sexo** do paciente.


## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 11** - Linguagem de programação
- **Jakarta EE 9** - Plataforma enterprise
- **Servlets 5.0** - Processamento de requisições HTTP
- **JSP 3.0 + JSTL 2.0** - Camada de visualização
- **JDBC** - Acesso a banco de dados

### Servidor de Aplicação
- **WildFly 38** - Application Server (compatível com Jakarta EE)

### Banco de Dados
- **PostgreSQL 16** - Banco de dados relacional
- **Liquibase 4.29.2** - Versionamento de schema

### Build e Deploy
- **Maven 3.x** - Gerenciamento de dependências e build
- **Docker & Docker Compose** - Containerização

### Frontend
- **Bootstrap 5.3** - Framework CSS
- **Bootstrap Icons** - Biblioteca de ícones
- **JavaScript Vanilla** - Validações client-side

---

## 📦 Dependências Maven

```xml
<!-- Jakarta Servlet API -->
jakarta.servlet:jakarta.servlet-api:5.0.0

<!-- JSTL (JSP Standard Tag Library) -->
jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:2.0.0
org.glassfish.web:jakarta.servlet.jsp.jstl:2.0.0

<!-- PostgreSQL Driver -->
org.postgresql:postgresql:42.7.3

<!-- Liquibase (Migrations) -->
org.liquibase:liquibase-core:4.29.2

<!-- Gson (JSON) -->
com.google.code.gson:gson:2.11.0

<!-- JUnit 5 (Testes) -->
org.junit.jupiter:junit-jupiter:5.9.3
```

---

## 🏗️ Arquitetura

### Padrão MVC + Camadas

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│   JSP Pages + Bootstrap + JavaScript    │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│          CONTROLLER LAYER               │
│  Servlets (AuthorizationRequestServlet, │
│           ProcedureRulesServlet)        │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│          SERVICE LAYER                  │
│  Business Logic + Validations           │
│  (AuthorizationService, ProcedureService)│
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│           DAO LAYER                     │
│  Data Access (AuthorizationRequestDAO,  │
│              ProcedureRulesDAO)         │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│         DATABASE LAYER                  │
│  PostgreSQL (Liquibase Migrations)      │
└─────────────────────────────────────────┘
```

### Princípios Aplicados

#### **SOLID**
- ✅ **Single Responsibility**: Cada classe tem uma única responsabilidade
- ✅ **Liskov Substitution**: Uso de interfaces permite substituição
- ✅ **Interface Segregation**: Interfaces específicas por camada
- ✅ **Dependency Inversion**: Dependência de abstrações (interfaces)

#### **Dependency Injection (DI)**
```java
// Exemplo: Servlet injeta dependências no init()
public void init() throws ServletException {
    IDatabaseConfig dbConfig = new DatabaseConfig();
    IAuthorizationRequestDAO dao = new AuthorizationRequestDAO(dbConfig);
    this.authorizationService = new AuthorizationService(dao);
}
```

#### **Exception Handling Global**
- Filtro global (`ExceptionHandlerFilter`) captura todas as exceções
- Exceções de negócio (`BusinessException`) exibem mensagens customizadas
- Erros inesperados geram ID de rastreamento e log completo
- Feedback visual via **Toasts do Bootstrap**

---

## ⚙️ Funcionalidades

### 1. 📋 Gerenciamento de Regras de Procedimentos
- Visualizar todas as regras cadastradas
- Regras definem: código do procedimento, idade, sexo e permissão
- Interface visual com badges coloridos

### 2. 🏥 Solicitação de Autorização
- Formulário para nova solicitação
- Campos: nome do paciente, idade, sexo, código do procedimento
- Validação automática em tempo real (client-side)
- Validação de regras de negócio (server-side)

### 3. ✅ Validação Automática
- Sistema verifica automaticamente se procedimento é permitido
- Comparação com regras cadastradas (código + idade + sexo)
- Geração de protocolo único
- Status: APROVADO ou NEGADO com justificativa

### 4. 📊 Histórico de Autorizações
- Listagem de todas as solicitações
- Filtros visuais por status (aprovado/negado)
- Visualização detalhada de cada autorização
- Data e hora de cada solicitação

### 5. 🎨 Interface Moderna
- Toasts para feedback visual
- Animações suaves
- Paleta de cores profissional

---

## 📋 Pré-requisitos

### Para Desenvolvimento Local

- **Java JDK 11+** ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **WildFly 38** ([Download](https://www.wildfly.org/downloads/))
- **Docker & Docker Compose** ([Download](https://www.docker.com/))
- **Git** ([Download](https://git-scm.com/))

### Para Execução via Docker

- **Docker & Docker Compose** apenas

---

## 🚀 Como Executar

### 🐳 **Opção 1: Execução Completa via Docker (Recomendado)**

#### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/procedure-authorization.git
cd procedure-authorization
```

#### 2. Configure o `application.properties`
Edite `src/main/resources/application.properties`:

```properties
# Para execução via Docker
jdbc.url=jdbc:postgresql://postgres-db:5432/procedure_authorization
jdbc.user=admin
jdbc.password=admin123
```

#### 3. Execute o Docker Compose
```bash
docker-compose up --build
```

#### 4. Acesse a aplicação
```
http://localhost:8080/home
```

**Serviços disponíveis:**
- **Aplicação**: http://localhost:8080/home
- **PostgreSQL**: localhost:5432

---

### 💻 **Opção 2: Execução Local (WildFly + PostgreSQL Docker)**

#### 1. Suba apenas o PostgreSQL via Docker
```bash
docker-compose up postgres-db
```

#### 2. Configure o `application.properties`
Edite `src/main/resources/application.properties`:

```properties
# Para execução local (PostgreSQL no Docker, WildFly local)
jdbc.url=jdbc:postgresql://localhost:5432/procedure_authorization
jdbc.user=admin
jdbc.password=admin123
```

#### 3. Compile o projeto
```bash
mvn clean package
```

#### 4. Inicie o WildFly
```bash
# Windows
cd C:\SDK\wildfly-38.0.0.Final
bin\standalone.bat

# Linux/Mac
cd /opt/wildfly-38.0.0.Final
bin/standalone.sh
```

#### 5. Faça o deploy
```bash
# Em outro terminal
mvn wildfly:deploy
```

#### 6. Acesse a aplicação
```
http://localhost:8080/home
```

---

### 🔄 **Redeploy após mudanças**

**Via Docker:**
```bash
docker-compose down
docker-compose up --build
```

**Local:**
```bash
mvn clean package wildfly:redeploy
```

### Dados Iniciais (Seeds)

O Liquibase insere automaticamente 6 regras de exemplo:

| Procedimento | Idade | Sexo | Permitido |
|--------------|-------|------|-----------|
| 1234 | 10 | M | ❌ NÃO |
| 4567 | 20 | M | ✅ SIM |
| 6789 | 10 | F | ❌ NÃO |
| 6789 | 10 | M | ✅ SIM |
| 1234 | 20 | M | ✅ SIM |
| 4567 | 30 | F | ✅ SIM |

---

## 🧪 Testes

### Executar Testes Unitários
```bash
mvn test
```

## 📝 Logs

### Localização dos Logs

**WildFly Local:**
```
wildfly-38.0.0.Final/standalone/log/server.log
```

**Docker:**
```bash
docker logs procedure_authorization_app
```

### Níveis de Log

- **INFO**: Inicialização de componentes
- **WARNING**: Erros de validação de negócio
- **SEVERE**: Erros inesperados com stack trace

### Exemplo de Log de Erro
```
[SEVERE] ❌ ERRO INESPERADO [ID: ERR-1698765432-1234] - URI: /authorizations - Method: POST - User: anonymous
java.sql.SQLException: Connection timeout
    at ...
```