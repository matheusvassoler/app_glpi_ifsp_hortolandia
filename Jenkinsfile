pipeline {
  agent any
  environment {
    APP_NAME = 'app'
    APP_VERSION = '1.0.0'
    ARTIFACTORY_PATH = ''
  }
  options {
    // Stop the build early in case of compile or test failures
    skipStagesAfterUnstable()
  }
  stages {

    stage('Detect build type') {
      steps {
        script {
          if (env.BRANCH_NAME == 'master' || env.CHANGE_TARGET == 'master') {
            env.BUILD_TYPE = 'release'
            ARTIFACTORY_PATH = "glpi-ifsp-hto-app/release/${APP_VERSION}/"
          } else {
            env.BUILD_TYPE = 'debug'

            if (env.BRANCH_NAME == 'develop') {
                ARTIFACTORY_PATH = "glpi-ifsp-hto-app/debug/${env.BRANCH_NAME}/${APP_VERSION}/"
            } else if (env.BRANCH_NAME.split('/')[0].toLowerCase() == 'feature' || env.BRANCH_NAME.split('/')[0].toLowerCase() == 'bugfix') {
                ARTIFACTORY_PATH = "glpi-ifsp-hto-app/debug/PRs/${env.BRANCH_NAME}/"
            }
          }
        }
      }
    }

    stage('Compile') {
      steps {
        // Compile the app and its dependencies
        sh './gradlew compile${BUILD_TYPE}Sources'
      }
    }

    stage('Build') {
      steps {
        // Compile the app and its dependencies
        sh './gradlew assemble${BUILD_TYPE}'
      }
    }

    stage('Static Analysis') {
        parallel {
            stage('Lint') {
                steps {
                    sh './gradlew lint'
                }
            }
            stage('Detekt') {
                steps {
                    sh './gradlew detekt'
                }
            }
            stage('Ktlint') {
                steps {
                    sh './gradlew ktlintCheck'
                }
            }
        }
    }



    stage("Tests") {
        parallel {
            stage("Unit test") {
                steps {
                    sh './gradlew test${BUILD_TYPE}'
                }
            }
//             stage("Instrumented test") {
//                 steps {
//                     sh './gradlew connectedAndroidTest'
//                 }
//             }
        }
    }

    stage('Publish') {
      steps {
        // Archive the APKs so that they can be downloaded from Jenkins
        archiveArtifacts "**/${APP_NAME}-${BUILD_TYPE}.apk"

        script {
            if (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.split('/')[0].toLowerCase() == 'feature' || env.BRANCH_NAME.split('/')[0].toLowerCase() == 'bugfix') {
                rtUpload (
                  serverId: "Artifactory_01",
                  spec:
                    """{
                      "files": [
                        {
                          "pattern": "**/${APP_NAME}-${BUILD_TYPE}.apk",
                          "target": "${ARTIFACTORY_PATH}"
                        }
                      ]
                    }"""
                )
            }
        }

      }
    }

  }
}