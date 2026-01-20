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
// ----------------------------------------- //
elb_security_groups ={
  lb_security_group_ids = ["sg-08b57c166776be5b4"]
}
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
    required_cpu_units        = 4096  //4 GB
    required_memory_MiB_units = 8192 //8 GB
    task_volume_config = [
      {
        volume_name = "datadog-socket"
        host_path = null    # ECS will create ephemeral storage
      }
    ]
  }
  containers_list = [
    {
      container_name            = "uno-risk-service-container"
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
      required_cpu_units        = 2048  //2 GB
      required_memory_MiB_units = 4096 //4 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "uno-risk-service-202512111918-6fdba45-develop-25" // Update this field for each ecr image build
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:480802442620:targetgroup/uno-risk-service-8080-tg/50015a9cc5e64663"
      }
      docker_labels = {
        "com.datadoghq.ad.check_names"  = "[\"uno_risk_service_http\"]"
        "com.datadoghq.ad.init_configs" = "[{}]"
        "com.datadoghq.ad.instances"    = "[{\"host\":\"%%host%%\",\"port\":8080}]"
        "com.datadoghq.ad.logs"         = "[{\"source\": \"java\", \"service\": \"uno_risk_service\"}]"
      }
      mount_nfs_drive = true
      volume_mount_point = [
        {
          container_path     = "/var/run/datadog"
          mount_as_read_only = false
          source_volume_name = "datadog-socket"
        }
      ]
      essential_container = true
      
      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/uno-risk-service/client/stage.env"  # Should contain DD_API_KEY
        create_secret       = true
      }
    },
    {
      container_name       = "dd_agent-container"
      container_port       = 8126  # Reference only (APM uses Unix socket)
      host_port            = 8126
      application_protocol = "TCP"
      required_cpu_units   = 256
      required_memory_MiB_units = 512
      ecr_image_uri        = "public.ecr.aws/datadog/agent"
      ecr_image_tag        = "latest"
      essential_container  = false  # Set to false so app can run if DD agent fails
      target_sgs           = ["sg-08b57c166776be5b4"]  # Same as your app
      fargate_assign_public_ip = false
      mount_nfs_drive = true
      volume_mount_point = [
        {
          container_path     = "/var/run/datadog"
          mount_as_read_only = false
          source_volume_name = "datadog-socket"
        }
      ]
      sg_rule_config = {
        allowed_ingress_cidr = ["10.9.32.0/20", "10.0.0.0/8"]
      }

      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/uno-risk-service/client/stage.env"  # Should contain DD_API_KEY
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

log_group_name = "uno/staging/ecs/uno-log-group"
namespace = "uno-staging"
// ----------------------------------------- //
load_balancer_arn = "arn:aws:elasticloadbalancing:us-west-2:480802442620:loadbalancer/app/uno-staging-elb/f7088d37e40cc4c5"
load_balancer_type = "application"
// ----------------------------------------- //
applications_config = {
  "uno-alb" = {
    application_name = "uno-risk-service"
    frontend_config = {
      port                = 443
      protocol            = "HTTPS"
      acm_certificate_arn = "arn:aws:acm:us-west-2:480802442620:certificate/93059c1b-dd13-491e-8576-dbd35f75aa74"
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
      host_header_value  = ["uno-stage.ambrygen.com"]
      path_pattern_value = ["/api/risk/*", "/risk/*"]
    }
  },
}
// ----------------------------------------- //
api_gateway_config = {
  stage_name = "stage"
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
  domain_name         = "api-uno-stage.ambrygen.com"
  listener_arn        = "arn:aws:elasticloadbalancing:us-west-2:480802442620:listener/app/uno-staging-elb/f7088d37e40cc4c5/2e70f4e7a5faf1f2"
  domain_name_certificate_arn = "arn:aws:acm:us-west-2:480802442620:certificate/93059c1b-dd13-491e-8576-dbd35f75aa74"
  access_logging      = null
}
