variable "product_name"        { type = string }
variable "environment"         { type = string }
variable "resource_group_name" { type = string }
variable "location"            { type = string }
variable "key_vault_name"      { type = string }
variable "key_vault_rg"        { type = string }
variable "pg_host"             { type = string }
variable "pg_database" {
  type    = string
  default = "postgres"
}
variable "pg_username"         { type = string }
variable "pg_password_secret_name" {
  type    = string
  default = "pg-password"
}
