# stage environment variables

// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment = "stg"
environment_name  = "staging"
// ----------------------------------------- //
aws_region     = "us-west-2"
// -------- --------------------------------- //
lb_security_group_egress_rule_id = "sgr-024cf8b3d96cff3f7"
// -------- --------------------------------- //
vpc_config = {
  vpc_id                 = "vpc-0e20392ac4d80ae14"
  public_target_subnets  = []
  private_target_subnets = ["subnet-05b17bd2501f225e2", "subnet-0c78b7afcac027f71", "subnet-0875f35c2e3e6f6bb", "subnet-05a235927b0d0102f"]
}
// ----------------------------------------- //
ecs_cluster_arn="arn:aws:ecs:us-west-2:480802442620:cluster/uno-stage-cluster"
ecs_cluster_name = "uno-stage-cluster"
// ----------------------------------------- //

// ----------------------------------------- //
frontend_lb_config = {
  lb_name              = "uno-staging-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-08b57c166776be5b4"   # Security Group created by uno_shared_services repo for ALB
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
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //
ecs_task_role_arn = "arn:aws:iam::480802442620:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::480802442620:role/uno-risk-service-ecs-task-execution-role"

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
      #   cw_log_group_name = "/ambry/uno-risk-service/stage/ecs/"
      #   create_log_group  = false
      #   log_group_region  = "us-west-2"
      #   mode             = "non-blocking"
      #   max_buffer_size   = "25m"
      #}
      required_cpu_units        = 512  //0.5 GB
      required_memory_MiB_units = 1024 //1 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202509301529-a7f8ab1-develop" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:480802442620:targetgroup/uno-8080-tg/69db9da71b109948"
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

log_group_name = "uno/stage/ecs/uno-log-group"
namespace = "uno-stage"
