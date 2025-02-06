#!groovy
@Library("jenkinsfile_shared_libraries@addMultiLangSupport") _

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
        stage("CodeQL") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            codeQL(maven: true)
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
