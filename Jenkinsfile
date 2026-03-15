pipeline {
    agent { 
       label 'jenkins-agent1'
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
                    mvn clean verify -DskipTests sonar:sonar\
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
    }
}
