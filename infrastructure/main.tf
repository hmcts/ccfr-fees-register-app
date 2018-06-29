locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.aseName}"

  previewVaultName = "${var.product}-api"
  nonPreviewVaultName = "${var.product}-api-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  nonPreviewVaultUri = "${module.key-vault.key_vault_uri}"
  previewVaultUri = "https://fees-regsiter-api-aat.vault.azure.net/"
  vaultUri = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultUri : local.nonPreviewVaultUri}"
}

module "fees-register-api" {
  source   = "git@github.com:hmcts/moj-module-webapp?ref=master"
  product  = "${var.product}-api"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = false
  capacity = "${var.capacity}"

  app_settings = {
    # db
    SPRING_DATASOURCE_USERNAME = "${module.fees-register-database.user_name}"
    SPRING_DATASOURCE_PASSWORD = "${module.fees-register-database.postgresql_password}"
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.fees-register-database.host_name}:${module.fees-register-database.postgresql_listen_port}/${module.fees-register-database.postgresql_database}?ssl=true"
    # idam
    IDAM_CLIENT_BASE_URL = "${var.idam_api_url}"
  }
}

module "fees-register-database" {
  source = "git@github.com:hmcts/moj-module-postgres?ref=master"
  product = "${var.product}-postgres-db"
  location = "${var.location}"
  env = "${var.env}"
  postgresql_user = "${var.postgresql_user}"
  database_name = "${var.database_name}"
  sku_name = "GP_Gen5_2"
  sku_tier = "GeneralPurpose"
}

module "key-vault" {
  source              = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  name                = "${local.vaultName}"
  product             = "${var.product}"
  env                 = "${var.env}"
  tenant_id           = "${var.tenant_id}"
  object_id           = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.fees-register-api.resource_group_name}"
  # group id of dcd_reform_dev_azure
  product_group_object_id = "56679aaa-b343-472a-bb46-58bbbfde9c3d"
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name      = "fees-register-POSTGRES-USER"
  value     = "${module.fees-register-database.user_name}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = "fees-register-POSTGRES-PASS"
  value     = "${module.fees-register-database.postgresql_password}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name      = "fees-register-POSTGRES-HOST"
  value     = "${module.fees-register-database.host_name}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name      = "fees-register-POSTGRES-PORT"
  value     = "${module.fees-register-database.postgresql_listen_port}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name      = "fees-register-POSTGRES-DATABASE"
  value     = "${module.fees-register-database.postgresql_database}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

