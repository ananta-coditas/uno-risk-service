# Test environment variables

// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment = "tst"
environment_name  = "test"
// ----------------------------------------- //
aws_region     = "us-west-2"
// -------- --------------------------------- //
lb_security_group_egress_rule_id = "sgr-0535509f00c3b756f"
// -------- --------------------------------- //
vpc_config = {
  vpc_id                 = "vpc-0c5f0396c6ed1c9ef"
  public_target_subnets  = []
  private_target_subnets = ["subnet-058cf47ade7906880", "subnet-014031f76ac71ac1e", "subnet-0505de6f45548d039", "subnet-0e9b78d432d15a044"]
}
// ----------------------------------------- //
ecs_cluster_arn="arn:aws:ecs:us-west-2:411233202044:cluster/uno-test-cluster"
ecs_cluster_name = "uno-test-cluster"
// ----------------------------------------- //

// ----------------------------------------- //
frontend_lb_config = {
  lb_name              = "uno-test-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-076e7c87e6699268b"   # Security Group created by uno_shared_services repo for ALB
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
ecs_task_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-ecs-task-execution-role"

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
    required_cpu_units        = 8192 //8 GB
    required_memory_MiB_units = 16384 //16 GB
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
      required_cpu_units        = 4096  //4 GB
      required_memory_MiB_units = 8192 //8 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202509301529-a7f8ab1-develop" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:411233202044:targetgroup/uno-8080-tg/69db9da71b109948"
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

log_group_name = "uno/test/ecs/uno-log-group"
namespace = "uno-test"
