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
        stage('Build') {
            steps {
                script {
                    codebuildResult = awsCodeBuild(
                        projectName: "${APPLICATION_NAME}-codebuild", 
                        region: 'us-east-2', 
                        credentialsType: 'keys', 
                        sourceControlType: 'project',
                        sourceVersion: "${env.BRANCH_NAME}",
                    )
                    s3Bucket = codebuildResult.getArtifactsLocation().split(":::")[1].split("/")[0]
                    s3Key = codebuildResult.getArtifactsLocation().split("/")[1]
                    echo "s3Bucket: ${s3Bucket}"
                    echo "s3Key: ${s3Key}"
                }
            }
        }
        stage('Test') {
            steps {
                withAWS(region: 'us-east-2') {
                    s3Download(bucket: "${s3Bucket}", path: "${s3Key}/reports", file: "reports.zip")
                    unzip zipFile: "reports.zip", dir: "reports"
                    junit "reports/*.xml"
                }
            }
        }
        stage('Deploy') { 
            steps {
                withAWS(region: 'us-east-2') {
                    createDeployment(
                        applicationName: "${APPLICATION_NAME}", 
                        deploymentGroupName: "${DEPLOYMENT_GROUP_NAME}",
                        description: "Deploying ${env.BUILD_NUMBER}",
                        s3Bucket: "${s3Bucket}",
                        s3Key: "${s3Key}/${APPLICATION_NAME}-codebuild",
                        s3BundleType: "zip",
                        waitForCompletion: true
                    )
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
    }
}