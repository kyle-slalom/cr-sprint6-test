def getRepoURL() {
  sh "git config --get remote.origin.url > .git/remote-url"
  return readFile(".git/remote-url").trim()
}

def getCommitSha() {
  sh "git rev-parse HEAD > .git/current-commit"
  return readFile(".git/current-commit").trim()
}

def updateGithubCommitStatus(String message, String state) {
  // workaround https://issues.jenkins-ci.org/browse/JENKINS-38674
  repoUrl = getRepoURL()
  commitSha = getCommitSha()

  step([
    $class: 'GitHubCommitStatusSetter',
    reposSource: [$class: "ManuallyEnteredRepositorySource", url: repoUrl],
    commitShaSource: [$class: "ManuallyEnteredShaSource", sha: commitSha],
    errorHandlers: [[$class: 'ShallowAnyErrorHandler']],
    statusResultSource: [
      $class: 'ConditionalStatusResultSource',
      results: [
        [$class: 'BetterThanOrEqualBuildResult', result: 'SUCCESS', state: 'SUCCESS', message: message],
        [$class: 'BetterThanOrEqualBuildResult', result: 'FAILURE', state: 'FAILURE', message: message],
        [$class: 'AnyBuildResult', state: 'FAILURE', message: 'Loophole']
      ]
    ]
  ])
}

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
        success {
            updateGithubCommitStatus('Build Success', 'SUCCESS')
        }
        failure {
            updateGithubCommitStatus('Build Failed', 'FAILURE')
        }
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}