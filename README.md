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
Infrastructure (App Service, Key Vault, Postgres) lives in a separate repo.
This repo only builds and deploys the apps via `.github/workflows/deploy.yml`.

Add these GitHub secrets (Settings → Secrets and variables → Actions):
`AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID`.

Each service needs env vars: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `PORT`.
The gateway also needs `USER_URL`, `CUSTOMER_URL`, `PRODUCT_URL`, `INVENTORY_URL`,
`ORDER_URL`, `PAYMENT_URL`, `NOTIFICATION_URL`, `SHIPPING_URL`, `REPORT_URL`.
