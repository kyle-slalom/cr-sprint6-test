pipeline {
    agent {
        docker {
            image 'maven:3.8.4-eclipse-temurin-17' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn package' 
            }
        }
    }
}