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
// ----------------------------------------- //
elb_security_groups ={
  lb_security_group_ids = ["sg-076e7c87e6699268b"]
}
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
frontend_lb_config = {
  lb_name              = "uno-test-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-076e7c87e6699268b"   # Security Group created by uno_shared_services repo for ALB
  logging_bucket_name  = ""
}
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //
ecs_task_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-ecs-task-execution-role"
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
      required_memory_MiB_units = 4096  //4 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202602092137-b93074f-develop-32" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:411233202044:targetgroup/uno-risk-service-8080-tg/1b43232d8fa98f67"
      }
      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/uno-risk-service/client/test.env"  #
        create_secret       = true
      }
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
log_group_name = "uno/test/ecs/uno-log-group"
namespace = "uno-test"
// ----------------------------------------- //
load_balancer_arn = "arn:aws:elasticloadbalancing:us-west-2:411233202044:loadbalancer/app/uno-test-elb/f2e6d54c0fb23072"
load_balancer_type = "application"
// ----------------------------------------- //
applications_config = {
  "uno-alb" = {
    application_name = "uno-risk-service"
    frontend_config = {
      port                = 443
      protocol            = "HTTPS"
      acm_certificate_arn = "arn:aws:acm:us-west-2:411233202044:certificate/7cfe279f-bf49-496a-949d-f49fc154068c"
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
      host_header_value  = ["uno-test.ambrygen.com"]
      path_pattern_value = ["/api/risk/*", "/risk/*", "/management/health"]
    }
  },
}
// ----------------------------------------- //
api_gateway_config = {
  stage_name = "test"
  routes = [
    {
      route_key           = "POST /*"
      authorization_type  = "NONE"
      target              = "integrations/{integration_id}"
    },
    {
      route_key           = "$default"
      authorization_type  = "NONE"
      target              = "integrations/{integration_id}"
    }
  ]
  cors_configuration = {
    allow_origins     = ["*"]
    allow_methods     = ["GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"]
    allow_headers     = ["*"]
    expose_headers    = []
    allow_credentials = false
    max_age           = 300
  }
  domain_name         = "api-uno-test.ambrygen.com"
  listener_arn        = "arn:aws:elasticloadbalancing:us-west-2:411233202044:listener/app/uno-test-elb/f2e6d54c0fb23072/731f9bb15d6e7f24"
  domain_name_certificate_arn = "arn:aws:acm:us-west-2:411233202044:certificate/7cfe279f-bf49-496a-949d-f49fc154068c"
  access_logging      = null
}
