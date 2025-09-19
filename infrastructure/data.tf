// ----------------------------------------- //
data "aws_caller_identity" "current" {} # data.aws_caller_identity.current.account_id
data "aws_region" "current" {}          # data.aws_region.current.region
#------------------------------------------------------------------------------
# ECS Task Execution Role Policy Document(s)
#------------------------------------------------------------------------------
data "aws_iam_policy_document" "ecs_task_execution_assume_role_policy_document" {
  version = "2012-10-17"
  statement {
    sid     = "EcsTaskAssumeRole"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}
data "aws_iam_policy_document" "ecs_task_execution_role_policy_document_1" {
  # checkov:skip=CKV_AWS_356: ECR action need resource to be * to pull Service Connect Envoy Image
  version = "2012-10-17"
  statement {
    sid    = "EcrGetAuthorizationToken"
    effect = "Allow"
    actions = [
      "ecr:GetAuthorizationToken",
    ]
    resources = [
      "*" // This has to be * and there are no conditions to limit this read-only action
    ]
  }
  statement {
    sid    = "EcrServiceConnectAccess"
    effect = "Allow"
    actions = [
      "ecr:BatchCheckLayerAvailability",
      "ecr:GetDownloadUrlForLayer",
      "ecr:BatchGetImage"
    ]
    resources = [
      "*" // This has to be * to pull Service Connect Envoy Image
    ]
  }
  statement {
    sid    = "AllowEcrAccess"
    effect = "Allow"
    actions = [
      "ecr:BatchCheckLayerAvailability",
      "ecr:BatchGetImage",
      "ecr:BatchGetRepositoryScanningConfiguration",
      "ecr:Describe*",
      "ecr:Get*",
      "ecr:List*",
      "ecr:TagResource",
      "ecr:UntagResource",
      "ecr:ValidatePullThroughCacheRule",
    ]
    resources = [
      "arn:aws:ecr:us-west-2:${local.shared_services_account_id}:repository/${var.application_name}*"
    ]
  }
  statement {
    sid    = "SsmAccess"
    effect = "Allow"
    actions = [
      "ssm:AddTagsToResource",
      "ssm:DeleteParameter",
      "ssm:DeleteParameters",
      "ssm:DescribeDocumentParameters",
      "ssm:DescribeParameters",
      "ssm:GetParameter",
      "ssm:GetParameterHistory",
      "ssm:GetParameters",
      "ssm:GetParametersByPath",
      "ssm:LabelParameterVersion",
      "ssm:ListTagsForResource",
      "ssm:PutParameter",
      "ssm:RemoveTagsFromResource",
      "ssm:UnlabelParameterVersion",
    ]
    resources = ["arn:aws:ssm:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:parameter/${var.organization_name}/${var.application_name}/${var.environment_name}/*"]
  }
  statement {
    sid    = "SecretsManagerAccess"
    effect = "Allow"
    actions = [
      "secretsmanager:DescribeSecret",
      "secretsmanager:GetSecretValue",
      "secretsmanager:ListSecrets"
    ]
    resources = ["arn:aws:secretsmanager:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:secret:/${var.organization_name}/${var.application_name}/*"]
  }
  statement {
    sid    = "AllowLogsAccess"
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents",
    ]
    resources = [
      "arn:aws:logs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:log-group:/${var.log_group_name}:*"
    ]
  }
  statement {
    sid    = "accessKMS"
    effect = "Allow"
    actions = [
      "kms:Decrypt"
    ]
    resources = ["arn:aws:kms:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:key/*"]
  }
  statement {
    sid    = "ServiceConnectLogsExecution"
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:DescribeLogGroups",
      "logs:DescribeLogStreams",
      "logs:PutLogEvents"
    ]
    resources = [
      "arn:aws:logs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/service-connect-proxy*"
    ]
  }
}
#------------------------------------------------------------------------------
# ECS Task Role Policy Document(s)
#------------------------------------------------------------------------------
data "aws_iam_policy_document" "ecs_task_assume_role_policy_document" {
  version = "2012-10-17"
  statement {
    sid     = "EcsTaskAssumeRole"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
    condition {
      test     = "ArnLike"
      variable = "aws:SourceArn"
      values   = ["arn:aws:ecs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:task/*"]
    }
  }
}
data "aws_iam_policy_document" "microservice_task_role_policy_document" {
  #checkov:skip=CKV_AWS_356:EC2 Describe (readonly) actions to be performed on * resources
  #checkov:skip=CKV_AWS_111:CW needs "logs:PutRetentionPolicy"
  version = "2012-10-17"
  statement {
    sid    = "S3Access"
    effect = "Allow"
    actions = [
      "s3:AbortMultipartUpload",
      "s3:DeleteObject",
      "s3:DeleteObjectTagging",
      "s3:GetAccelerateConfiguration",
      "s3:GetObject",
      "s3:GetObjectAcl",
      "s3:GetObjectTagging",
      "s3:ListBucket",
      "s3:ListBucketMultipartUploads",
      "s3:ListBucketVersions",
      "s3:PutObject",
      "s3:PutObjectTagging",
    ]
    resources = [
      "arn:aws:s3:::${var.frontend_lb_config.logging_bucket_name}",
      "arn:aws:s3:::${var.frontend_lb_config.logging_bucket_name}/*"
    ]
  }
  statement {
    sid       = "Ec2Access"
    effect    = "Allow"
    actions   = ["ec2:Describe*"]
    resources = ["*"]
  }
  statement {
    sid    = "SsmAccess"
    effect = "Allow"
    actions = [
      "ssm:AddTagsToResource",
      "ssm:DeleteParameter",
      "ssm:DeleteParameters",
      "ssm:DescribeDocumentParameters",
      "ssm:DescribeParameters",
      "ssm:GetParameter",
      "ssm:GetParameterHistory",
      "ssm:GetParameters",
      "ssm:GetParametersByPath",
      "ssm:LabelParameterVersion",
      "ssm:ListTagsForResource",
      "ssm:PutParameter",
      "ssm:RemoveTagsFromResource",
      "ssm:UnlabelParameterVersion",
    ]
    resources = ["arn:aws:ssm:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:parameter/${var.organization_name}/${var.application_name}/${var.environment_name}/*"]
  }
  statement {
    sid    = "CloudwatchLogsAccess"
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:DescribeLogGroups",
      "logs:DescribeLogStreams",
      "logs:GetLogEvents",
      "logs:PutLogEvents",
      "logs:PutRetentionPolicy",
    ]
    resources = ["arn:aws:logs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:log-group:/${var.log_group_name}:*"]
  }
  statement {
    sid    = "ServiceConnectReadAccess"
    effect = "Allow"
    actions = [
      "servicediscovery:ListServices",
      "servicediscovery:GetOperation",
      "servicediscovery:DiscoverInstances",
      "servicediscovery:ListTagsForResource",
      "servicediscovery:ListNamespaces",
      "servicediscovery:GetNamespace",
      "servicediscovery:GetInstance",
      "servicediscovery:GetService",
      "servicediscovery:ListInstances"
    ]
    resources = ["*"] //Need * for Service Connect to work
  }
  statement {
    sid    = "ServiceConnectLogsAccess"
    effect = "Allow"
    actions = [
      "logs:CreateLogStream",
      "logs:DescribeLogGroups",
      "logs:DescribeLogStreams",
      "logs:PutLogEvents"
    ]
    resources = [
      "arn:aws:logs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/service-connect-proxy*"
    ]
  }
  statement {
    sid    = "EcsTaskProtection" //Need for graceful task shutdown
    effect = "Allow"
    actions = [
      "ecs:UpdateTaskProtection"
    ]
    resources = ["*"]
  }
}
#------------------------------------------------------------------------------
# EC2 Instance Profile Role Policy Document
#------------------------------------------------------------------------------
data "aws_iam_policy_document" "ec2_instance_profile_role" {
  #checkov:skip=CKV_AWS_356:EC2 Describe and Get (readonly) actions, and ssmmessages actions needs to be performed on * resources
  #checkov:skip=CKV_AWS_111:ssmmessages requires Write access allowed without constraint
  statement {
    effect = "Allow"
    actions = [
      "autoscaling:DescribeAutoScalingInstances",
      "ec2:DescribeInstances",
      "ec2:DescribeTags",
      "ecr:GetAuthorizationToken",
      "ssm:UpdateInstanceInformation"
    ]
    resources = ["*"]
  }
  statement {
    effect = "Allow"
    actions = [
      "ecs:CreateCluster",
      "ecs:DeregisterContainerInstance",
      "ecs:DiscoverPollEndpoint",
      "ecs:Poll",
      "ecs:RegisterContainerInstance",
      "ecs:StartTelemetrySession",
      "ecs:Submit*",
      "ecs:UpdateContainerInstancesState",
    ]
    resources = [
      "arn:aws:ecs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:cluster/${local.identifier.prefix}-cluster",
      "arn:aws:ecs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:cluster/default",
      "arn:aws:ecs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:container-instance/${local.identifier.prefix}-cluster/*",
      "arn:aws:ecs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:container-instance/default/*"
    ]
  }
  statement {
    effect = "Allow"
    actions = [
      "ecr:BatchCheckLayerAvailability",
      "ecr:BatchGetImage",
      "ecr:GetDownloadUrlForLayer",
    ]
    resources = ["arn:aws:ecr:${data.aws_region.current.region}:${local.shared_services_account_id}:repository/*"]
  }
  statement {
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:DescribeLogStreams",
      "logs:PutLogEvents",
    ]
    resources = ["arn:aws:logs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:log-group:/${var.log_group_name}:*"]
  }
  statement {
    effect = "Allow"
    actions = [
      "ssmmessages:CreateControlChannel",
      "ssmmessages:CreateDataChannel",
      "ssmmessages:OpenControlChannel",
      "ssmmessages:OpenDataChannel"
    ]
    resources = ["*"] //It needs to be *
  }
  statement {
    effect = "Allow"
    actions = [
      "servicediscovery:DeregisterInstance",
      "servicediscovery:RegisterInstance",
    ]
    resources = ["arn:aws:servicediscovery:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:service/${var.organization_name}-${var.application_name}*"]
  }
  statement {
    effect = "Allow"
    actions = [
      "ssm:AddTagsToResource",
      "ssm:GetParameter",
      "ssm:GetParameters",
      "ssm:GetParametersByPath",
      "ssm:PutParameter"
    ]
    resources = ["arn:aws:ssm:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:parameter/${var.organization_name}/${var.application_name}/${var.environment_name}/*"]
  }
  statement {
    sid    = "KmsForEbsEncryption"
    effect = "Allow"
    actions = [
      "kms:CreateGrant",
      "kms:Encrypt",
      "kms:Decrypt",
      "kms:ReEncrypt*",
      "kms:GenerateDataKey*"
    ]
    resources = [
      "arn:aws:kms:us-west-2:559000568088:key/b14ee613-a628-48e0-9c1e-f4b547af095f" # CMK Key for AMI_Mart
    ]
  }
}
// =============== <EOF> =============== //
