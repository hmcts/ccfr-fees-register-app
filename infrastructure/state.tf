terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 2.25"
    }
    azuread = {
      source  = "hashicorp/azuread"
      version = "1.6.0"
    }
  }
}

provider "azurerm" {
  alias = "cftappsdemo"
  features {}
  subscription_id = "d025fece-ce99-4df2-b7a9-b649d3ff2060"
}
