output "vaultUri" {
  value = "${data.azurerm_key_vault.fees_key_vault.vault_uri}"
}

output "idam_api_url" {
  value = "${var.idam_api_url}"
}

output "OAUTH2_CLIENT_ID" {
  value = "fees_admin_frontend"
}

output "OAUTH2_REDIRECT_URI" {
  value = "${var.frontend_url}/oauth2"
}
