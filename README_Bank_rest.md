# Bank REST API

Проект: REST-сервис для работы с банковскими картами.

---

## Требования

- Java 17+ (OpenJDK или Oracle JDK)
- Maven 3.6+
- PostgreSQL 14+ (локально или в Docker)
- Docker (опционально для базы данных)
- IDE (IntelliJ IDEA, VSCode и т.п.)

---

## Запуск базы данных PostgreSQL через Docker

1. В корне проекта выполните:

```bash
 docker compose up --build
```
2. Откройте http://localhost:8080/swagger-ui/index.html для доступа к приложению через Swagger.

3. Для остановки используйте:
```bash
 docker compose down
```