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
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.fees-register-database.host_name}:${module.fees-register-database.postgresql_listen_port}/${var.database-name}"
  }
}

module "fees-register-database" {
  source              = "git@github.com:hmcts/moj-module-postgres?ref=master"
  product             = "${var.product}"
  location            = "West Europe"
  env                 = "${var.env}"
  postgresql_user   = "fradmin"
  postgresql_database = "${var.database-name}"
}


