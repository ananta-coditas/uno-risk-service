// ----------------------------------------- //
data "aws_caller_identity" "current" {} # data.aws_caller_identity.current.account_id
data "aws_region" "current" {}          # data.aws_region.current.name
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
      values   = ["arn:aws:ecs:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:task/*"]
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
    resources = ["arn:aws:ssm:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:parameter/${var.organization_name}/${var.application_name}/${var.environment_name}/*"]
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
    resources = ["arn:aws:logs:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:log-group:/${var.organization_name}/${var.application_name}*"]
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
      "arn:aws:logs:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/service-connect-proxy*"
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
