terraform {
  required_version = ">= 1.3"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
  }
  backend "azurerm" {}   # use: terraform init -backend-config=backend.tfvars
}

provider "azurerm" {
  features {}
}

locals {
  services = toset([
    "api-gateway", "user-service", "customer-service", "product-service",
    "inventory-service", "order-service", "payment-service",
    "notification-service", "shipping-service", "report-service",
  ])
}

data "azurerm_key_vault" "kv" {
  name                = var.key_vault_name
  resource_group_name = var.key_vault_rg
}

resource "azurerm_service_plan" "plan" {
  name                = "asp-${var.product_name}-${var.environment}"
  resource_group_name = var.resource_group_name
  location            = var.location
  os_type             = "Linux"
  sku_name            = "B2"
}

resource "azurerm_linux_web_app" "svc" {
  for_each            = local.services
  name                = "app-${var.product_name}-${each.key}-${var.environment}"
  resource_group_name = var.resource_group_name
  location            = var.location
  service_plan_id     = azurerm_service_plan.plan.id
  https_only          = true

  identity {
    type = "SystemAssigned"
  }

  site_config {
    health_check_path = "/actuator/health"
    application_stack {
      java_server         = "JAVA"
      java_server_version = "17"
      java_version        = "17"
    }
  }

  app_settings = {
    WEBSITES_PORT = "8080"
    PORT          = "8080"
    DB_URL        = "jdbc:postgresql://${var.pg_host}:5432/${var.pg_database}?sslmode=require"
    DB_USER       = var.pg_username
    DB_PASSWORD   = "@Microsoft.KeyVault(SecretUri=${data.azurerm_key_vault.kv.vault_uri}secrets/${var.pg_password_secret_name}/)"
  }
}

resource "azurerm_role_assignment" "kv_read" {
  for_each             = local.services
  scope                = data.azurerm_key_vault.kv.id
  role_definition_name = "Key Vault Secrets User"
  principal_id         = azurerm_linux_web_app.svc[each.key].identity[0].principal_id
}

output "app_urls" {
  value = { for k, v in azurerm_linux_web_app.svc : k => "https://${v.default_hostname}" }
}
