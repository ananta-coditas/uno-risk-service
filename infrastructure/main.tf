#------------------------------------------------------------------------------
# Provider configuration
#------------------------------------------------------------------------------
# provider "aws" {
#   region  = var.aws_region
#   profile = "ambry-uno-test"
# }

# profile = "ambry-uno-sandbox" # this can be set to your local AWS profile to test locally
# this can be set to your local AWS profile to test locally
# Uncomment below to use access_key and secret_key directly (Not recommended)
#access_key = var.aws_access_key
#secret_key = var.aws_secret_key

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
# task definition for uno-risk-service
#------------------------------------------------------------------------------
# module "uno-risk-service_task_definition" {
#   #source = "ambrygen/terraform-aws-module-ecs-task-definition/tree/feat-v2"
#   source = "../../terraform-aws-module-repos/terraform-aws-module-ecs-task-definition?ref=feat-v2"
#   service_name    = "uno-risk-service"
#   service_node_setting = {
#     ecs_launch_type = "FARGATE"
#     fargate_platform_version = "LATEST"
#   }

#   service_network_config = {
#     container_networking_mode = "awsvpc"
#     target_subnets = var.vpc_config.private_target_subnets
#     target_sgs = ["${module.uno-risk-service_security_group.id}"]
#     fargate_assign_public_ip = false
#   }

#   # Add other required variables here
# }



#------------------------------------------------------------------------------
# Microservice ECS Service
#------------------------------------------------------------------------------
module "uno-risk-service_ecs_service" {
  # checkov:skip=CKV_TF_1: Referring to git tag instead of commit hash
  # checkov:skip=CKV_AWS_333: assign_public_ip is set to "false" by default within core module for EC2 ecs_launch_type
  # checkov:skip=CKV_AWS_333: assign_public_ip is set to "false" by default within core module for EC2 ecs_launch_type
  source = "github.com/ambrygen/terraform-aws-module-ecs-service.git?ref=v9.1"
  #source = "../../terraform-aws-module-repos/terraform-aws-module-ecs-service-7.0/"
  ecs_cluster_arn  = var.ecs_cluster_arn
  application_name = var.application_name
  service_name     = var.uno-risk-service.service_name
  service_node_setting = {
    ecs_launch_type = var.uno-risk-service.ecs_launch_type
    capacity_provider_strategy = {
      # capacity_provider = "${var.application_name}-${var.ecs_capacity_provider_setting.default_provider_configuration.provider_name}"
      capacity_provider = "FARGATE"
      base              = var.ecs_capacity_provider_setting.default_provider_configuration.base
      weight            = var.ecs_capacity_provider_setting.default_provider_configuration.weight
    }
  }
  service_network_config = {
    container_networking_mode = "awsvpc"
    target_subnets            = var.vpc_config.private_target_subnets
    target_sgs                = [module.uno-risk-service_security_group.id]
    fargate_assign_public_ip  = false
  }
  service_deployment_setting = {
    service_deployment_controller_type = "ECS"
    enable_service_deployment_rollback = true
  }
  service_connect_settings = {
    enable_service_connect = true
    namespace              = var.namespace
  }
  service_task_setting = {
    desired_task_count        = var.uno-risk-service.desired_task_count
    task_execution_role_arn   = var.ecs_task_execution_role_arn # use module. insted var.  this need to be created
    task_role_arn             = var.ecs_task_role_arn           # use module. insted var.  - this need to be created
    cpu_units                 = var.uno-risk-service.task_setting["required_cpu_units"]
    memory_MiB_units          = var.uno-risk-service.task_setting["required_memory_MiB_units"]
    os_family                 = var.uno-risk-service.task_setting["os_family"]
    cpu_architecture          = var.uno-risk-service.task_setting["cpu_architecture"]
    enable_execute_command    = false
    rebalance_task_across_azs = true
    task_placement_constraint = []
    task_placement_strategy = []
  }
  service_task_volume_setting = [
    for volume in var.uno-risk-service.task_setting.task_volume_config : {
      volume_name               = volume.volume_name
      host_path                 = volume.host_path
      volume_configuration_type = "nfs"
    }
  ]
  containers_list = [
    for container in var.uno-risk-service.containers_list : {
      container_name           = container["container_name"]
      container_port           = container["container_port"]
      container_protocol       = null //container["application_protocol"]
      network_protocol         = "tcp"
      target_subnets           = var.vpc_config.private_target_subnets
      target_sgs               = [module.uno-risk-service_security_group.id]
      fargate_assign_public_ip = false
      ecr_image                = "${container["ecr_image_uri"]}:${container["ecr_image_tag"]}"
      cpu_units                = container["required_cpu_units"]
      memory_MiB_units         = container["required_memory_MiB_units"]
      essential_container      = container["essential_container"]
      container_logs_setting = {
        log_destination   = var.container_log_destination
        cw_log_group_name = local.cloudwatch_log_group_name
        create_log_group  = false
        log_group_region  = var.aws_region
        mode              = "non-blocking"
        max_buffer_size   = "25m"
      }
      load_balancer_target_group_arn = container.lb_config != null ? container.lb_config.target_group_arn : null
      environment_files_s3_arn       = null

      environment_variables = container.environment_variables != null ? {
        ssm_based_variables = container.environment_variables.ssm_based_variables_file != null ? jsonencode({ for pair in split("\n", trimspace(file("${path.module}${container.environment_variables.ssm_based_variables_file}"))) : trimspace(split("=", pair)[0]) => tostring(trimspace(split("=", pair)[1])) }) : jsonencode({})
        additional_secrets  = container.environment_variables.additional_secrets
        local_variables     = jsonencode({})
        create_secret       = container.environment_variables.create_secret
        } : {
        ssm_based_variables = jsonencode({})
        additional_secrets  = [jsonencode({})]
        local_variables     = jsonencode({})
        create_secret       = false
      }
      docker_labels = container.docker_labels
      volume_mount_point = container.mount_nfs_drive ? [
        for volume in var.uno-risk-service.task_setting.task_volume_config : {
          container_path     = volume.host_path
          mount_as_read_only = false
          source_volume_name = volume.volume_name
        }
      ] : []
      docker_configuration = container.docker_configuration
    }
  ]
  service_secret_path_prefix = join("/", ["/${var.organization_name}", var.application_name, var.environment_name, var.uno-risk-service.service_name, "environment_variable"])
  custom_healthcheck         = ["CMD-SHELL", "echo 'healthy' || exit 1", "cURL https://uno-test.ambrygen.com/api/risk/models/api-docs"]
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
#------------------------------------------------------------------------------
// ECS Task Role ----->>>
// Note: The ECS Task Role is an IAM role that defines the permissions for the applications running inside the ECS task, allowing them to access AWS services and resources.
//       As a best practice create different task-roles for each ecs-service/task that you define
#------------------------------------------------------------------------------
module "uno-risk-service_task_role" {
  # checkov:skip=CKV_TF_1: Referring to git tag instead of commit hash
  source = "github.com/ambrygen/terraform-aws-module-iam-role.git?ref=v2.2.0"

  iam_role_name                    = "${var.uno-risk-service.service_name}-ecs-task-role"
  iam_policy_name                  = "${var.uno-risk-service.service_name}-ecs-task-policy"
  iam_policy_description           = "IAM policy for ${var.uno-risk-service.service_name}"
  create_inline_policy             = true
  assume_role_policy_json_document = data.aws_iam_policy_document.ecs_task_assume_role_policy_document.json
  iam_policy_json_documents = [
    data.aws_iam_policy_document.microservice_task_role_policy_document.json
  ]
}
# // ============================== <EOF> ============================== //
