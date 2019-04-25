provider "azurerm" {
  version = "1.19.0"
}

locals {
  app_full_name = "${var.product}-${var.component}"
  aseName = "core-compute-${var.env}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.aseName}"

  previewVaultName = "${var.core_product}-aat"
  nonPreviewVaultName = "${var.core_product}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  #region API gateway
  api_policy = "${replace(file("template/api-policy.xml"), "CAllS_PER_IP_PER_MINUTE", var.restrict_fee_api_gw_calls_per_ip_per_minute)}"
  api_base_path = "fees-api"
  #endregion

  asp_name = "${var.env == "prod" ? "fees-register-api-prod" : "${var.core_product}-${var.env}"}"
}

data "azurerm_key_vault" "fees_key_vault" {
  name = "${local.vaultName}"
  resource_group_name = "${var.core_product}-${local.local_env}"
}

data "azurerm_key_vault_secret" "appinsights_instrumentation_key" {
  name = "AppInsightsInstrumentationKey"
  vault_uri = "${data.azurerm_key_vault.fees_key_vault.vault_uri}"
}

module "fees-register-api" {
  source   = "git@github.com:hmcts/moj-module-webapp?ref=master"
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
  source = "git@github.com:hmcts/moj-module-postgres?ref=master"
  product = "${var.product}-postgres-db"
  location = "${var.location}"
  env = "${var.env}"
  postgresql_user = "${var.postgresql_user}"
  database_name = "${var.database_name}"
  sku_name = "GP_Gen5_2"
  sku_tier = "GeneralPurpose"
  common_tags     = "${var.common_tags}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = "${local.app_full_name}-POSTGRES-PASS"
  value     = "${module.fees-register-database.postgresql_password}"
  vault_uri = "${data.azurerm_key_vault.fees_key_vault.vault_uri}"
}

# region API (gateway)
data "template_file" "api_template" {
  template = "${file("${path.module}/template/api.json")}"
}

resource "azurerm_template_deployment" "api" {
  template_body       = "${data.template_file.api_template.rendered}"
  name                = "${var.product}-api-${var.env}"
  deployment_mode     = "Incremental"
  resource_group_name = "core-infra-${var.env}"
  count               = "${var.env != "preview" ? 1: 0}"

  parameters = {
    apiManagementServiceName  = "core-api-mgmt-${var.env}"
    apiName                   = "${var.product}-api"
    apiProductName            = "${var.product}"
    serviceUrl                = "http://${var.product}-api-${var.env}.service.core-compute-${var.env}.internal"
    apiBasePath               = "${local.api_base_path}"
    policy                    = "${local.api_policy}"
  }
}
# endregion

# Populate Vault with DB info

