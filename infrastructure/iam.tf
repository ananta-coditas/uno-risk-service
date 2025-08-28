// ---------------------------------- //
// ECS Task Execution Role ----->>>
# Note: The ECS Task Execution Role is an IAM role that allows the ECS container agent to make AWS API calls on your behalf, primarily to pull container images from ECR and send logs to CloudWatch Logs.
// ---------------------------------- //
module "task_execution_role" {
  # checkov:skip=CKV_TF_1: Referring to git tag instead of commit hash
  source = "github.com/ambrygen/terraform-aws-module-iam-role.git?ref=v2.0.0"

  iam_role_name                    = "uno-risk-service-ecs-task-execution-role"
  iam_policy_name                  = "uno-risk-service-ecs-task-execution-policy"
  iam_policy_description           = "IAM policy for ${var.application_name} ecs-task-execution role"
  create_inline_policy             = true
  assume_role_policy_json_document = data.aws_iam_policy_document.ecs_task_execution_assume_role_policy_document.json
  iam_policy_json_documents = [
    data.aws_iam_policy_document.ecs_task_execution_role_policy_document_1.json
  ]
}
// ---------------------------------- //
// EC2 Instance Profile Role ----->>>
// ---------------------------------- //
resource "aws_iam_policy" "instance_profile" {
  name        = "${local.identifier.prefix}-instance-policy"
  description = "EC2 instance profile policy"
  policy      = data.aws_iam_policy_document.ec2_instance_profile_role.json
}
// =============== <EOF> =============== //
