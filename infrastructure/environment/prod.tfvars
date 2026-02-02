# prod environment variables

// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment = "prd"
environment_name  = "prod"
// ----------------------------------------- //
aws_region     = "us-west-2"
// -------- --------------------------------- //
lb_security_group_egress_rule_id = "sgr-00d44bba603a0e6cf"
// ----------------------------------------- //
elb_security_groups ={
  lb_security_group_ids = ["sg-086b0b26318069458"]
}
// -------- --------------------------------- //
vpc_config = {
  vpc_id                 = "vpc-0fe63812d82fa70c4"
  public_target_subnets  = []
  private_target_subnets = ["subnet-06c399b206da86421", "subnet-06a2baf1f8b602ba5", "subnet-0a8fc370338e8feeb", "subnet-0fc93ec80dfff1613"]
}
// ----------------------------------------- //
ecs_cluster_arn="arn:aws:ecs:us-west-2:658178292836:cluster/uno-prod-cluster"
ecs_cluster_name = "uno-prod-cluster"
// ----------------------------------------- //

// ----------------------------------------- //
frontend_lb_config = {
  lb_name              = "uno-prod-elb"           # Load Balancer created by uno_shared_services repo
  lb_type              = "application"
  lb_security_group_id = "sg-086b0b26318069458"   # Security Group created by uno_shared_services repo for ALB
  logging_bucket_name  = ""
}
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //
ecs_task_role_arn = "arn:aws:iam::658178292836:role/uno-risk-service-ecs-task-role"
ecs_task_execution_role_arn = "arn:aws:iam::658178292836:role/uno-risk-service-ecs-task-execution-role"

// ----------------------------------------- //

uno-risk-service = {
  service_name       = "uno-risk-service"
  desired_task_count = 1
  ecs_launch_type    = "FARGATE"
  task_setting = {
    os_family                 = "LINUX"
    cpu_architecture          = "X86_64"
    required_cpu_units        = 8192  //8 GB
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
      #   cw_log_group_name = "/ambry/uno-risk-service/prod/ecs/"
      #   create_log_group  = false
      #   log_group_region  = "us-west-2"
      #   mode             = "non-blocking"
      #   max_buffer_size   = "25m"
      #}
      required_cpu_units        = 4096  //4 GB
      required_memory_MiB_units = 8192 //8 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202601221527-65f82ba-develop-27" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:658178292836:targetgroup/uno-risk-service-8080-tg/5bf65d322348fd35"
      }
      docker_labels = {
        # Logs configuration
        "com.datadoghq.ad.logs" = "[{\"source\": \"java\", \"service\": \"uno-risk-service\"}]"
      
        # Multiple checks
        "com.datadoghq.ad.check_names" = "[\"http_check\", \"prometheus\"]"
        "com.datadoghq.ad.init_configs" = "[{}, {}]"
        "com.datadoghq.ad.instances" = "[{\"name\": \"uno_risk_service_health\", \"url\": \"http://localhost:8080/risk/management/health\", \"timeout\": 5, \"method\": \"GET\", \"tags\": [\"env:prod\", \"service:uno-risk-service\"]}, {\"prometheus_url\": \"http://localhost:8080/management/prometheus\", \"namespace\": \"uno_risk_service\", \"metrics\": [\"*\"], \"tags\": [\"env:prod\", \"service:uno-risk-service\"]}]"
      }
      mount_nfs_drive = false
      essential_container = true
      
      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/uno-risk-service/client/prod.env"  # Should contain DD_API_KEY
        create_secret       = true
      }
    },
    {
      container_name       = "dd_agent-container"
      container_port       = 8126   # Reference only (APM uses Unix socket)
      host_port            = 8126
      application_protocol = "TCP"
      required_cpu_units   = 256
      required_memory_MiB_units = 512
      ecr_image_uri        = "public.ecr.aws/datadog/agent"
      ecr_image_tag        = "latest"
      essential_container  = false  # Set to false so app can run if DD agent fails
      target_sgs           = ["sg-086b0b26318069458"]  # Same as your app
      fargate_assign_public_ip = false
      mount_nfs_drive = false
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }

      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/uno-risk-service/client/prod.env"  # Should contain DD_API_KEY
        create_secret       = true
      }
      
      docker_labels = {}
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

log_group_name = "uno/prod/ecs/uno-log-group"
namespace = "uno-prod"
// ----------------------------------------- //
load_balancer_arn = "arn:aws:elasticloadbalancing:us-west-2:658178292836:loadbalancer/app/uno-prod-elb/e7feb74b017a1629"
load_balancer_type = "application"
// ----------------------------------------- //
applications_config = {
  "uno-alb" = {
    application_name = "uno-risk-service"
    frontend_config = {
      port                = 443
      protocol            = "HTTPS"
      acm_certificate_arn = "arn:aws:acm:us-west-2:658178292836:certificate/2527acc3-64a2-464f-bb47-a53640b55df3"
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
      host_header_value  = ["ras.ambrygen.com"]
      path_pattern_value = ["/api/risk/*", "/risk/*"]
    }
  },
}
// ----------------------------------------- //
api_gateway_config = {
  stage_name = "prod"
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
  domain_name         = "api-uno-prod.ambrygen.com"
  listener_arn        = "arn:aws:elasticloadbalancing:us-west-2:658178292836:listener/app/uno-prod-elb/e7feb74b017a1629/6bca251544ad1dce"
  domain_name_certificate_arn = "arn:aws:acm:us-west-2:658178292836:certificate/2527acc3-64a2-464f-bb47-a53640b55df3"
  access_logging      = null
}
