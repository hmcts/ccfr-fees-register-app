module "fees-register-api" {
  source   = "git@github.com:hmcts/moj-module-webapp?ref=master"
  product  = "${var.product}-api"
  location = "${var.location}"
  env      = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = false

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

