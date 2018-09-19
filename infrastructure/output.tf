output "vaultUri" {
  value = "${data.azurerm_key_vault.fees_key_vault.vault_uri}"
}

output "vaultName" {
  value = "${local.vaultName}"
}

output "idam_api_url" {
  value = "${var.idam_api_url}"
}

output "OAUTH2_CLIENT_ID" {
  value = "ccd_gateway"
}

output "OAUTH2_REDIRECT_URI" {
  value = "${var.frontend_url}/oauth2"
}
