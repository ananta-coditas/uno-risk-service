# UNO Microservice: Risk Service
This repository contains the infrastructure for Risk Service with CI/CD pipelines to build, scan, and deploy Docker image to AWS ECS.

## Dependencies
Resources shared with other UNO microservices located in uno-shared-services repository.

## AWS Resources
This Repository utilizes the following AWS resources:
- UNO Shared Load Balancer(s) -- Configurable by UNO-Shared-Services
    - Target-Group(s)
    - Multiple Listeners
- ECS Cluster created by UNO Shared Services
- S3 bucket(s) -- which stores ELB's application-logs
- Security Group(s)
- IAM Roles and Policies

Note: The core-module further uses several submodules to create the required infrastructure

## File Structure
```
.
└── pipelines
│   └── workflows
│       ├── infrastructure
│       │   └── .env.<environment>
|       ├── docker-build-push.yaml
│       └── terraform.yaml
└── .mvn
│   └── <mvn wrapper files>
└── dependencies (will be moved to uno-risk-objects repo)
│   └── <risk model jar files>
└── infrastructure
│   ├── environment
│   │   └── <environment>.tfvars
│   ├── backend.j2
│   ├── data.tf
│   ├── iam.tf
│   ├── locals.tf
│   ├── main.tf
│   ├── outputs.tf
│   ├── providers.tf
│   ├── required_tags.tf
│   └── variables.tf
├── src
│   └── main
│   └── test
├── .editorconfig
├── .gitattributes
├── .gitignore
├── .prettierignore
├── .prettierrc
├── Dockerfile
├── checkstyle.xml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── sonar-project.properties
└── README.md

{Note: <environment> here could be int, test, stage, prod}
```

- `main.tf`: Contains the main Terraform configuration for ECS Service, Security Group, and associated resources.
- `variables.tf`: Defines all the input variables used in the module configuration.
- `locals.tf`: Defines local variables and computed values used throughout the module.
- `data.tf`: Contains data source definitions and IAM policy documents.
- `backend.j2`: Save state lock configuration for terraform backend.
- `providers.tf`: Template for generating the provider configuration.
- `README.md`: This file, containing documentation for the module.
- `outputs.tf`: TBD


## Infrastructure Deployment

This project uses GitHub Actions for automated infrastructure deployment through a reusable workflow pattern. The deployment workflow is triggered on:

- Push events to `release/**/*` branches
- Pull request events to `release/**/*` branches
- Manual workflow dispatch with configurable options

### Workflow Configuration

The workflow supports four deployment environments with sequential execution:

1. **Sandbox Environment**
   - Uses `environment/dev.tfvars` and `.env.dev`
   - Runs on all triggers

2. **Test Environment**
   - Uses `environment/test.tfvars` and `.env.test`
   - Runs on all triggers
  
3. **Stage Environment**
   - Uses `environment/stage.tfvars` and `.env.stage`
   - Requires successful test deployment
   
4. **Production Environment**
   - Uses `environment/prod.tfvars` and `.env.prod`
   - Requires successful test and stage deployments

### Manual Execution Options

The workflow can be triggered manually with the following parameters:
- **terraform_action**: Choice between `apply`, `plan`, or `destroy`
- **target_environment**: Choice of  `sandbox`, `test`, `stage`, or `prod`

### Prerequisites

- Appropriate IAM roles and permissions via Role Vending Machine
- Load Balancer and ECS Cluster created by UNO Shared Services

Note: Ensure all sensitive information is stored as GitHub secrets and never committed directly to the repository.

## Workflow Runs
Navigate to Actions tab, and select one of the following workflows:
- **Build, Scan, and Push Container Image**: Builds, scans, and pushes Docker Image to ECR in AWS Shared Infra Account
- **Create Branch: Release**: Creates a new release branch from an existing remote branch
- **Deploy Container Image to ECS Fargate**: Deploys Docker Image to ECS

## Running the Risk Server
Once the risk service has been deployed to ECS, it is accessible within Ambry VPN via cURL command:
- URL: https://uno-<env>.ambrygen.com
- Endpoint: /api/risk/models/calculate
- Content-Type: application/json 

## Requirements

| Name | Version |
|------|---------|
| <a name="requirement_terraform"></a> [terraform](#requirement\_terraform) | >= 1.0 |
| <a name="requirement_aws"></a> [aws](#requirement\_aws) | >= 5.26 |


## Change Log

- v1.0.0 (Aug 29, 2025) -- by Samuel H., Soundar M., Jason W., and Pradip K. : Set up required infrastructure and deployed Image to ECS Fargate
- v1.0.1 (Sep 02, 2025) -- by Samuel H. : Modified README.md
- v1.0.2 (Oct 29, 2025) -- by Samuel H. : Updated README.md

## License
© 2025 Amazon Web Services, Inc. or its affiliates. All Rights Reserved.
This work is licensed under a Creative Commons Attribution 4.0 International License.
