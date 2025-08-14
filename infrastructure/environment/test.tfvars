# Test environment variables

// ----------------------------------------- //
organization_name = "ambry"
application_name  = "uno-risk-service"
environment_name  = "test"
// ----------------------------------------- //
aws_region     = "us-west-2"
// ----------------------------------------- //

vpc_config = {
  vpc_id                 = "vpc-0c5f0396c6ed1c9ef" 
  public_target_subnets  = []
  private_target_subnets = ["subnet-058cf47ade7906880", "subnet-014031f76ac71ac1e", "subnet-0505de6f45548d039", "subnet-0e9b78d432d15a044"]
}
// ----------------------------------------- //
ecs_cluster="arn:aws:ecs:us-west-2:411233202044:cluster/uno-test-cluster"
// ----------------------------------------- //
all_tags = {
    name_prefix       = "uno"
    resource_name     = "ecs"
    environment       = "test"
    business_owner    = "uno-team"
    company           = "ag"
    operational_unit  = "it-ops"
    compliance        = "none"
    privacy           = "sensitive"
    phi               = "no"
    pii               = "no"
    product           = "uno"
}

// ----------------------------------------- //
# frontend_lb_config = {
#   lb_name              = "evoapps-ecs-lb-int-elb"
#   lb_type              = "application"
#   lb_security_group_id = "sg-009a0b5d06692f1b4"
#   logging_bucket_name  = "lblogs-011547835955"
# }
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

uno-risk-service-td = {
  service_name       = "uno-risk-service"
  desired_task_count = 1
  ecs_launch_type    = "EC2"
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
      container_port            = 8000
      host_port                 = 8000
      application_protocol      = "HTTP"
      required_cpu_units        = 512  //0.5 GB
      required_memory_MiB_units = 1024 //1 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service"
      ecr_image_tag             = "latest" // Update this tag as needed from external
      sg_rule_config = {
        allowed_ingress_cidr = ["10.6.144.0/20"]
      }
      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/api/api-core/int.env"
        create_secret            = true
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
nginx_microservice = {
  service_name       = "nginx"
  desired_task_count = 1
  ecs_launch_type    = "EC2"
  task_setting = {
    os_family                 = "LINUX"
    cpu_architecture          = "X86_64"
    required_cpu_units        = 1024 //1 GB
    required_memory_MiB_units = 2048 //2 GB
    task_volume_config = []
  }
  containers_list = [
    {
      container_name            = "nginx-core"
      container_port            = 80
      host_port                 = 80
      application_protocol      = "HTTP"
      required_cpu_units        = 512  //0.5 GB
      required_memory_MiB_units = 1024 //1 GB
      ecr_image_uri             = "540051198529.dkr.ecr.us-west-2.amazonaws.com/sample-kits-nginx"
      ecr_image_tag             = "nginx-202508112203-480ce2b-develop"
      sg_rule_config = {
        allowed_ingress_cidr = ["10.6.144.0/20"]
      }
      lb_config = {
        target_group_arn = "arn:aws:elasticloadbalancing:us-west-2:011547835955:targetgroup/sample-kits-nginx-8081-tg/8dea4842696d5e44"
      }
      environment_variables = {
        ssm_based_variables_file = "/environment/env_vars/nginx/nginx-core/int.env"
        create_secret            = true
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
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //