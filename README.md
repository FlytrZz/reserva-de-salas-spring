# 🏢 Reserva de Salas

API REST para gerenciamento de **usuários**, **salas** e **reservas**, desenvolvida com **Java** e **Spring Boot**. O sistema impede reservas com horários conflitantes na mesma sala e valida os dados de entrada.

## ✨ Funcionalidades

- CRUD completo de **usuários**, **salas** e **reservas**
- Verificação de **conflito de horário**: não permite duas reservas sobrepostas na mesma sala (reservas que apenas se encostam, fim = início, são permitidas)
- Validação de período: o fim da reserva precisa ser depois do início
- Validação dos dados de entrada com Bean Validation (nome obrigatório, e-mail válido, capacidade positiva etc.)
- Tratamento global de exceções com códigos HTTP adequados (`400`, `404` e `409`)
- Status da reserva (`ATIVA` / `CANCELADA`)
- Testes automatizados de serviços e controllers

## 🛠️ Tecnologias

- Java 25
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- MySQL (driver do PostgreSQL também incluído nas dependências)
- Gradle (wrapper incluso)
- JUnit 5
- Docker
- Dependabot para atualização semanal de dependências

## 🗂️ Estrutura do projeto

```
src/main/java/SpringApplication
├── controller   # Endpoints REST
├── service      # Regras de negócio
├── repository   # Acesso ao banco (Spring Data JPA)
├── model        # Entidades: Usuario, Sala, Reserva e enum Status
├── DTO          # Records de entrada com validações
└── exception    # Exceções customizadas e handler global
```

## 📋 Pré-requisitos

- JDK 25
- MySQL rodando e com um banco criado
- (Opcional) Docker

## ⚙️ Configuração

A conexão com o banco é feita por variáveis de ambiente, definidas em `application.properties`:

| Variável | Descrição |
|----------|-----------|
| `DB_HOST` | Host do MySQL (ex.: `localhost`) |
| `DB_NAME` | Nome do banco de dados |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |

As tabelas são criadas/atualizadas automaticamente (`spring.jpa.hibernate.ddl-auto=update`).

## 🚀 Como executar

```bash
# Clone o repositório
git clone https://github.com/FlytrZz/reserva-de-salas-spring.git
cd reserva-de-salas-spring

# Defina as variáveis de ambiente (Linux/macOS)
export DB_HOST=localhost
export DB_NAME=reserva_salas
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha

# Rode a aplicação
./gradlew bootRun
```

No Windows (PowerShell), use `$env:DB_HOST="localhost"` e `.\gradlew.bat bootRun`.

A API ficará disponível em `http://localhost:8080`.

### 🐳 Com Docker

```bash
docker build -t reserva-de-salas .
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=reserva_salas \
  -e DB_USER=seu_usuario \
  -e DB_PASSWORD=sua_senha \
  reserva-de-salas
```

## 🧪 Testes

```bash
./gradlew test
```

## 📡 Endpoints

Base URL: `/api/v1`

### Usuários — `/api/v1/usuarios`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/usuarios` | Lista todos os usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| POST | `/usuarios` | Cria um usuário |
| PUT | `/usuarios/{id}` | Atualiza um usuário |
| DELETE | `/usuarios/{id}` | Remove um usuário |

```json
{
  "nome": "João Henrique",
  "email": "joao@email.com"
}
```

### Salas — `/api/v1/salas`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/salas` | Lista todas as salas |
| GET | `/salas/{id}` | Busca sala por ID |
| POST | `/salas` | Cria uma sala |
| PUT | `/salas/{id}` | Atualiza uma sala |
| DELETE | `/salas/{id}` | Remove uma sala |

```json
{
  "nome": "Sala 101",
  "capacidade": 30,
  "disponibilidade": true
}
```

### Reservas — `/api/v1/reservas`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/reservas` | Lista todas as reservas |
| GET | `/reservas/{id}` | Busca reserva por ID |
| POST | `/reservas` | Cria uma reserva |
| PUT | `/reservas/{id}` | Atualiza uma reserva |
| DELETE | `/reservas/{id}` | Remove uma reserva |

```json
{
  "idUsuario": 1,
  "idSala": 1,
  "inicio": "2026-11-12T14:00:00",
  "fim": "2026-11-12T16:00:00"
}
```

## ⚠️ Respostas de erro

| Código | Quando acontece |
|--------|-----------------|
| `400 Bad Request` | Dados inválidos (ex.: fim da reserva antes do início, campos obrigatórios ausentes) |
| `404 Not Found` | Usuário, sala ou reserva não encontrados |
| `409 Conflict` | A sala já possui uma reserva nesse horário |

## 🔮 Próximos passos

- [ ] Autenticação e autorização (Spring Security)
- [ ] Documentação da API com Swagger/OpenAPI
- [ ] Endpoint para cancelar reserva (mudando o status para `CANCELADA`) em vez de apagar
- [ ] Migrations com Flyway
- [ ] Deploy

## 👨‍💻 Autor

**João Henrique**
GitHub: [@FlytrZz](https://github.com/FlytrZz)
