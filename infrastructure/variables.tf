variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
}
// -------------------------------------------- //
variable "ecs_cluster_name" {
  description = "The name of the ECS cluster to deploy the service into."
  type        = string
}
// -------------------------------------------- //
variable "ecs_cluster_arn" {
  description = "The ARN of the ECS cluster."
  type        = string
}
// -------------------------------------------- //
variable "ecs_task_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}
// -------------------------------------------- //
variable "ecs_task_execution_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}
// -------------------------------------------- //
variable "application_name" {
  description = "The name of the application."
  type        = string
}
// -------------------------------------------- //
variable "environment_name" {
  description = "The name of the environment."
  type        = string
}
// -------------------------------------------- //
variable "organization_name" {
  description = "The name of the organization."
  type        = string

}
// -------------------------------------------- //
variable "log_group_name" {
  description = "The name of the log group."
  type = string
}
// -------------------------------------------- //
variable "lb_security_group_egress_rule_id" {
  description = "lb security group egress (outbound) rule id"
  type        = string
}
// -------------------------------------------- //
variable "environment" {
  description = "The environment the app is being deployed in"
  type        = string
}
// -------------------------------------------- //
variable "container_log_destination" {
  description = "The destination for container logs."
  type        = string

}
// -------------------------------------------- //
variable "vpc_config" {
  description = "VPC configuration for the ECS service."
  type = object({
    vpc_id                 = string
    public_target_subnets  = list(string)
    private_target_subnets = list(string)
  })
}
// -------------------------------------------- //
variable "namespace" {
  description = "The namespace for the ECS service."
  type        = string
}
// -------------------------------------------- //
variable "uno-risk-service" {
  description = "Configuration for the uno-risk-service ECS service."
  type = object({
    service_name       = string
    desired_task_count = number
    ecs_launch_type    = string
    task_setting = optional(object({
      os_family                 = string
      cpu_architecture          = string
      required_cpu_units        = number
      required_memory_MiB_units = number
      task_volume_config = optional(list(object({
        volume_name = string
        host_path   = optional(string)
      })))
    }))
    containers_list = list(object({
      container_name            = string
      container_port            = number
      host_port                 = number
      application_protocol      = string
      required_cpu_units        = number
      required_memory_MiB_units = number
      ecr_image_uri             = string
      ecr_image_tag             = string
      essential_container       = bool
      sg_rule_config = object({
        allowed_ingress_cidr = list(string)
      })
      lb_config = optional(object({
        target_group_arn = string
      }))

      environment_variables = optional(object({
        additional_secrets = optional(map(string))
        ssm_based_variables_file = optional(string)
        create_secret            = bool
        local_variables          = optional(map(string))
      }))
      docker_labels   = optional(map(any))
      mount_nfs_drive = bool
      volume_mount_point = optional(list(object({
        container_path = optional(string)
        mount_as_read_only = bool
        source_volume_name   = optional(string)
      })))
      docker_configuration = optional(object({
        commands    = optional(list(string))
        entry_point = optional(list(string))
        working_dir = optional(string)
      }))
    }))
    microservice_scaling_config = object({
      min_task_count      = number
      max_task_count      = number
      metric_name         = string
      metric_target_value = optional(number)
    })
  })
}
// ----------------------------------------- //
variable "frontend_lb_config" {
  description = "Define the configuration for the frontend load balancer for your application"
  type = object({
    lb_name              = string
    lb_type              = string
    lb_security_group_id = string
    logging_bucket_name  = string
  })
  validation {
    condition     = contains(["application", "network"], var.frontend_lb_config.lb_type)
    error_message = "Invalid input for load_balancer_type, possible values: \"application\" or \"network\"."
  }
}
// -------------------------------------------- //
variable "ecs_capacity_provider_setting" {
  description = "Configuration for ECS capacity providers."
  type = object({
    enable_fargate_provider      = bool
    enable_fargate_spot_provider = bool
    ec2_asg_providers            = list(string)
    default_provider_configuration = object({
      provider_name = string
      weight        = number
      base          = number
    })
  })
}
// -------------------------------------------- //
variable "applications_config" {
  description = "Load Balancer setting"
  type = map(object({
    application_name = string
    # application_domain = string
    frontend_config = object({
      port                = number
      protocol            = string
      acm_certificate_arn = optional(string)
      ssl_policy          = optional(string)
    })
    backend_config = object({
      port                = number
      protocol            = string
      target_type         = string
      acm_certificate_arn = optional(string)
      ssl_policy          = optional(string)
    })
    health_check_setting = object({
      healthy_threshold_count   = number
      unhealthy_threshold_count = number
      timeout_secs              = number
      interval_secs             = number
      hc_port                   = number
      hc_path                   = optional(string)
      hc_http_success_status    = optional(string)
    })
    stickiness_setting = object({
      enable_stickiness   = bool
      alb_stickiness_type = optional(string)
      app_cookie_name     = optional(string)
      alb_cookie_duration = optional(number)
    })
    listener_condition_config = object({
      host_header_value  = optional(list(string))
      path_pattern_value = optional(list(string))
    })
  }))
}
// -------------------------------------------- //
variable "load_balancer_arn" {
  description = "The ARN of the EC2 Load Balancer."
  type        = string
}
// -------------------------------------------- //
variable "load_balancer_type" {
  description = "The Type of the EC2 Load Balancer."
  type        = string
}
// -------------------------------------------- //
variable "elb_security_groups" {
  description = "ELB Security groups."
  type = object({
    lb_security_group_ids  = list(string)
  })
}
// -------------------------------------------- //
variable "api_gateway_config" {
  description = "Configuration for API Gateway HTTP API v2."
  type = object({
    stage_name = string
    routes = list(object({
      route_key         = string
      authorization_type = string
      target            = string
    }))
    cors_configuration = optional(object({
      allow_origins     = list(string)
      allow_methods     = list(string)
      allow_headers     = optional(list(string))
      expose_headers    = optional(list(string))
      allow_credentials = optional(bool)
      max_age           = optional(number)
    }))
    domain_name                = optional(string)
    domain_name_certificate_arn = optional(string)
    listener_arn = optional(string)
    access_logging = optional(object({
      destination_arn = string
      format         = string
    }))
  })
  default = {
    stage_name = "default"
    routes = []
    cors_configuration = null
    domain_name = null
    domain_name_certificate_arn = null
    access_logging = null
  }
}
// ----------------------------------------- //