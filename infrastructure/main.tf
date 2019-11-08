provider "azurerm" {
  version = "1.22.1"
}

locals {
  app_full_name = "${var.product}-${var.component}"
  aseName = "core-compute-${var.env}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.aseName}"

  previewVaultName = "${var.product}-aat"
  nonPreviewVaultName = "${var.product}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  //ccpay key vault configuration
  core_product_previewVaultName = "${var.core_product}-aat"
  core_product_nonPreviewVaultName = "${var.core_product}-${var.env}"
  core_product_vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.core_product_previewVaultName : local.core_product_nonPreviewVaultName}"

  asp_name = "${var.env == "prod" ? "fees-register-api-prod" : "${var.product}-${var.env}"}"

  sku_size = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? "I2" : "I1"}"
}

data "azurerm_key_vault" "fees_key_vault" {
  name = "${local.vaultName}"
  resource_group_name = "${var.product}-${local.local_env}"
}

data "azurerm_key_vault" "payment_key_vault" {
  name = "${local.core_product_vaultName}"
  resource_group_name = "ccpay-${var.env}"
}

data "azurerm_key_vault_secret" "appinsights_instrumentation_key" {
  name = "AppInsightsInstrumentationKey"
  key_vault_id = "${data.azurerm_key_vault.payment_key_vault.id}"
}

resource "azurerm_key_vault_secret" "appinsights_instrumentation_key" {
  name = "AppInsightsInstrumentationKey"
  value "${data.azurerm_key_vault_secret.appinsights_instrumentation_key.value}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

module "fees-register-api" {
  source   = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product  = "${var.product}-api"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = "${var.env != "preview" ? 1: 0}"
  additional_host_name = "${var.env != "preview" ? var.external_host_name : "null"}"
  https_only="false"
  capacity = "${var.capacity}"
  common_tags     = "${var.common_tags}"
  appinsights_instrumentation_key = "${data.azurerm_key_vault_secret.appinsights_instrumentation_key.value}"
  asp_name = "${local.asp_name}"
  asp_rg = "${local.asp_name}"
  instance_size = "${local.sku_size}"

  app_settings = {
    # db
    SPRING_DATASOURCE_USERNAME = "${module.fees-register-database.user_name}"
    SPRING_DATASOURCE_PASSWORD = "${module.fees-register-database.postgresql_password}"
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.fees-register-database.host_name}:${module.fees-register-database.postgresql_listen_port}/${module.fees-register-database.postgresql_database}?sslmode=require"

    # disabled liquibase at startup as there is a separate pipleline step (enableDbMigration)
    SPRING_LIQUIBASE_ENABLED = "false"

    # idam
    IDAM_CLIENT_BASE_URL = "${var.idam_api_url}"
    # logging vars
    REFORM_SERVICE_NAME = "fees-register-api"
    REFORM_TEAM = "cc"
    REFORM_ENVIRONMENT = "${var.env}"

    # enable fee loader
    ENABLE_FEE_LOADER = "${var.enable_fee_loader}"
  }
}

module "fees-register-database" {
  source = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product = "${var.product}-postgres-db"
  location = "${var.location}"
  env = "${var.env}"
  postgresql_user = "${var.postgresql_user}"
  database_name = "${var.database_name}"
  sku_name = "GP_Gen5_2"
  sku_tier = "GeneralPurpose"
  common_tags     = "${var.common_tags}"
  subscription = "${var.subscription}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = "${var.component}-POSTGRES-PASS"
  value     = "${module.fees-register-database.postgresql_password}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name      = "${var.component}-POSTGRES-USER"
  value     = "${module.fees-register-database.user_name}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name      = "${var.component}-POSTGRES-HOST"
  value     = "${module.fees-register-database.host_name}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name      = "${var.component}-POSTGRES-PORT"
  value     = "${module.fees-register-database.postgresql_listen_port}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name      = "${var.component}-POSTGRES-DATABASE"
  value     = "${module.fees-register-database.postgresql_database}"
  key_vault_id = "${data.azurerm_key_vault.fees_key_vault.id}"
}

# Populate Vault with DB info

