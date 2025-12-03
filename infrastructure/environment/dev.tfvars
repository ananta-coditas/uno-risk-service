# Sandbox environment variables
// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment = "dev"
environment_name  = "dev"
// ----------------------------------------- //
aws_region     = "us-west-2"
// -------- --------------------------------- //
lb_security_group_egress_rule_id = "sgr-047803515e9b08959"
// -------- --------------------------------- //
vpc_config = {
  vpc_id                 = "vpc-068d889f2d003f60d"
  public_target_subnets  = []
  private_target_subnets = ["subnet-0f3f2dee6d91d338c", "subnet-0ecef8ec24965d979", "subnet-0fba69dde222d8916", "subnet-083c8e3c16ba1e9e9"]
}
// ----------------------------------------- //
ecs_cluster_arn="arn:aws:ecs:us-west-2:034362031415:cluster/uno-dev-cluster"
ecs_cluster_name = "uno-dev-cluster"
// ----------------------------------------- //
frontend_lb_config = {
  lb_name              = "uno-dev-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-05d96445103219667"   # Security Group created by uno_shared_services repo for ALB
  logging_bucket_name  = "ambry-uno-services-dev-034362031415-bucket"
}
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //
ecs_task_role_arn = "arn:aws:iam::034362031415:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::034362031415:role/uno-risk-service-ecs-task-execution-role"
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
      container_name            = "uno-risk-service-container"
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
      required_cpu_units        = 2048  //2 GB
      required_memory_MiB_units = 4096 //4 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202511262137-a5597b7-develop-24" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:034362031415:targetgroup/uno-risk-service-8080-tg/97df65b207eef93a"
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
// ----------------------------------------- //
log_group_name = "uno/dev/ecs/uno-log-group"
namespace = "uno-dev"
// ----------------------------------------- //
load_balancer_arn = "arn:aws:elasticloadbalancing:us-west-2:034362031415:loadbalancer/app/uno-dev-elb/8bfe93ec63e21f2b"
load_balancer_type = "application"

applications_config = {
  "uno-alb" = {
    application_name = "uno-risk-service"
    frontend_config = {
      port                = 443
      protocol            = "HTTPS"
      acm_certificate_arn = "arn:aws:acm:us-west-2:034362031415:certificate/f0f8f2d2-46c5-499d-9870-7a6f69cd78ae"
      ssl_policy          = "ELBSecurityPolicy-TLS-1-1-2017-01"
    }
    backend_config = {
      port                = 8080
      protocol            = "HTTP"
      target_type         = "ip"
      acm_certificate_arn = null
      ssl_policy          = null
    }
    health_check_setting = {
      healthy_threshold_count   = 3
      unhealthy_threshold_count = 5
      timeout_secs              = 10
      interval_secs             = 60
      hc_port                   = 8080
      hc_path                   = "/risk/api/models/api-docs"
      hc_http_success_status    = "200-399"
    }
    stickiness_setting = {
      enable_stickiness   = true
      alb_stickiness_type = "app_cookie"
      app_cookie_name     = "PHPSESSID"
      alb_cookie_duration = 86400
    }
    listener_condition_config = {
      host_header_value  = ["uno-dev.ambrygen.com"]
      path_pattern_value = ["/api/risk/*", "/risk/*", "/management/health"]
    }
  },
}