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

aws_ecs_task_definition = {
  "family": "uno-risk-service-task-def",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "executionRoleArn": "arn:aws:iam::411233202044:role/uno-risk-service-task-execution-role",
  "containerDefinitions": [
    {
      "name": "risk-service-container",
      "image": "540051198529.dkr.ecr.us-west-2.amazonaws.com/uno-risk-service:uno-risk-service-202508132148-e2ec9b7-UNO-433-infrastructure",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 3000,
          "protocol": "tcp"
        }
      ]
    }
  ]
}
// ----------------------------------------- //
container_log_destination = "cloudwatch_logs"
// ----------------------------------------- //

ecs_task_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-task-execution-role"
ecs_task_execution_role_arn = "arn:aws:iam::411233202044:role/uno-risk-service-task-execution-role"

uno-risk-service = {
  containers_list : ["risk-service-container"]
  service_name     : "uno-risk-service"
  container_port     : 8080
  service_type     : "FARGATE"
  service_desired_count : 1
  service_health_check_grace_period_seconds : 60
  }

container_port = 8080