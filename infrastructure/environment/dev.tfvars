# Test environment variables

// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment = "dev"
environment_name  = "dev"
// ----------------------------------------- //
aws_region     = "us-west-2"
// -------- --------------------------------- //

vpc_config = {
  vpc_id                 = "vpc-065a5cbbce57c80b6"
  public_target_subnets  = []
  private_target_subnets = ["subnet-0b8a3219ec998cac7", "subnet-00831ed25ee437e14"]
}
// ----------------------------------------- //
ecs_cluster_arn="arn:aws:ecs:us-west-2:034362031415:cluster/uno-cluster"
ecs_cluster_name = "uno-cluster"
// ----------------------------------------- //
all_tags = {
  name_prefix       = "uno"
  resource_name     = "ecs"
  environment       = "dev"
  business_owner    = "uno-team"
  company           = "ag"
  operational_unit  = "it-ops"
  compliance        = "none"
  privacy           = "sensitive"
  phi               = "no"
  pii               = "no"
  product           = "uno-risk-service"
}

// ----------------------------------------- //
frontend_lb_config = {
  lb_name              = "uno-dev-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-036abc2afdc681041"   # Security Group created by uno_shared_services repo for ALB
  logging_bucket_name  = ""
}
// ----------------------------------------- //
capacity_provider_details = {
  enable_fargate_provider = true
  enable_fargate_spot_provider = false
  ec2_asg_providers = []
  default_provider_configuration = {
    provider_name = "FARGATE"
    weight = 1
    base = 0
  }
}
// ----------------------------------------- //
# Need to update the service name and task definition - this is pending - remmove this comment once done

# aws_ecs_task_definition = {
#   "family": "uno-risk-service-task-def",
#   "networkMode": "awsvpc",
#   "requiresCompatibilities": ["FARGATE"],
#   "cpu": "256",
#   "memory": "512",
#   "executionRoleArn": "arn:aws:iam::411233202044:role/uno-risk-service-task-execution-role",
#   "containerDefinitions": [
#     {
#       "name": "risk-service-container",
#       "image": "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service:uno-risk-service-202508132148-e2ec9b7-UNO-433-infrastructure",
#       "essential": true,
#       "portMappings": [
#         {
#           "containerPort": 3000,
#           "protocol": "tcp"
#         }
#       ]
#     }
#   ]
# }
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //
ecs_task_role_arn = "arn:aws:iam::034362031415:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::034362031415:role/uno-risk-service-ecs-task-execution-role"

# uno-risk-service = {
#   containers_list : ["risk-service-container"]
#   service_name     : "uno-risk-service"
#   container_port     : 8080
#   service_type     : "FARGATE"
#   service_desired_count : 1
#   desired_task_count : 1
#   service_health_check_grace_period_seconds : 60
#   }

# container_port = 8080

// ----------------------------------------- //

uno-risk-service = {
  service_name       = "uno-risk-service"
  desired_task_count = 1
  ecs_launch_type    = "FARGATE"
  task_setting = {
    os_family                 = "LINUX"
    cpu_architecture          = "X86_64"
    required_cpu_units        = 1024 //1 GB
    required_memory_MiB_units = 2048 //2 GB
    task_volume_config = []
  }
  containers_list = [
    {
      container_name            = "uno-risk-service"
      container_port            = 8080
      host_port                 = 8080
      application_protocol      = "HTTP"
      # container_logs_setting = {
      #   log_destination   = "cloudwatch_logs"
      #   cw_log_group_name = "/ambry/uno-risk-service/test/ecs/"
      #   create_log_group  = false
      #   log_group_region  = "us-west-2"
      #   mode             = "non-blocking"
      #   max_buffer_size   = "25m"
      #}
      required_cpu_units        = 512  //0.5 GB
      required_memory_MiB_units = 1024 //1 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202509152028-77955c8-develop" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:034362031415:targetgroup/uno-8080-tg/39f46b4a3493f525"
      }
      environment_variables = null
      mount_nfs_drive = false
      essential_container = true
    }
  ]
  microservice_scaling_config = {
    min_task_count      = 1
    max_task_count      = 3
    metric_name         = "ECSServiceAverageCPUUtilization"
    metric_target_value = 80
  }
}

// ----------------------------------------- //
ecs_capacity_provider_setting = {
  enable_fargate_provider      = true
  enable_fargate_spot_provider = false
  ec2_asg_providers            = []
  default_provider_configuration = {
    provider_name = "FARGATE"
    weight        = 1
    base          = 0
  }
}

log_group_name = "uno/dev/ecs/uno-log-group"
namespace = "uno-dev"
