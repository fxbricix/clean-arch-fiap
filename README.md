# Sistema de Gestão de Restaurantes — Tech Challenge Fase 2

Back-end em **Java + Spring Boot** para um sistema de gestão compartilhado por um grupo de restaurantes. Projeto do **Tech Challenge – Fase 02** da Pós Tech FIAP (Arquitetura e Desenvolvimento Java), organizado em **Clean Architecture**.

Esta fase entrega o gerenciamento de **tipos de usuário** e **usuários**, o cadastro de **restaurantes** e o cadastro dos **itens do cardápio**.

- **Autor:** Fabricio Moreira
- **Repositório:** https://github.com/fxbricix/clean-arch-fiap
- **Vídeo de apresentação:** 🚧 _link a adicionar_

---

## Sumário

1. [Status do projeto](#status-do-projeto)
2. [Tecnologias](#tecnologias)
3. [Como executar](#como-executar)
4. [API](#api)
5. [Arquitetura](#arquitetura)
6. [Modelo de dados e migrations](#modelo-de-dados-e-migrations)
7. [Testes e cobertura](#testes-e-cobertura)
8. [Decisões de projeto](#decisões-de-projeto)
9. [Collection do Postman](#collection-do-postman)

---

## Status do projeto

| Funcionalidade | Status |
|---|---|
| Infraestrutura (Docker Compose, Flyway, Swagger) | ✅ pronto |
| Cadastro de **tipo de usuário** (CRUD) | ✅ pronto |
| **Usuário** e associação ao tipo de usuário | ✅ pronto |
| Cadastro de **restaurante** (CRUD) | ✅ pronto |
| Cadastro de **item do cardápio** (CRUD) | 🚧 em andamento: domínio, use case e persistência prontos; faltam a camada web (controller e DTOs) e o tratamento do `403` |
| Collection do Postman | 🚧 planejado |

---

## Tecnologias

| Item | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.3.5 (Web, Data JPA, Validation) |
| Banco de dados | PostgreSQL 17 |
| Migrations | Flyway 11 |
| Documentação da API | springdoc-openapi (Swagger UI) |
| Build | Maven (com Maven Wrapper) |
| Containers | Docker e Docker Compose |
| Testes | JUnit 5, Mockito, Testcontainers, ArchUnit |
| Cobertura | JaCoCo (mínimo de 80% de linhas) |

---

## Como executar

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) com Docker Compose

Para rodar os testes ou desenvolver fora do container, também são necessários o **JDK 21** e o Docker em execução (os testes de integração usam Testcontainers).

### Subindo tudo com Docker Compose

```bash
git clone https://github.com/fxbricix/clean-arch-fiap.git
cd clean-arch-fiap
docker compose up --build
```

O Compose sobe três serviços, nesta ordem:

1. **`postgres`**: banco PostgreSQL 17, com _healthcheck_ e volume nomeado `postgres-data`.
2. **`flyway`**: aplica as migrations de `database/migrations` e encerra.
3. **`app`**: a aplicação Spring Boot, iniciada somente depois que o banco está saudável e as migrations terminaram com sucesso.

Depois de subir:

| Recurso | URL |
|---|---|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI (JSON) | http://localhost:8080/v3/api-docs |
| PostgreSQL | `localhost:5432` (banco `fiap-tech-challenge`, usuário `postgres`, senha `postgres`) |

Para parar:

```bash
docker compose down        # para os containers, mantém os dados
docker compose down -v     # para e apaga o volume: o banco recomeça do zero
```

### Desenvolvimento local (aplicação fora do Docker)

Suba apenas o banco e as migrations e rode a aplicação pelo Maven:

```bash
docker compose up -d postgres flyway
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

### Variáveis de ambiente

A aplicação lê a conexão do banco destas variáveis (o Compose já as define; os valores abaixo são os padrões para rodar localmente):

| Variável | Padrão |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `fiap-tech-challenge` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | `postgres` |

> **Importante:** o schema é de responsabilidade do Flyway. A aplicação roda com `spring.jpa.hibernate.ddl-auto=validate` e `spring.flyway.enabled=false`; se o schema não bater com as entidades, a aplicação não sobe.

---

## API

Todas as rotas são prefixadas com `/v1`. A documentação interativa está no Swagger UI.

### Tipos de usuário — `/v1/tipo-usuarios`

| Método | Rota | Descrição | Sucesso | Erros possíveis |
|---|---|---|---|---|
| `POST` | `/v1/tipo-usuarios` | Cria um tipo de usuário | `201` | `400`, `409` |
| `GET` | `/v1/tipo-usuarios/{id}` | Busca um tipo pelo id | `200` | `404` |
| `GET` | `/v1/tipo-usuarios/listar` | Lista todos os tipos | `200` | — |
| `PATCH` | `/v1/tipo-usuarios/{id}` | Altera o nome de um tipo | `200` | `400`, `404`, `409` |
| `DELETE` | `/v1/tipo-usuarios/{id}` | Exclui um tipo | `204` | `404`, `409` |

**Regras do campo `nome`:** obrigatório (não pode ser vazio ou só espaços), no máximo 50 caracteres e **único**. Espaços nas pontas são removidos.

Os tipos `CLIENTE` e `DONO_RESTAURANTE` são criados pela migration `V1`. Esses tipos sistêmicos não podem ser alterados nem excluídos.

```bash
curl -X POST http://localhost:8080/v1/tipo-usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "GERENTE"}'
```

### Usuários — `/v1/usuarios`

| Método | Rota | Descrição | Sucesso | Erros possíveis |
|---|---|---|---|---|
| `POST` | `/v1/usuarios` | Cria um usuário com nome, e-mail e tipo | `201` | `400`, `404`, `409` |
| `GET` | `/v1/usuarios/{id}` | Busca um usuário pelo id | `200` | `404` |
| `GET` | `/v1/usuarios/listar` | Lista os usuários, ordenados pelo nome | `200` | — |
| `PUT` | `/v1/usuarios/{usuarioId}/tipo/{tipoUsuarioId}` | Associa o usuário a outro tipo | `200` | `404`, `422` |

**Regras**

- O **e-mail** é normalizado e deve ser único (`409` se já existir).
- O tipo informado precisa existir (`404`).
- **Bloqueio de troca de tipo:** um dono que ainda possui restaurantes não pode ser associado a outro tipo (`422`). É preciso transferir ou excluir os restaurantes antes.

```bash
curl -X POST http://localhost:8080/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "Maria", "email": "maria@fiap.com", "tipoUsuarioId": 2}'
```

### Restaurantes — `/v1/restaurantes`

| Método | Rota | Descrição | Sucesso | Erros possíveis |
|---|---|---|---|---|
| `POST` | `/v1/restaurantes` | Cria um restaurante | `201` | `400`, `404`, `422` |
| `GET` | `/v1/restaurantes/{id}` | Busca um restaurante pelo id | `200` | `404` |
| `GET` | `/v1/restaurantes/listar` | Lista todos os restaurantes | `200` | — |
| `PUT` | `/v1/restaurantes/{id}` | Atualiza todos os dados, inclusive o dono | `200` | `400`, `404`, `422` |
| `DELETE` | `/v1/restaurantes/{id}` | Exclui um restaurante | `204` | `404`, `409` |

**Campos:** `nome` (até 120 caracteres), `endereco` (até 255), `tipoCozinha` (até 60), `horarioFuncionamento` (`abertura` e `fechamento` no formato `HH:mm`) e `donoId`. Todos são obrigatórios.

**Regras**

- **Dono inexistente:** `donoId` que não corresponde a nenhum usuário retorna `404`.
- **Dono de outro tipo:** o usuário informado precisa ser do tipo `DONO_RESTAURANTE`; caso contrário, `422`. A regra vale também ao trocar o dono em um `PUT`.
- **Horário no mesmo dia:** a abertura deve ser anterior ao fechamento (`400` caso contrário). Não há horário que atravesse a meia-noite.

```bash
curl -X POST http://localhost:8080/v1/restaurantes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Cantina", "endereco": "Rua A, 10", "tipoCozinha": "Italiana",
       "horarioFuncionamento": {"abertura": "10:00", "fechamento": "22:30"}, "donoId": 1}'
```

### Itens do cardápio — em andamento

A regra de negócio já está implementada e testada; os endpoints REST ainda serão expostos.

| Recurso | Rotas previstas | Campos |
|---|---|---|
| Item do cardápio | CRUD em `/v1/restaurantes/{id}/itens` | nome, descrição, preço, disponível apenas no local, caminho da foto |

**Regras já implementadas no use case**

- Criar, atualizar e excluir exigem o **usuário que é dono do restaurante**: restaurante ou usuário inexistente resulta em `404`; usuário que não é o dono, em `403`.
- Buscar e listar exigem apenas que o restaurante exista (`404`).
- O item é sempre buscado **dentro do restaurante informado**: um item de outro restaurante é tratado como inexistente (`404`).
- `nome` (até 120) e `descricao` (até 500) são obrigatórios; `preco` deve ser maior que zero, com no máximo 2 casas decimais; `caminhoFoto` é opcional (até 255).

### Respostas de erro

Os erros seguem o formato **RFC 7807** (`application/problem+json`):

| Status | Quando acontece |
|---|---|
| `400 Bad Request` | corpo inválido ou mal formatado (campo obrigatório ausente, tamanho excedido, horário inválido) ou regra de domínio violada |
| `403 Forbidden` | usuário que não é o dono do restaurante tenta alterar o cardápio _(a ser mapeado na camada web)_ |
| `404 Not Found` | o recurso pedido não existe (inclusive o dono informado ao cadastrar um restaurante) |
| `409 Conflict` | nome já cadastrado, e-mail já cadastrado, tentativa de excluir um registro em uso, ou alteração/exclusão de tipo sistêmico |
| `422 Unprocessable Entity` | regra de negócio violada: dono que não é do tipo `DONO_RESTAURANTE`, ou troca de tipo de um dono que ainda possui restaurantes |

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Mensagem descrevendo o erro",
  "instance": "/v1/tipo-usuarios/99"
}
```

Erros de validação de campos trazem ainda a propriedade `erros`, com a mensagem por campo (por exemplo, `{"erros": {"nome": "O nome do restaurante é obrigatório"}}`).

---

## Arquitetura

O projeto segue a **Clean Architecture** em três camadas. A **regra de dependência** é única: as dependências apontam sempre para dentro.

```mermaid
flowchart TB
    subgraph INFRA["infrastructure"]
        WEB["web: controllers, DTOs, handler de erros"]
        PERS["persistence: entidades JPA, repositories, gateways"]
        CFG["config: beans dos use cases"]
    end
    subgraph APP["application"]
        UC["use cases + portas (interfaces)"]
    end
    subgraph DOM["domain"]
        ENT["entidades, value objects e exceções de domínio"]
    end
    WEB --> UC
    PERS --> UC
    CFG --> UC
    UC --> ENT
```

| Camada | Responsabilidade | Pode depender de |
|---|---|---|
| `domain` | Entidades, value objects e regras de negócio puras. Java puro, sem Spring e sem JPA. | nada |
| `application` | Casos de uso e as **portas** (interfaces) que eles precisam: use cases e gateways de persistência. Sem Spring. | `domain` |
| `infrastructure` | Detalhes técnicos e frameworks: controllers REST, DTOs, entidades JPA, repositories, implementação dos gateways, configuração. | `application` e `domain` |

A inversão de dependência acontece nos gateways: a **interface** é definida em `application` e a **implementação** fica em `infrastructure`, então o use case nunca conhece JPA nem o banco.

A regra é verificada automaticamente por testes **ArchUnit**: se `domain` ou `application` passarem a importar Spring, JPA ou `infrastructure`, o build falha.

### Fluxo de uma requisição

```mermaid
sequenceDiagram
    participant C as Cliente HTTP
    participant Ctrl as Controller
    participant UC as Use case (application)
    participant GW as Gateway (infrastructure)
    participant DB as PostgreSQL

    C->>Ctrl: POST /v1/restaurantes
    Ctrl->>UC: criar(Restaurante)
    UC->>GW: usuarioGateway.buscarPorId (valida o dono)
    UC->>GW: restauranteGateway.salvar
    GW->>DB: SQL
    DB-->>GW: registro
    GW-->>UC: Restaurante
    UC-->>Ctrl: Restaurante
    Ctrl-->>C: 201 + RestauranteDTO
```

### Estrutura de pacotes

```
src/main/java/com/fiap/cleanarchitecture
├── domain
│   ├── entities                 TipoUsuario, Usuario, Restaurante, ItemCardapio
│   ├── valueobjects             Email, HorarioFuncionamento
│   └── exceptions               DominioException
│
├── application
│   ├── interfaces               I*UseCase                      (portas de entrada)
│   ├── usecases                 *UseCaseImpl
│   ├── gateways                 I*Gateway                      (portas de saída)
│   └── exceptions               ConflitoException, RecursoNaoEncontradoException,
│                                RegraDeNegocioException, AcessoNegadoException
│
└── infrastructure
    ├── config                   UseCaseConfig                  (use cases como beans)
    ├── web
    │   ├── controllers          TipoUsuario, Usuario e Restaurante
    │   ├── dtos                 DTOs de entrada e saída
    │   ├── mappers              *WebMapper                     (DTO <-> domínio)
    │   └── exceptions           GlobalExceptionHandler
    └── persistence
        ├── entities             *Entity                        (modelo JPA)
        ├── repository           *JpaRepository
        ├── mappers              *PersistenceMapper             (JPA <-> domínio)
        └── gateway              *GatewayImpl

database/migrations              scripts Flyway (V1 a V5)
src/test                         testes unitários, de integração e de arquitetura
```

---

## Modelo de dados e migrations

```mermaid
erDiagram
    TIPO_USUARIO ||--o{ USUARIO : classifica
    USUARIO ||--o{ RESTAURANTE : "é dono de"
    RESTAURANTE ||--o{ ITEM_CARDAPIO : oferece

    TIPO_USUARIO {
        bigint id PK
        varchar nome UK
    }
    USUARIO {
        bigint id PK
        varchar nome
        varchar email UK
        bigint tipo_usuario_id FK
    }
    RESTAURANTE {
        bigint id PK
        varchar nome
        varchar endereco
        varchar tipo_cozinha
        time hora_abertura
        time hora_fechamento
        bigint dono_id FK
    }
    ITEM_CARDAPIO {
        bigint id PK
        bigint restaurante_id FK
        varchar nome
        varchar descricao
        numeric preco
        boolean apenas_no_local
        varchar caminho_foto
    }
```

| Migration | O que faz |
|---|---|
| `V1__criar_tipo_usuario.sql` | cria `tipo_usuario` e insere `CLIENTE` e `DONO_RESTAURANTE` |
| `V2__criar_usuario.sql` | cria `usuario`, com chave estrangeira para o tipo |
| `V3__criar_tabela_restaurante.sql` | cria `restaurante`, com o dono (usuário) |
| `V4__criar_item_cardapio.sql` | cria `item_cardapio`, ligado ao restaurante |
| `V5__ajustar_restricoes_item_cardapio.sql` | torna `descricao` obrigatória e exige `preco > 0` em `item_cardapio` |

As migrations ficam em `database/migrations` e são executadas pelo serviço `flyway` do Compose. Depois de aplicada, uma migration **nunca deve ser editada**: crie uma nova versão (`V6`, `V7`...). Em desenvolvimento local, `docker compose down -v` recria o banco do zero.

---

## Testes e cobertura

```bash
./mvnw verify        # roda todos os testes e valida a cobertura (Docker precisa estar rodando)
```

| Tipo | O que cobre | Ferramentas |
|---|---|---|
| **Unitários** | regras do domínio, use cases, gateways, mappers e controllers, com dependências simuladas | JUnit 5, Mockito |
| **Integração** | API completa (HTTP → use case → banco), com **PostgreSQL real** em container e as migrations reais aplicadas | `@SpringBootTest`, MockMvc, Testcontainers, Flyway |
| **Arquitetura** | a regra de dependência entre as camadas | ArchUnit |

- Os testes de integração compartilham **um único container** PostgreSQL e usam dados únicos por teste, sem apagar tabelas, então um teste não depende de outro nem da ordem de execução.
- As migrations dos testes vêm de `database/migrations`; por isso o Maven deve ser executado na **raiz do projeto**.
- O **JaCoCo** exige no mínimo **80% de cobertura de linhas**: abaixo disso o `mvn verify` falha.
- Relatório de cobertura: `target/site/jacoco/index.html`.
- Em JDKs mais novos que o 21, o JaCoCo pode imprimir avisos de instrumentação durante os testes; eles não afetam o resultado.

---

## Decisões de projeto

| Decisão | Motivo |
|---|---|
| **Três camadas** (`domain`, `application`, `infrastructure`), com `infrastructure` dividida em `web` e `persistence` | atende à estrutura pedida no enunciado e mantém controllers e persistência como adaptadores independentes |
| **Sem presenter:** o use case devolve o resultado e o controller o converte em DTO | mais simples para uma API REST; um presenter só compensaria com vários formatos de saída |
| **Entidade de domínio como entrada e saída do use case** (campos soltos quando a entidade ainda não tem identidade, como no item do cardápio) | evita uma camada extra de objetos de entrada e saída; o custo é o controller conhecer o domínio |
| **Entidades JPA separadas das entidades de domínio**, com mappers | o domínio não conhece JPA, e o modelo de persistência pode mudar sem afetar as regras |
| **Use cases sem anotações do Spring**, registrados como beans em `UseCaseConfig` | mantém `application` livre de framework |
| **Flyway como serviço do Compose**, desligado na aplicação e usado somente nos testes | separa o ciclo de vida do schema do da aplicação; a aplicação só valida o schema (`ddl-auto=validate`) |
| **Erros traduzidos em um único ponto** (`GlobalExceptionHandler`) | o use case não sabe o que é um status HTTP; só lança exceções de domínio ou de aplicação |
| **Regras de negócio entre agregados no use case** (dono do restaurante, bloqueio de troca de tipo) | o domínio de cada entidade permanece isolado; quem coordena várias entidades é a camada de aplicação |
| **Restrições nas duas camadas:** validação no domínio e `UNIQUE`, `NOT NULL` e `CHECK` no banco | resposta amigável no caso comum e integridade garantida mesmo sob concorrência ou escrita fora da API |
| **Testes de integração com PostgreSQL real** (Testcontainers) em vez de banco em memória | valida as migrations reais e o comportamento do banco de produção |

---

## Collection do Postman

🚧 _A collection será adicionada em `postman/` com o fluxo completo: criar tipo → criar usuário → associar tipo → criar restaurante → cadastrar item do cardápio._

Enquanto isso, todos os endpoints disponíveis podem ser testados pelo Swagger UI: http://localhost:8080/swagger-ui.html
