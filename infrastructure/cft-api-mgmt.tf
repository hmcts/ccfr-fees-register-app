# Note for API docs see - https://github.com/hmcts/cnp-api-docs/tree/master/docs/specs

locals {
  cft_api_mgmt_suffix = var.apim_suffix == "" ? var.env : var.apim_suffix
  cft_api_mgmt_name   = join("-", ["cft-api-mgmt", local.cft_api_mgmt_suffix])
  cft_api_mgmt_rg     = join("-", ["cft", var.env, "network-rg"])
  cft_api_base_path   = "feeRegister-api"
}

data "template_file" "cft_policy_template" {
  template = file(join("", [path.module, "/template/cft-api-policy.xml"]))

  vars = {
    allowed_certificate_thumbprints = local.feeregister_thumbprints_in_quotes_str
    s2s_client_id                   = data.azurerm_key_vault_secret.s2s_client_id.value
    s2s_client_secret               = data.azurerm_key_vault_secret.s2s_client_secret.value
    s2s_base_url                    = local.s2sUrl
  }
}

module "cft_api_mgmt_product" {
  source                        = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  name                          = var.product_name
  api_mgmt_name                 = local.cft_api_mgmt_name
  api_mgmt_rg                   = local.cft_api_mgmt_rg
  product_access_control_groups = ["developers"]
  providers = {
    azurerm = azurerm.aks-cftapps
  }
}

module "cft_api_mgmt_api" {
  source        = "git@github.com:hmcts/cnp-module-api-mgmt-api?ref=master"
  name          = join("-", [var.product_name, "api"])
  display_name  = "Fee Register API"
  api_mgmt_name = local.cft_api_mgmt_name
  api_mgmt_rg   = local.cft_api_mgmt_rg
  product_id    = module.cft_api_mgmt_product.product_id
  path          = local.cft_api_base_path
  service_url   = local.feeregister_api_url
  swagger_url   = "https://raw.githubusercontent.com/hmcts/cnp-api-docs/master/docs/specs/ccpay-payment-app.freg_api1.json"
  protocols     = ["http", "https"]
  revision      = "1"
  providers = {
    azurerm = azurerm.aks-cftapps
  }
}

module "cft_api_mgmt_policy" {
  source                 = "git@github.com:hmcts/cnp-module-api-mgmt-api-policy?ref=master"
  api_mgmt_name          = local.cft_api_mgmt_name
  api_mgmt_rg            = local.cft_api_mgmt_rg
  api_name               = module.cft_api_mgmt_api.name
  api_policy_xml_content = data.template_file.cft_policy_template.rendered
  providers = {
    azurerm = azurerm.aks-cftapps
  }
}
