pipeline {
    agent { 
       label 'jenkins-agent1'
          }
    environment {
        ECR_URI = "283744739314.dkr.ecr.eu-north-1.amazonaws.com/my-repo"
    }
      tools {
          jdk 'jdk-21'
          maven 'maven'
      }
    stages {

        stage('Verify Checkout') {
            steps {
                sh '''
                echo "Current directory:"
                pwd
                echo "Listing files:"
                ls -l
                '''
            }
        }

        stage('Trivy FS Scan') {
            steps {
                sh '''
                echo "Running Trivy filesystem scan..."
                trivy fs --severity HIGH,CRITICAL --exit-code 1 .
                '''
          }
        }
          stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                   sh '''
                    mvn clean verify -Dmaven.test.failure.ignore=true sonar:sonar\
                    -Dsonar.projectKey=my-project \
                    -Dsonar.projectName=my-project
                    '''
                                  }
            }
          }
        stage('Quality Gate') {
    steps {
        waitForQualityGate abortPipeline: true
    }
        }
          stage('Build Image') {
            steps {
                  // Get branch name
                    def branch = env.BRANCH_NAME ?: "unknown"

                    // Clean branch name (replace / with -)
                    branch = branch.replaceAll("/", "-")

                    // Create tag
                    env.IMAGE_TAG = "${branch}-${env.BUILD_NUMBER}"
                    
                    sh "docker build -t ${REPO_NAME}:${IMAGE_TAG} ."

                    sh "echo Image Tag: ${env.IMAGE_TAG}"
            }
          }
    }
}
