#!groovy
@Library("jenkinsfile_shared_libraries@blackduck/pom") 

def FAILED_STAGE

pipeline {
  agent none

  environment {
    REPO_NAME = "braintree-java"
    SLACK_CHANNEL = "#auto-team-sdk-builds"
  }

  stages {
    stage("Audit") {
      parallel {

        // Runs a static code analysis scan and posts results to the PayPal Polaris server
        stage("Polaris") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            polarisAudit()
          }

          post {
            failure {
              script {
                FAILED_STAGE = env.STAGE_NAME
              }
            }
          }
        }


        // Runs a software composition analysis scan and posts results to the PayPal Black Duck server
        stage("Black Duck") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            blackduckAudit()
          }

          post {
            failure {
              script {
                FAILED_STAGE = env.STAGE_NAME
              }
            }
          }         
        }
      }
    } 
  }
}

