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
        stage('Build'){
            steps{
                sh '''
                 mvn clean verify -Dmaven.test.failure.ignore=true
                 '''
        }
        }
          stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                   sh '''
                     mvn sonar:sonar \
                    -Dsonar.projectKey=my-project \
                    -Dsonar.projectName=my-project
                    '''
                                  }
            }
          }
      /*  stage('Quality Gate') {
    steps {
        waitForQualityGate abortPipeline: true
    }
        } */
          stage('Build Image') {
            steps {
                script {
                   // Extract repo name from Git URL
            def repo = sh(
                script: "basename -s .git `git config --get remote.origin.url`",
                returnStdout: true
            ).trim()

            // Get branch name (robust way)
            def branch = env.BRANCH_NAME ?: env.GIT_BRANCH

            if (!branch) {
                branch = sh(
                    script: "git name-rev --name-only HEAD",
                    returnStdout: true
                ).trim()
            }

            // Clean values
            branch = branch.replaceAll("origin/", "").replaceAll("/", "-")
            repo   = repo.replaceAll("/", "-")

            // Final tag
            env.imageTag = "${repo}-${branch}-${env.BUILD_NUMBER}"

            sh """
                docker build -t ${imageTag} .
            """

            echo "Image Tag: ${imageTag}"
                }
                }

            }

        stage('Push to ECR') {
      steps {
        script {

            sh """
                # Login to ECR
                aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin 283744739314.dkr.ecr.eu-north-1.amazonaws.com

                # Tag image for ECR
                docker tag ${imageTag} ${ECR_URI}:${imageTag}

                # Push image
                docker push ${ECR_URI}:${imageTag}
            """
        }
    }
        }
          }
    }

