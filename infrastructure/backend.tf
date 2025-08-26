# terraform backend and state lock configuration
terraform {
  backend "s3" {
    bucket         = "uno-test-tfstate"
    key            = "infrastructure/uno-risk-service/terraform.tfstate"
    region         = "us-west-2"
    dynamodb_table = "uno-risk-service-tfstate-lock"
  }
}
