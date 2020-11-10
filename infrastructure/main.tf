provider "azurerm" {
  features {}
}

locals {
  vaultName = join("-", [var.product, var.env])

  //ccpay key vault configuration
  core_product_vaultName = join("-", [var.core_product, var.env])
}

data "azurerm_key_vault" "fees_key_vault" {
  name = local.vaultName
  resource_group_name = join("-", [var.core_product, var.env])
}

data "azurerm_key_vault" "payment_key_vault" {
  name = local.core_product_vaultName
  resource_group_name = join("-", ["ccpay", var.env])
}

data "azurerm_key_vault_secret" "appinsights_instrumentation_key" {
  name = "AppInsightsInstrumentationKey"
  key_vault_id = data.azurerm_key_vault.payment_key_vault.id
}

//copy below secrets from payment app
resource "azurerm_key_vault_secret" "appinsights_instrumentation_key" {
  name  = "AppInsightsInstrumentationKey"
  value = data.azurerm_key_vault_secret.appinsights_instrumentation_key.value
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}


//copy below secrets from payment app for functional tests
data "azurerm_key_vault_secret" "freg-idam-test-user-password" {
  name = "freg-idam-test-user-password"
  key_vault_id = data.azurerm_key_vault.payment_key_vault.id
}

resource "azurerm_key_vault_secret" "freg-idam-test-user-password" {
  name  = "freg-idam-test-user-password"
  value = data.azurerm_key_vault_secret.freg-idam-test-user-password.value
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

data "azurerm_key_vault_secret" "freg-idam-generated-user-email-pattern" {
  name = "freg-idam-generated-user-email-pattern"
  key_vault_id = data.azurerm_key_vault.payment_key_vault.id
}

resource "azurerm_key_vault_secret" "freg-idam-generated-user-email-pattern" {
  name  = "freg-idam-generated-user-email-pattern"
  value = data.azurerm_key_vault_secret.freg-idam-generated-user-email-pattern.value
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

data "azurerm_key_vault_secret" "freg-idam-client-secret" {
  name = "freg-idam-client-secret"
  key_vault_id = data.azurerm_key_vault.payment_key_vault.id
}

resource "azurerm_key_vault_secret" "freg-idam-client-secret" {
  name  = "freg-idam-client-secret"
  value = data.azurerm_key_vault_secret.freg-idam-client-secret.value
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

module "fees-register-database" {
  source = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product = join("-", [var.product, "postgres-db"])
  location = var.location
  env = var.env
  postgresql_user = var.postgresql_user
  database_name = var.database_name
  sku_name = "GP_Gen5_2"
  sku_tier = "GeneralPurpose"
  common_tags     = var.common_tags
  subscription = var.subscription
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = join("-", [var.component, "POSTGRES-PASS"])
  value     = module.fees-register-database.postgresql_password
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name      = join("-", [var.component, "POSTGRES-USER"])
  value     = module.fees-register-database.user_name
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name      = join("-", [var.component, "POSTGRES-HOST"])
  value     = module.fees-register-database.host_name
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name      = join("-", [var.component, "POSTGRES-PORT"])
  value     = module.fees-register-database.postgresql_listen_port
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name      = join("-", [var.component, "POSTGRES-DATABASE"])
  value     = module.fees-register-database.postgresql_database
  key_vault_id = data.azurerm_key_vault.fees_key_vault.id
}

# Populate Vault with DB info

