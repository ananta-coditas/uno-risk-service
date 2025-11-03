// ---------------------------------- //
// ELB Target Groups ----->>>
// ---------------------------------- //
module "elb_target_groups" {
  # checkov:skip=CKV_TF_1:Referring to git tag instead of commit hash
  for_each   = var.applications_config
  source     = "github.com/ambrygen/terraform-aws-module-target-group?ref=v1.0.0"
  providers  = { aws = aws }

  service_name          = "${each.value.application_name}-${each.value.backend_config.port}"
  elb_vpc_id            = var.vpc_config.vpc_id
  elb_arn               = var.load_balancer_arn
  elb_type              = var.load_balancer_type
  target_group_protocol = each.value.backend_config.protocol
  target_group_port     = each.value.backend_config.port
  target_type           = each.value.backend_config.target_type
  health_check_setting  = each.value.health_check_setting
  stickiness_setting    = each.value.stickiness_setting
}

// ---------------------------------- //
// Load Balancer Listener Rules ----->>>
// ---------------------------------- //
module "elb_listener_rules" {
  # checkov:skip=CKV_TF_1:Referring to git tag instead of commit hash
  source = "github.com/ambrygen/terraform-aws-module-lb-listener-rules?ref=v2.0"
  for_each   = var.applications_config
  #depends_on = [module.load_balancer, module.elb_listener]

  elb_config = {
    elb_type = var.load_balancer_type
    elb_arn  = var.load_balancer_arn
  }
  listener_config = {
    port     = each.value.frontend_config.port
    protocol = each.value.frontend_config.protocol
  }
  additional_listener_rule_config = {
    #priority_number  = index(keys(var.applications_config), each.key) + 1
    priority_number  = 20
    action_type      = "forward"
    target_group_arn = module.elb_target_groups[each.key].arn
    #   conditions_list = [
    #     {
    #       #condition_type      = "host_header"
    #       condition_type      = "path_pattern"
    #       #condition_values    = each.value.listener_condition_config.host_header_value
    #       condition_values    = each.value.listener_condition_config.path_pattern_value
    #       http_header_values  = null
    #       query_string_values = null
    #   }
    # ]
    conditions_list = concat(
      [
        {
          condition_type      = "host_header"
          condition_values    = each.value.listener_condition_config.host_header_value
          http_header_values  = null
          query_string_values = null
        }
      ],
        each.value.listener_condition_config.path_pattern_value != null ? [
        {
          condition_type      = "path_pattern"
          condition_values    = each.value.listener_condition_config.path_pattern_value
          http_header_values  = null
          query_string_values = null
        }
      ] : [] )
  }
}