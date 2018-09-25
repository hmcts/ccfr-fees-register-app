variable "product" {
  type    = "string"
  default = "fees-register"
}

variable "location" {
  type    = "string"
  default = "UK South"
}

variable "env" {
  type = "string"
}
variable "subscription" {
  type = "string"
}

variable "ilbIp"{}

variable "tenant_id" {}

variable "jenkins_AAD_objectId" {
  type                        = "string"
  description                 = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "microservice" {
  type = "string"
  default = "fees-register-app"
}

variable "database_name" {
  type    = "string"
  default = "fees_register"
}

variable "postgresql_user" {
  type    = "string"
  default = "fees_register"
}

// disabled liquibase temporarily - enable for new db changes build and then disable again
variable "liquibase_enabled" {
  default = "true"
}

variable "idam_api_url" {
  default = "https://preprod-idamapi.reform.hmcts.net:3511"
}

variable "capacity" {
  default = "1"
}

variable "common_tags" {
  type = "map"
}

variable "external_host_name" {
  default = "fees-register-api.nonprod.platform.hmcts.net"
}

variable "enable_fee_loader" {
  default = "false"
}

variable "frontend_url" {
  type = "string"
  default = "https://fees-register-frontend-aat.service.core-compute-aat.internal"
  description = "Optional front end URL to use for building redirect URI"
}

variable "restrict_fee_api_gw_calls_per_ip_per_minute" {
  default = "20"
}
