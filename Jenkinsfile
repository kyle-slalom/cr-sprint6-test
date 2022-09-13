pipeline {
    agent {
        docker {
            image 'maven:3.8.4-eclipse-temurin-11' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
        }
        stage('Package') { 
            steps {
                sh 'mvn package' 
            }
        }
        stage('Upload') { 
            steps {
                withAWS(region: 'us-east-2') {
                    s3Upload(file: 'target/api-0.0.1-SNAPSHOT.war', bucket: "${BUCKET_NAME}", path: "artifacts/${BUILD_NUMBER}-api.war")
                }
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}