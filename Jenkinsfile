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
                updateGithubCommitStatus('Build started', 'PENDING')
                sh 'mvn clean compile'
            }
        }
        stage('Test') { 
            steps {
                updateGithubCommitStatus('Test started', 'PENDING')
                sh 'mvn test' 
            }
        }
        stage('Package') { 
            steps {
                updateGithubCommitStatus('Package started', 'PENDING')
                sh 'mvn package -Dmaven.test.skip' 
            }
        }
        stage('Create Deployment Artifact') { 
            steps {
                updateGithubCommitStatus('Create Deployment Artifact started', 'PENDING')
                script {
                    zip archive: true, dir: '', glob: '**/target/*.war,**/scripts/*.sh,appspec.yml', zipFile: '${env.BUILD_NUMBER}.zip'
                }
            }
        }
        stage('Upload') { 
            steps {
                updateGithubCommitStatus('Upload started', 'PENDING')
                withAWS(region: 'us-east-2') {
                    s3Upload(file: '${env.BUILD_NUMBER}.zip', bucket: "${BUCKET_NAME}", path: "artifacts/${BUILD_NUMBER}.zip")
                }
            }
        }
        stage('Deploy') { 
            steps {
                script {
                    updateGithubCommitStatus('Deploying', 'PENDING')
                }
                withAWS(region: 'us-east-2') {
                    codedeploy applicationName: "${APPLICATION_NAME}", deploymentGroupName: "${DEPLOYMENT_GROUP_NAME}", s3Location: [bucket: "${BUCKET_NAME}", bundleType: 'zip', eTag: '', key: "artifacts/${BUILD_NUMBER}.zip"]
                }
                script {
                    updateGithubCommitStatus('Deployed', 'SUCCESS')
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