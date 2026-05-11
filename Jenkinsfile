#!groovy

def FAILED_STAGE

pipeline {
  agent none

  environment {
    REPO_NAME = "braintree-java"
    SLACK_CHANNEL = "#auto-team-sdk-builds"
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '50'))
    timestamps()
    timeout(time: 120, unit: 'MINUTES')
  }

  stages {
    stage("Audit") {
      parallel {
        stage("CodeQL") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            codeQLv2(maven: true)
          }

          post {
            failure {
              script {
                FAILED_STAGE = env.STAGE_NAME
              }
            }
          }
        }

        stage("SonarQube") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}-sonar"
            }
          }

          steps {
            script {
              sh "docker build -t braintree-java ."
              sh "docker run --rm -v \"\$(pwd):\$(pwd)\" -w \"\$(pwd)\" braintree-java /bin/bash -l -c 'mvn test'"
              executeSonarQubeScan()
            }
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

    stage("SDK Tests") {
      when {
        branch 'master'
      }

      parallel {
        stage("Java 8 Bookworm") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            build job: 'java_8-bookworm_server_sdk_master', wait: true
          }

          post {
            failure {
              script {
                FAILED_STAGE = env.STAGE_NAME
              }
            }
          }
        }

        stage("Java 11 Bullseye") {
          agent {
            node {
              label ""
              customWorkspace "workspace/${REPO_NAME}"
            }
          }

          steps {
            build job: 'java_maven_11-bullseye_server_sdk_master', wait: true
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

  post {
    unsuccessful {
      slackSend color: "danger",
        channel: "${env.SLACK_CHANNEL}",
        message: "${env.JOB_NAME} - #${env.BUILD_NUMBER} Failure after ${currentBuild.durationString} at stage \"${FAILED_STAGE}\"(<${env.BUILD_URL}|Open>)"
    }
  }
}
