locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.aseName}"
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

  app_settings = {
    # db
    SPRING_DATASOURCE_USERNAME = "${module.fees-register-database.user_name}"
    SPRING_DATASOURCE_PASSWORD = "${module.fees-register-database.postgresql_password}"
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.fees-register-database.host_name}:${module.fees-register-database.postgresql_listen_port}/${module.fees-register-database.postgresql_database}?ssl=true"

    # disables liquibase run
    SPRING_LIQUIBASE_ENABLED = "${var.liquibase_enabled}"

    # idam
    IDAM_CLIENT_BASE_URL = "${var.idam_api_url}"
    # logging vars
    REFORM_SERVICE_NAME = "fees-register-api"
    REFORM_TEAM = "cc"
    REFORM_ENVIRONMENT = "${var.env}"
    ROOT_APPENDER = "JSON_CONSOLE"

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

