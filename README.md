# 10 Spring Boot microservices on Azure

9 CRUD services + 1 API gateway. Java 17, Spring Boot 3.2, PostgreSQL.

| Service | Local port | Base path |
|---|---|---|
| user-service | 8081 | `/api/users` |
| customer-service | 8082 | `/api/customers` |
| product-service | 8083 | `/api/products` |
| inventory-service | 8084 | `/api/inventory` |
| order-service | 8085 | `/api/orders` |
| payment-service | 8086 | `/api/payments` |
| notification-service | 8087 | `/api/notifications` |
| shipping-service | 8088 | `/api/shipments` |
| report-service | 8089 | `/api/reports` |
| api-gateway | 8080 | routes all `/api/**` paths above |

## Run locally
```bash
docker compose up --build
curl -X POST localhost:8080/api/customers -H 'Content-Type: application/json' \
  -d '{"name":"Ravi","email":"ravi@example.com","phone":"9999999999"}'
curl localhost:8080/api/customers
```

## Build one service
```bash
mvn -pl user-service -am package
java -jar user-service/target/app.jar
```

## Deploy to Azure
1. `cd infra && cp terraform.tfvars.example terraform.tfvars` (edit values)
2. `terraform init -backend-config=backend.tfvars && terraform apply`
3. In the gateway app settings add `USER_URL`, `CUSTOMER_URL`, ... (the https URLs from `terraform output app_urls`).
4. Add GitHub secrets `AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID` (OIDC) and push to `main`.

Env vars used by every service: `DB_URL`, `DB_USER`, `DB_PASSWORD` (from Key Vault), `PORT`.
