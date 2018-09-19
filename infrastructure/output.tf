output "idam_api_url" {
  value = "${var.idam_api_url}"
}

output "OAUTH2_CLIENT_ID" {
  value = "ccd_gateway"
}

output "OAUTH2_REDIRECT_URI" {
  value = "${var.frontend_url}/oauth2"
}
