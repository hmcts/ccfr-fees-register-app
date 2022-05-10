module "ccpay-feeregister-product" {
  source                        = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  api_mgmt_name                 = local.api_mgmt_name_cft
  api_mgmt_rg                   = local.api_mgmt_rg_cft
  name                          = "feeRegister"
  product_access_control_groups = ["developers"]

  providers = {
    azurerm = azurerm.cftappsdemo
  }
}

module "ccpay-feeregister-api" {
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api?ref=master"

  api_mgmt_name = local.api_mgmt_name_cft
  api_mgmt_rg   = local.api_mgmt_rg_cft
  revision      = "1"
  service_url   = local.feeregister_api_url
  product_id    = module.ccpay-feeregister-product.product_id
  name          = join("-", [var.product_name, "apiList"])
  protocols             = ["http", "https"]

  display_name  = "Fee Register API"
  path          = "feeRegister-api"
  swagger_url   = "https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/specs/ccpay-payment-app.freg_api1.json"

  providers = {
    azurerm = azurerm.cftappsdemo
  }
}

data "template_file" "feeregister_policy_template" {
  template = file(join("", [path.module, "/template/api-policy.xml"]))
  vars = {
    allowed_certificate_thumbprints = local.feeregister_thumbprints_in_quotes_str
    s2s_client_id                   = data.azurerm_key_vault_secret.s2s_client_id.value
    s2s_client_secret               = data.azurerm_key_vault_secret.s2s_client_secret.value
    s2s_base_url                    = local.s2sUrl
  }
}


module "ccpay-feeregister-policy" {
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api-policy?ref=master"

  api_mgmt_name = local.api_mgmt_name_cft
  api_mgmt_rg   = local.api_mgmt_rg_cft

  api_name               = module.ccpay-feeregister-api.name
  api_policy_xml_content = data.template_file.feeregister_policy_template.rendered

  providers = {
    azurerm = azurerm.cftappsdemo
  }
}

data "azurerm_api_management_product" "feeregister" {
  product_id          = module.ccpay-feeregister-product.product_id
  api_management_name = local.api_mgmt_name_cft
  resource_group_name = local.api_mgmt_rg_cft
  provider            = azurerm.cftappsdemo
}



data "azurerm_api_management_user" "fee_user" {
  user_id             = "5931a75ae4bbd512288c680b"
  api_management_name = local.api_mgmt_name_cft
  resource_group_name = local.api_mgmt_rg_cft
  provider            = azurerm.cftappsdemo
}


resource "azurerm_api_management_subscription" "fee_subs" {
  api_management_name = local.api_mgmt_name_cft
  resource_group_name = local.api_mgmt_rg_cft
  user_id             = data.azurerm_api_management_user.fee_user.id
  product_id          = data.azurerm_api_management_product.feeregister.id
  display_name        = "FeeReg Subscription"
  state               = "active"
  provider            = azurerm.cftappsdemo
}




resource "azurerm_api_management_user" "feeReg_Liberata" {
  api_management_name = local.api_mgmt_name_cft
  resource_group_name = local.api_mgmt_rg_cft
  user_id             = "5931a75ae4bbd512288c690c"
  first_name          = "Liberata"
  last_name           = "MOS"
  email               = "BPA.Developments@liberata.com"
  state               = "active"

  provider = azurerm.cftappsdemo
}

resource "azurerm_api_management_subscription" "feeReg_subscription_Liberata" {
  api_management_name = local.api_mgmt_name_cft
  resource_group_name = local.api_mgmt_rg_cft
  user_id             = azurerm_api_management_user.feeReg_Liberata.id
  product_id          = data.azurerm_api_management_product.feeregister.id
  display_name        = "FeeRegister Subscription Liberata"
  state               = "active"

  provider = azurerm.cftappsdemo
}



  resource "azurerm_api_management_user" "FREG_John" {
    api_management_name = local.api_mgmt_name_cft
    resource_group_name = local.api_mgmt_rg_cft
    user_id             = "5831a75ae4bbd512288c681c"
    first_name          = "Pettedson"
    last_name           = "John"
    email               = "Pettedson.John@HMCTS.NET"
    state               = "active"

    provider = azurerm.cftappsdemo
  }

  resource "azurerm_api_management_subscription" "FREG_john" {
    api_management_name = local.api_mgmt_name_cft
    resource_group_name = local.api_mgmt_rg_cft
    user_id             = azurerm_api_management_user.FREG_John.id
    product_id          = data.azurerm_api_management_product.feeregister.id
    display_name        = "FREG Subscription John"
    state               = "active"

    provider = azurerm.cftappsdemo
  }

}
