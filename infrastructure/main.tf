#------------------------------------------------------------------------------
# Microservice specific Security Group
#------------------------------------------------------------------------------
module "uno-risk-service_security_group" {
  # checkov:skip=CKV_TF_1: Referring to git tag instead of commit hash
  # checkov:skip=CKV_TF_2: Pointing to major version that Ambry controls
  # checkov:skip=CKV_AWS_382: SG need egress rule to allow connectivity to internet to install external packages
  source = "github.com/ambrygen/terraform-aws-module-security-group.git?ref=v4.0"

  identifier = local.identifier.prefix
  created_for = {
    resource_name = "uno-risk-service-ecs-sg"
    description   = "aws security group for uno risk service ecs targets"
    vpc_id        = var.vpc_config.vpc_id
  }

  ingress_config = flatten([
    for container in(var.uno-risk-service.containers_list) : [
      {
        from_port        = container.container_port
        to_port          = container.container_port
        network_protocol = "tcp"
        source_type      = "security_groups"
        source_values    = [var.frontend_lb_config.lb_security_group_id]
        description      = "Allow ingress traffic for ${container.container_name} from Load balancer SG"
      },
      {
        from_port        = container.container_port
        to_port          = container.container_port
        network_protocol = "tcp"
        source_type      = "cidr_blocks"
        source_values    = container.sg_rule_config.allowed_ingress_cidr
      },
      {
        from_port        = container.container_port
        to_port          = container.container_port
        network_protocol = "tcp"
        source_type      = "security_groups"
        source_values    = []
        refer_self_sg_id = true
        description      = "Allow ingress traffic for self security-group for inter-container communication"
      }
    ]])
}
#------------------------------------------------------------------------------
# Microservice specific Application AutoScaling Group
#------------------------------------------------------------------------------
module "uno-risk-service_autoscaling" {
  # checkov:skip=CKV_TF_1: Referring to git tag instead of commit hash
  source = "github.com/ambrygen/terraform-aws-module-app-autoscaling.git?ref=v1.0.0"

  identifier       = local.identifier
  ecs_cluster_name = var.ecs_cluster_name
  ecs_service_name = var.uno-risk-service.service_name
  autoscaling_setting = {
    max_capacity            = var.uno-risk-service.microservice_scaling_config["max_task_count"]
    min_capacity            = var.uno-risk-service.microservice_scaling_config["min_task_count"]
    cooldown_period         = 60
    policy_type             = "TargetTrackingScaling"
    metric_aggregation_type = "Maximum"
  }
  target_tracking_scaling_config = {
    metric_type               = "predefined_metric"
    target_value              = var.uno-risk-service.microservice_scaling_config["metric_target_value"]
    disable_scale_in          = false
    scale_in_cooldown_period  = 60
    scale_out_cooldown_period = 60
  }
  predefined_metric_config = {
    metric_type = var.uno-risk-service.microservice_scaling_config["metric_name"]
    elb_arn     = null
    tg_arn      = null
  }
}
# // ============================== <EOF> ============================== //