// ====================================== //
locals {
  identifier = {
    prefix = lower("${var.organization_name}-${var.application_name}-${var.environment_name}")
    suffix = ""
  }
  // -------------------------------//
  cloudwatch_log_group_name = "/${var.organization_name}/${var.application_name}/${var.environment_name}/ecs/"
  // -------------------------------//
  #shared_services_account_id = 540051198529
}
// ====================================== //
