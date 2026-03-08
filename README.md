# Trackr-Api

> Backend API tipo Trello para manejar **Boards → Lists → Tasks**, con **JWT Auth**, **PostgreSQL** y **Swagger UI**.  
> Incluye **tests** y **CI (GitHub Actions)** para validar automáticamente que cada cambio no rompa el sistema.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-brightgreen?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Docker-4169E1?logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?logo=swagger&logoColor=black)
![CI](https://github.com/YOUR_USER/trackr-api/actions/workflows/ci.yml/badge.svg)

---

## Data Model

```mermaid
erDiagram
    USER ||--o{ BOARD : owns
    BOARD ||--o{ LIST : contains
    LIST  ||--o{ TASK : contains

    USER {
        long id
        string username
        string email
        string password
    }
    BOARD {
        long id
        string name
        long userId
    }
    LIST {
        long id
        string name
        int position
        long boardId
    }
    TASK {
        long id
        string title
        string description
        long listId
    }
```

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL (Docker) para desarrollo
- H2 (in-memory) para tests y CI
- Swagger / OpenAPI
- Postman

---

## Requisitos

Para correr el proyecto necesitas:

- Java 17
- Maven
- Docker Desktop
- Postman (opcional)

---

## Ejecutar el proyecto localmente

### 1. Levantar la base de datos (PostgreSQL)

En la raíz del proyecto:

```bash
docker compose up -d
```

Esto inicia PostgreSQL en Docker.

### 2. Correr el backend

```bash
mvn spring-boot:run
```

La API quedará corriendo en:

```
http://localhost:8080
```

---

## Swagger UI

Documentación interactiva de la API:

```
http://localhost:8080/swagger-ui/index.html
```

![Swagger UI Preview](docs/swagger-preview.png)

### Usar autenticación JWT en Swagger

1. Ejecuta `POST /auth/login` o `POST /auth/register`
2. Copia el token que devuelve
3. Presiona **Authorize** en Swagger
4. Pega el token JWT

---

## Autenticación JWT

```mermaid
flowchart TD
    A([🚀 Inicio]) --> B

    B["📝 POST /auth/register - Crea tu cuenta"]
    B --> C

    C["🔑 POST /auth/login - Obtén tu JWT Token"]
    C --> D

    D(["🎫 Token en mano"])
    D --> E

    E["📋 Usa el token en cada request - Authorization: Bearer &lt;token&gt;"]
    E --> F 

    F["📌 Crear 'Board' - POST /boards"]
    F --> G & H

    G["📂 Crear 'List' - POST /boards/{:id}/lists"]
    H["✅ Crear 'Task' - POST /boards/{:id}/lists/{:id}/tasks"]

    G & H --> I

    I["🔀 Mover Task entre listas - PATCH .../tasks/{:id}/move"]
    I --> J([✨ Listo])

    style A fill:#4CAF50,color:#fff,stroke:none
    style D fill:#2196F3,color:#fff,stroke:none
    style J fill:#4CAF50,color:#fff,stroke:none
    style E fill:#FFF3CD,color:#333,stroke:#FFC107
```

---

## Usar Postman

En la carpeta `postman/` están los archivos:

- `TrackrApi.postman_collection.json`
- `Local.postman_environment.json`

Importa ambos en Postman.

---

## Endpoints principales

### Auth

```
POST /auth/register
POST /auth/login
GET  /me
```

### Boards

```
POST   /boards
GET    /boards
GET    /boards/{id}
PUT    /boards/{id}
DELETE /boards/{id}
```

### Lists

```
POST   /boards/{boardId}/lists
GET    /boards/{boardId}/lists
PUT    /boards/{boardId}/lists/{listId}
DELETE /boards/{boardId}/lists/{listId}
```

### Tasks

```
POST   /boards/{boardId}/lists/{listId}/tasks
GET    /boards/{boardId}/lists/{listId}/tasks
PUT    /boards/{boardId}/lists/{listId}/tasks/{taskId}
DELETE /boards/{boardId}/lists/{listId}/tasks/{taskId}
PATCH  /boards/{boardId}/lists/{listId}/tasks/{taskId}/move
```

---

## Tests

Para correr los tests localmente:

```bash
mvn test
```

✅ Los tests usan **H2 in-memory database**, por lo que no dependen de PostgreSQL.

---

## CI (GitHub Actions)

Cada vez que se hace un `push` o `pull request`, GitHub Actions ejecuta automáticamente:

```bash
mvn test
```

Si los tests fallan, el pipeline marca error. Esto asegura que el código siempre se mantenga estable.
