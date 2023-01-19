module "ccpay-feeregister-product-core" {
  source                        = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  api_mgmt_name                 = local.api_mgmt_name
  api_mgmt_rg                   = local.api_mgmt_rg
  name                          = "feeRegister"
  product_access_control_groups = ["developers"]

}

module "ccpay-feeregister-api-core" {
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api?ref=master"

  api_mgmt_name = local.api_mgmt_name
  api_mgmt_rg   = local.api_mgmt_rg
  revision      = "1"
  service_url   = local.feeregister_api_url
  product_id    = module.ccpay-feeregister-product-core.product_id
  name          = join("-", [var.product_name, "apiList"])
  protocols             = ["http", "https"]

  display_name  = "Fee Register API"
  path          = "feeRegister-api"
  swagger_url   = "https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/specs/ccpay-payment-app.freg_api1.json"

}

data "template_file" "feeregister_policy_template-core" {
  template = file(join("", [path.module, "/template/api-policy.xml"]))
  vars = {
    allowed_certificate_thumbprints = local.feeregister_thumbprints_in_quotes_str
    s2s_client_id                   = data.azurerm_key_vault_secret.s2s_client_id.value
    s2s_client_secret               = data.azurerm_key_vault_secret.s2s_client_secret.value
    s2s_base_url                    = local.s2sUrl
  }
}


module "ccpay-feeregister-policy-core" {
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api-policy?ref=master"

  api_mgmt_name = local.api_mgmt_name
  api_mgmt_rg   = local.api_mgmt_rg

  api_name               = module.ccpay-feeregister-api-core.name
  api_policy_xml_content = data.template_file.feeregister_policy_template-core.rendered

}
resource "azurerm_api_management_user" "user_payment" {
  api_management_name = local.api_mgmt_name
  resource_group_name = local.api_mgmt_rg
  user_id             = "5731a75ae4bcd512288c690e"
  first_name          = "Anooj"
  last_name           = "Kurup"
  email               = "anooj.kurup@hmcts.net"
  state               = "active"
}

resource "azurerm_api_management_subscription" "feeregistersubscription" {
  api_management_name = local.api_mgmt_name
  resource_group_name = local.api_mgmt_rg
  user_id             = azurerm_api_management_user.user_payment.id
  product_id          = module.api_mgmt_product.product_id
  display_name        = "FeeRegistersubscription"
  state               = "active"

}

