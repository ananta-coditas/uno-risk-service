# ---------------------------------- #
# API Gateway Configurations
# ---------------------------------- #
data "aws_lb" "main" {
  count = var.api_gateway_config != null ? 1 : 0
  arn   = var.load_balancer_arn

  depends_on = [module.load_balancer]
}
module "api_gateway" {
  # checkov:skip=CKV_TF_1:Referring to git tag instead of commit hash
  source    = "github.com/ambrygen/terraform-aws-module-api-gateway?ref=api-gateway"
  providers = { aws = aws }

  name                    = "${local.identifier.prefix}-api-gateway"
  protocol_type           = "HTTP"  

  # Routes configuration - proxy all routes to ALB
  routes = {
    "POST /{proxy+}" = {
      #route_key         = "ANY /{proxy+}"  # Catch-all default route
      authorization_type        = "NONE"     # Or use a default value
      integration = {
        type                    = "HTTP_PROXY"
        uri                     = var.api_gateway_config.listener_arn
        method                  = "ANY"  # Changed to ANY for catch-all
        timeout_milliseconds    = 29000
        connection_type         = "VPC_LINK" 
        connection_id           = aws_apigatewayv2_vpc_link.alb[0].id 
      }
    }
  }

  # CORS configuration
  cors_configuration = var.api_gateway_config.cors_configuration

  # Custom domain for REST endpoints (optional)
  domain_name                = var.api_gateway_config.domain_name
  domain_name_certificate_arn = var.api_gateway_config.domain_name_certificate_arn

  # Stage configuration
  stage_access_log_settings = {
    stage_name = var.api_gateway_config.stage_name
    auto_deploy = true
    create_log_group            = true
    log_group_retention_in_days = 7
  }
  # Ensure load balancer and VPC link are created first
  depends_on = [aws_apigatewayv2_vpc_link.alb]
  #depends_on = [module.load_balancer]
}

#VPC Link for HTTP API Gateway v2 to connect to ALB
resource "aws_apigatewayv2_vpc_link" "alb" {
  count = var.api_gateway_config != null ? 1 : 0

  name               = "${var.application_name}-${var.environment_name}-vpc-link-v2"
  security_group_ids = [module.elb_security_group.id]
  subnet_ids         = var.vpc_config.private_elb_subnets

  tags = {
    Name = "${var.application_name}-${var.environment_name}-vpc-link-v2"
  }

  depends_on = [module.load_balancer, module.elb_security_group]
}

