variable "product" {}

variable "component" {}

variable "location" {
  default = "UK South"
}

variable "env" {}

variable "subscription" {}

variable "ilbIp"{}

variable "tenant_id" {}

variable "jenkins_AAD_objectId" {
  description                 = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "appinsights_instrumentation_key" {
  description = "Instrumentation key of the App Insights instance this webapp should use. Module will create own App Insights resource if this is not provided"
  default = ""
}

variable "database_name" {
  default = "fees_register"
}

variable "additional_databases" {
  default = []
}

variable "postgresql_user" {
  default = "fees_register"
}

variable "common_tags" {
  type = map(string)
}

variable "product_name" {
  type    = string
  default = "feeregister"

}

variable "feeregister_api_gateway_certificate_thumbprints" {
  type    = list(any)
  default = [] # TODO: remove default and provide environment-specific values
}

variable "aks_subscription_id" { }

variable "core_product" {
  default = "ccpay"
}

variable "postgresql_version" {
  default = "11"
}
