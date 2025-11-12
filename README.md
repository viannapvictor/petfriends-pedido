# Guia de Testes - API PetFriends via Swagger

## 📋 Índice
- [Pré-requisitos](#pré-requisitos)
- [Microsserviço Almoxarifado](#microsserviço-almoxarifado)
  - [Endpoints de Comando (Write)](#endpoints-de-comando-write)
  - [Endpoints de Query (Read)](#endpoints-de-query-read)
- [Microsserviço Transporte](#microsserviço-transporte)
  - [Endpoints de Comando (Write)](#endpoints-de-comando-write-1)
  - [Endpoints de Query (Read)](#endpoints-de-query-read-1)
- [Fluxo Completo de Teste](#fluxo-completo-de-teste)

---

##  Pré-requisitos

### 1. Iniciar as Aplicações

```bash
# Terminal 1 - Almoxarifado
cd petfriends-almoxarifado
mvn spring-boot:run

# Terminal 2 - Transporte
cd petfriends-transporte
mvn spring-boot:run
```

### 2. Acessar o Swagger UI

- **Almoxarifado:** http://localhost:8082/swagger-ui.html
- **Transporte:** http://localhost:8083/swagger-ui.html

---

##  Microsserviço Almoxarifado

**Base URL:** `http://localhost:8082`

###  Endpoints de Comando (Write)

#### 1. POST - Reservar Estoque

**Endpoint:** `/almoxarifado/reservas`

**Descrição:** Cria uma nova reserva de estoque para um pedido.

**Request Body:**
```json
{
  "pedidoId": "PED-001",
  "itens": [
    {
      "produtoId": "PROD-001",
      "quantidade": 10
    },
    {
      "produtoId": "PROD-002",
      "quantidade": 5
    }
  ]
}
```

**Response:** ID da reserva criada (UUID)

**Como testar no Swagger:**
1. Clique em `POST /almoxarifado/reservas`
2. Clique em "Try it out"
3. Cole o JSON acima no campo "Request body"
4. Clique em "Execute"
5. **Copie o ID retornado** - você precisará dele para os próximos testes

---

#### 2. PUT - Confirmar Reserva

**Endpoint:** `/almoxarifado/reservas/{id}/confirmar`

**Descrição:** Confirma uma reserva pendente.

**Path Parameter:**
- `id`: ID da reserva (obtido no passo anterior)

**Request Body:** Não requer

**Como testar no Swagger:**
1. Clique em `PUT /almoxarifado/reservas/{id}/confirmar`
2. Clique em "Try it out"
3. Cole o **ID da reserva** no campo `id`
4. Clique em "Execute"

---

#### 3. PUT - Separar Itens

**Endpoint:** `/almoxarifado/reservas/separar`

**Descrição:** Separa os itens de uma reserva confirmada.

**Request Body:**
```json
{
  "id": "COLE_O_ID_DA_RESERVA_AQUI",
  "operadorId": "OP-001"
}
```

**Como testar no Swagger:**
1. Clique em `PUT /almoxarifado/reservas/separar`
2. Clique em "Try it out"
3. Cole o JSON acima, substituindo o ID
4. Clique em "Execute"

---

#### 4. PUT - Cancelar Reserva

**Endpoint:** `/almoxarifado/reservas/cancelar`

**Descrição:** Cancela uma reserva (não pode ser cancelada se já separada).

**Request Body:**
```json
{
  "id": "COLE_O_ID_DA_RESERVA_AQUI",
  "motivo": "Cliente cancelou o pedido"
}
```

**Como testar no Swagger:**
1. Crie uma nova reserva primeiro (Endpoint 1)
2. Clique em `PUT /almoxarifado/reservas/cancelar`
3. Clique em "Try it out"
4. Cole o JSON acima com o ID da nova reserva
5. Clique em "Execute"

---

###  Endpoints de Query (Read)

#### 5. GET - Obter Reserva por ID

**Endpoint:** `/almoxarifado/reservas/{id}`

**Descrição:** Consulta uma reserva pelo seu ID.

**Path Parameter:**
- `id`: ID da reserva

**Como testar no Swagger:**
1. Clique em `GET /almoxarifado/reservas/{id}`
2. Clique em "Try it out"
3. Cole o ID da reserva no campo `id`
4. Clique em "Execute"

**Response Exemplo:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "pedidoId": "PED-001",
  "status": "CONFIRMADA",
  "itens": [
    {
      "produtoId": "PROD-001",
      "quantidade": 10
    },
    {
      "produtoId": "PROD-002",
      "quantidade": 5
    }
  ],
  "operadorId": null
}
```

---

#### 6. GET - Obter Reserva por Pedido ID

**Endpoint:** `/almoxarifado/reservas/pedido/{pedidoId}`

**Descrição:** Busca uma reserva pelo ID do pedido.

**Path Parameter:**
- `pedidoId`: ID do pedido (ex: "PED-001")

**Como testar no Swagger:**
1. Clique em `GET /almoxarifado/reservas/pedido/{pedidoId}`
2. Clique em "Try it out"
3. Digite `PED-001` no campo `pedidoId`
4. Clique em "Execute"

---

#### 7. GET - Listar Eventos da Reserva

**Endpoint:** `/almoxarifado/reservas/{id}/eventos`

**Descrição:** Lista todos os eventos (Event Sourcing) de uma reserva.

**Path Parameter:**
- `id`: ID da reserva

**Como testar no Swagger:**
1. Clique em `GET /almoxarifado/reservas/{id}/eventos`
2. Clique em "Try it out"
3. Cole o ID da reserva
4. Clique em "Execute"

**Response Exemplo:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "pedidoId": "PED-001",
    "itens": [...]
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "pedidoId": "PED-001"
  }
]
```

---

##  Microsserviço Transporte

**Base URL:** `http://localhost:8083`

###  Endpoints de Comando (Write)

#### 1. POST - Agendar Entrega

**Endpoint:** `/transporte/entregas`

**Descrição:** Agenda uma entrega para uma reserva confirmada.

**Request Body:**
```json
{
  "pedidoId": "PED-001",
  "reservaId": "COLE_O_ID_DA_RESERVA_AQUI",
  "rua": "Rua das Flores",
  "numero": "123",
  "complemento": "Apto 45",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataPrevisao": "2025-11-15"
}
```

**Como testar no Swagger:**
1. Primeiro, crie e separe uma reserva no Almoxarifado
2. Acesse http://localhost:8083/swagger-ui.html
3. Clique em `POST /transporte/entregas`
4. Clique em "Try it out"
5. Cole o JSON acima, substituindo o `reservaId`
6. Clique em "Execute"
7. **Copie o ID da entrega retornado**

---

#### 2. PUT - Iniciar Transporte

**Endpoint:** `/transporte/entregas/iniciar`

**Descrição:** Inicia o transporte de uma entrega agendada.

**Request Body:**
```json
{
  "id": "COLE_O_ID_DA_ENTREGA_AQUI",
  "motoristaId": "MOT-001",
  "veiculoId": "VEI-001"
}
```

**Como testar no Swagger:**
1. Clique em `PUT /transporte/entregas/iniciar`
2. Clique em "Try it out"
3. Cole o JSON acima com o ID da entrega
4. Clique em "Execute"

---

#### 3. PUT - Concluir Entrega

**Endpoint:** `/transporte/entregas/concluir`

**Descrição:** Finaliza uma entrega que está em transporte.

**Request Body:**
```json
{
  "id": "COLE_O_ID_DA_ENTREGA_AQUI",
  "recebedor": "João Silva",
  "dataRecebimento": "2025-11-15T14:30:00",
  "observacoes": "Entrega realizada com sucesso"
}
```

**Como testar no Swagger:**
1. Clique em `PUT /transporte/entregas/concluir`
2. Clique em "Try it out"
3. Cole o JSON acima com o ID da entrega
4. Clique em "Execute"

---

### Endpoints de Query (Read)

#### 4. GET - Obter Entrega por ID

**Endpoint:** `/transporte/entregas/{id}`

**Descrição:** Consulta uma entrega pelo seu ID.

**Path Parameter:**
- `id`: ID da entrega

**Como testar no Swagger:**
1. Clique em `GET /transporte/entregas/{id}`
2. Clique em "Try it out"
3. Cole o ID da entrega
4. Clique em "Execute"

---

#### 5. GET - Obter Entrega por Pedido ID

**Endpoint:** `/transporte/entregas/pedido/{pedidoId}`

**Descrição:** Busca uma entrega pelo ID do pedido.

**Path Parameter:**
- `pedidoId`: ID do pedido (ex: "PED-001")

**Como testar no Swagger:**
1. Clique em `GET /transporte/entregas/pedido/{pedidoId}`
2. Clique em "Try it out"
3. Digite `PED-001` no campo `pedidoId`
4. Clique em "Execute"

---

#### 6. GET - Obter Entrega por Reserva ID

**Endpoint:** `/transporte/entregas/reserva/{reservaId}`

**Descrição:** Busca uma entrega pelo ID da reserva.

**Path Parameter:**
- `reservaId`: ID da reserva

**Como testar no Swagger:**
1. Clique em `GET /transporte/entregas/reserva/{reservaId}`
2. Clique em "Try it out"
3. Cole o ID da reserva
4. Clique em "Execute"

---

#### 7. GET - Listar Eventos da Entrega

**Endpoint:** `/transporte/entregas/{id}/eventos`

**Descrição:** Lista todos os eventos (Event Sourcing) de uma entrega.

**Path Parameter:**
- `id`: ID da entrega

**Como testar no Swagger:**
1. Clique em `GET /transporte/entregas/{id}/eventos`
2. Clique em "Try it out"
3. Cole o ID da entrega
4. Clique em "Execute"

---

## Fluxo Completo de Teste

### Cenário: Pedido Completo (da Reserva até a Entrega)

#### Passo 1: Reservar Estoque (Almoxarifado)
```
POST http://localhost:8082/almoxarifado/reservas
```
```json
{
  "pedidoId": "PED-TESTE-001",
  "itens": [
    {
      "produtoId": "RACAO-PREMIUM-15KG",
      "quantidade": 2
    },
    {
      "produtoId": "COLEIRA-MEDIA",
      "quantidade": 1
    }
  ]
}
```
 **Anote o ID da reserva:** `________________________________________`

---

#### Passo 2: Confirmar Reserva (Almoxarifado)
```
PUT http://localhost:8082/almoxarifado/reservas/{ID_DA_RESERVA}/confirmar
```

---

#### Passo 3: Consultar Status da Reserva (Almoxarifado)
```
GET http://localhost:8082/almoxarifado/reservas/{ID_DA_RESERVA}
```
 Verifique se o status está: `CONFIRMADA`

---

#### Passo 4: Separar Itens (Almoxarifado)
```
PUT http://localhost:8082/almoxarifado/reservas/separar
```
```json
{
  "id": "ID_DA_RESERVA",
  "operadorId": "OP-MARIA-123"
}
```

---

#### Passo 5: Consultar Status Atualizado (Almoxarifado)
```
GET http://localhost:8082/almoxarifado/reservas/{ID_DA_RESERVA}
```
 Verifique se o status está: `SEPARADA`

---

#### Passo 6: Agendar Entrega (Transporte)
```
POST http://localhost:8083/transporte/entregas
```
```json
{
  "pedidoId": "PED-TESTE-001",
  "reservaId": "ID_DA_RESERVA",
  "rua": "Av. Paulista",
  "numero": "1000",
  "complemento": "Loja 45",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01310-100",
  "dataPrevisao": "2025-11-20"
}
```
 **Anote o ID da entrega:** `________________________________________`

---

#### Passo 7: Consultar Entrega Agendada (Transporte)
```
GET http://localhost:8083/transporte/entregas/{ID_DA_ENTREGA}
```
 Verifique se o status está: `AGENDADA`

---

#### Passo 8: Iniciar Transporte (Transporte)
```
PUT http://localhost:8083/transporte/entregas/iniciar
```
```json
{
  "id": "ID_DA_ENTREGA",
  "motoristaId": "MOT-JOAO-456",
  "veiculoId": "VAN-SP-001"
}
```

---

#### Passo 9: Consultar Entrega em Trânsito (Transporte)
```
GET http://localhost:8083/transporte/entregas/{ID_DA_ENTREGA}
```
 Verifique se o status está: `EM_TRANSITO`

---

#### Passo 10: Concluir Entrega (Transporte)
```
PUT http://localhost:8083/transporte/entregas/concluir
```
```json
{
  "id": "ID_DA_ENTREGA",
  "recebedor": "Carlos Souza",
  "dataRecebimento": "2025-11-20T15:45:00",
  "observacoes": "Cliente recebeu e conferiu os produtos"
}
```

---

#### Passo 11: Consultar Entrega Concluída (Transporte)
```
GET http://localhost:8083/transporte/entregas/{ID_DA_ENTREGA}
```
 Verifique se o status está: `CONCLUIDA`

---

#### Passo 12: Consultar Histórico de Eventos

**Eventos da Reserva:**
```
GET http://localhost:8082/almoxarifado/reservas/{ID_DA_RESERVA}/eventos
```

**Eventos da Entrega:**
```
GET http://localhost:8083/transporte/entregas/{ID_DA_ENTREGA}/eventos
```

---

## Monitoramento

### Logs
Acompanhe os logs nos terminais onde os serviços estão rodando para ver:
- Eventos sendo publicados
- Event Handlers sendo acionados
- Queries no banco H2

### H2 Console
- **Almoxarifado:** http://localhost:8082/h2-console
- **Transporte:** http://localhost:8083/h2-console

**Configuração:**
- Driver: `org.h2.Driver`
- URL: `jdbc:h2:file:./h2_databases/almoxarifado_db` ou `transporte_db`
- User: `sa`
- Password: `password`

---

**Data de criação:** 2025-11-12  
**Versão:** 1.0.0-SNAPSHOT  
**Arquitetura:** DDD + CQRS + Event Sourcing

