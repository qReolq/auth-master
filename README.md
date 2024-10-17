# auth-master

The project is a distributed system based on microservice architecture designed to manage users and their roles.

* [Endpoints](#endpoints)
* [Quick start](#quick-start)
* [Documentation](#documentation)

## Technologies
Spring(Boot, JPA, Cloud), Swagger, Docker, PostgreSQL, Liquibase, JUnit, Mockito, JaCoCo, OpenFeign

## Features
  * Users
    * Created via POST `/api/users` (register a new user).
    * Retrieved via GET `/api/users/{id}` (get user by ID).
    * Updated via PUT `/api/users/{id}` (update user information).
    * Deleted via DELETE `/api/users/{id}` (remove user by ID).
    * List of all users can be fetched via GET `/api/users`.
  * Roles:
    * Created via POST `/api/roles` (create a new role).
    * Retrieved via GET `/api/roles/{id}` (get role by ID).
    * Updated via PUT `/api/roles/{id}` (update role information).
    * Deleted via DELETE `/api/roles/{id}` (remove role by ID).
    * List of all roles can be fetched via GET `/api/roles`.
  * User-Role:
    * Roles can be assigned to users via POST `/api/users/{userId}/roles/{roleId}` (assign a role to a user).
    * Roles can be removed from users via DELETE `/api/users/{userId}/roles/{roleId}` (remove a role from a user).
    * The list of roles for a specific user can be fetched via GET `/api/users/{userId}/roles`.

## Endpoints
  * User swagger doc: http://localhost:8765/user-service/swagger-ui/index.html
  * Role swagger doc: http://localhost:8765/role-service/swagger-ui/index.html

## Environments
To run this application you need to create or fill `.env` file in root directory with next environments:

- `POSTGRES_USER_DB` - name of the Postgresql database for the "User" service.
- `POSTGRES_USER_PORT` - port number for the "User" Postgresql database (default is 5432).
- `POSTGRES_ROLE_DB` - name of the Postgresql database for the "Role" service.
- `POSTGRES_ROLE_PORT` - port number for the "Role" Postgresql database (default is 5433).
- `EUREKA_SERVER_PORT` - port number for the Eureka server (default is 8761).
- `API_GATEWAY_PORT` - port number for the API Gateway (default is 8765).

## Quick start
1. Clone this repo into folder.

```Bash
git clone https://github.com/qReolq/auth-master.git
cd auth-master
```
2. Start docker compose

```Bash
docker compose up
```
3. Go to localhost:8761

## Documentation
Docs: https://docs.google.com/document/d/18mwqaaiF7_fEWt-_RUlwnMIZRd4y_GIBbnk5jdhz8lk/edit?usp=sharing
