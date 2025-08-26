# # In addition to these required tags, you must ALSO tag all resources with `Name`, where applicable.
# # You may provide other tags at the individual resource level as well.

# # For more details on tag requirements, see https://ambrygen.atlassian.net/wiki/spaces/ITOPSDOC/pages/9434038309/Tagging

# # REQUIRED TAGS - Add values to each before deploying your Terraform code.
locals {
  business_owner    = "sbanerjee@ambrygen.com"              # Email address of the business owner GROUP. This should NOT be a named individual. It should be a distribution list email.
  company           = "ag"                                    # one of: ag, tempus
  operational_unit  = "development"                           # Used to describe the organizational unit responsible for managing the resource (eg. marketing, bioinformatics, lattice, lab, quality, etc)
  environment       = var.environment                         # Must be one of: dev, tst, stg, prd. Can be pulled dynamically from variables if you deploy to multiple environments and specify config via a tfvars file.
  compliance        = "none"                                  # Pipe-delimited string of the compliance/audit frameworks that govern the data (eg. hipaa|gdpr). Options are: none, gdpr, pci, hipaa (NOT hippa).
  product           = "uno-risk-service"                             # Name of the application or service this resource belongs to. (eg. secondary-analysis, ap, cancernext, cardio, exome, skm, tracker, monitoring, adaudit, overhead)
  disaster_recovery = "none"                                  # one of: tier1, tier2, tier3, none. tier1 is most critical.
  repo              = "https://github.com/ambrygen/uno-risk-services" # The URL of the IAC repo that contains this application's code. (eg. https://github.com/<MY_GITHUB_ORG>/i_copied_the_default_value)
  p_l_categories    = "r-d"                                   # Profit and loss categories, one of: cogs, r-d, g-a, s-m . These are, respectively: Cost of Goods and Services, Research and Development, General and Administrative, and Sales and Marketing.

  # OPTIONAL TAGS - These tags are not required globally but can be added as needed
  optional_tags = {
    # map-migrated = "migIPLS0G0ZM5" # allows AWS MAP credits to be applied to your resources

    # expiration-date = "" # TODO - What format should be used here? ISO-8601 yyyy-MM-dd?
    # tearcode        = "" # Maybe you want to include a tearcode!

    # Only required for Bio Account resources
    # run-id = "" # eg. 231109_A01310_0766_BHLLHJDRX3

    # Only required if you are deploying EC2/EBS resources
    # backup-policy = "" # one of: a-west-test-ec2-policy, a-west-staging-ec2-policy, a-west-prod-ec2-policy
  }
}

# #######################################################################
# # DON'T MODIFY BELOW THIS LINE UNLESS YOU REALLY KNOW WHAT YOU'RE DOING
# #######################################################################
module "tagging_module" {
  # checkov:skip=CKV_TF_1: commit hashing is superfluous for this case
  # For details on exact tag validation, see the repo below
  source = "github.com/ambrygen/terraform-aws-module-tagging//terraform?ref=v1.0.0"
  # Required tags
  business_owner    = local.business_owner
  company           = local.company
  operational_unit  = local.operational_unit
  environment       = local.environment
  compliance        = local.compliance
  product           = local.product
  disaster_recovery = local.disaster_recovery
  repo              = local.repo
  p_l_categories    = local.p_l_categories

  # Optional tags
  optional_tags = local.optional_tags
}
