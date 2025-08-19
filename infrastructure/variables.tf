variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
  
}
variable "ecs_cluster_name" {
  description = "The name of the ECS cluster to deploy the service into."
  type        = string
  default     = "uno-test-cluster"
}

variable "ecs_task_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}

variable "ecs_task_execution_role_arn" {
  description = "The ARN of the ECS task execution role."
  type        = string
}

variable "aws_ecs_task_definition" {
  description = "The ECS task definition configuration."
  type = object({
    family                   = string
    networkMode              = string
    requiresCompatibilities  = list(string)
    cpu                      = string
    memory                   = string
    executionRoleArn         = string
    taskRoleArn              = string
    containerDefinitions     = list(object({
      name           = string
      image          = string
      essential      = bool
      portMappings   = list(object({
        containerPort = number
        protocol      = string
      }))
    }))
  })
}

variable "container_port" {
  description = "The port on which the container listens."
  type        = number
}