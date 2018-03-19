module "fees-register-api" {
  source   = "git@github.com:hmcts/moj-module-webapp?ref=0.0.78"
  product  = "${var.product}-api"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = false

  app_settings = {
    POSTGRES_HOST     = "${module.fees-register-database.host_name}"
    POSTGRES_PORT     = "${module.fees-register-database.postgresql_listen_port}"
    POSTGRES_DATABASE = "${module.fees-register-database.postgresql_database}"
    POSTGRES_USER     = "${module.fees-register-database.user_name}"
    SPRING_DATASOURCE_USERNAME = "${module.fees-register-database.user_name}"
    SPRING_DATASOURCE_PASSWORD = "${module.fees-register-database.postgresql_password}"
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.fees-register-database.host_name}:${module.fees-register-database.postgresql_listen_port}/${module.fees-register-database.postgresql_database}"
  }
}

module "fees-register-database" {
  source              = "git@github.com:hmcts/moj-module-postgres?ref=master"
  product             = "${var.product}"
  location            = "West Europe"
  env                 = "${var.env}"
  postgresql_user   = "fradmin"
}

module "key-vault" {
  source              = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  product             = "${var.product}"
  env                 = "${var.env}"
  tenant_id           = "${var.tenant_id}"
  object_id           = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.fees-register-api.resource_group_name}"
  product_group_object_id = "38f9dea6-e861-4a50-9e73-21e64f563537"
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

