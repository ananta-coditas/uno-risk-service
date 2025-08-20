variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
  
}
variable "ecs_cluster_name" {
  description = "The name of the ECS cluster to deploy the service into."
  type        = string
}

variable "ecs_cluster_arn" {
  description = "The ARN of the ECS cluster."
  type        = string
} 

variable "ecs_task_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}

variable "ecs_task_execution_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}

variable "application_name" {
  description = "The name of the application."
  type        = string
  
}

variable "environment_name" {
  description = "The name of the environment."
  type        = string
  
}

variable "organization_name" {
  description = "The name of the organization."
  type        = string
  
}

# variable "aws_ecs_task_definition" {
#   description = "The ECS task definition configuration."
#   type = object({
#     family                   = string
#     networkMode              = string
#     requiresCompatibilities  = list(string)
#     cpu                      = string
#     memory                   = string
#     executionRoleArn         = string
#     taskRoleArn              = string
#     containerDefinitions     = list(object({
#       name           = string
#       image          = string
#       essential      = bool
#       portMappings   = list(object({
#         containerPort = number
#         protocol      = string
#       }))
#     }))
#   })
# }

# variable "container_port" {
#   description = "The port on which the container listens."
#   type        = number
# }

variable "container_log_destination" {
  description = "The destination for container logs."
  type        = string
  
}

variable "vpc_config" {
  description = "VPC configuration for the ECS service."
  type = object({
    vpc_id                 = string
    public_target_subnets  = list(string)
    private_target_subnets = list(string)
  })
}

variable "namespace" {
  description = "The namespace for the ECS service."
  type        = string
}

variable "uno-risk-service" {
  description = "Configuration for the uno-risk-service ECS service."
  type = object({
    service_name       = string
    desired_task_count = number
    ecs_launch_type    = string
    task_setting = object({
      os_family                 = string
      cpu_architecture          = string
      required_cpu_units        = number
      required_memory_MiB_units = number
      task_volume_config        = list(object({
        name      = string
        efs_volume_config = object({
          file_system_id = string
          root_directory = string
        })
      }))
    })
    containers_list = list(object({
      container_name            = string
      container_port            = number
      host_port                 = number
      application_protocol      = string
      # container_logs_setting = object({
      #   log_destination   = string
      #   cw_log_group_name = string
      #   create_log_group  = bool
      #   log_group_region  = string
      #   mode              = string
      #   max_buffer_size   = string
      # })
      environment_variables     = optional(object({
        ssm_based_variables_file = optional(string)
      }))
    }))
  })
  
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
