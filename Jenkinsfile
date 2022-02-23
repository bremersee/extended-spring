pipeline {
  agent {
    label 'maven'
  }
  environment {
    DEPLOY = true
    SNAPSHOT_SITE = true
    RELEASE_SITE = true
    DEPLOY_FEATURE = false
  }
  tools {
    jdk 'jdk11'
    maven 'm3'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '8', artifactNumToKeepStr: '8'))
  }
  stages {
    stage('Tools') {
      steps {
        sh 'java -version'
        sh 'mvn -B --version'
      }
    }
    stage('Test') {
      when {
        not {
          branch 'feature/*'
        }
      }
      steps {
        sh 'mvn -B clean test'
      }
      post {
        always {
          junit '**/surefire-reports/*.xml'
          jacoco(
              execPattern: '**/coverage-reports/*.exec'
          )
        }
      }
    }
    stage('Deploy') {
      when {
        allOf {
          environment name: 'DEPLOY', value: 'true'
          anyOf {
            branch 'develop'
            branch 'main'
          }
        }
      }
      steps {
        sh 'mvn -B -P deploy clean deploy'
      }
    }
    stage('Snapshot Site') {
      when {
        allOf {
          branch 'develop'
          environment name: 'SNAPSHOT_SITE', value: 'true'
        }
      }
      steps {
        sh 'mvn -B clean site-deploy'
      }
    }
    stage('Release Site') {
      when {
        allOf {
          branch 'main'
          environment name: 'RELEASE_SITE', value: 'true'
        }
      }
      steps {
        sh 'mvn -B -P gh-pages-site clean site site:stage scm-publish:publish-scm'
      }
    }
    stage('Deploy Feature') {
      when {
        allOf {
          branch 'feature/*'
          environment name: 'DEPLOY_FEATURE', value: 'true'
        }
      }
      steps {
        sh 'mvn -B -P feature,allow-features clean deploy'
      }
      post {
        always {
          junit '**/surefire-reports/*.xml'
          jacoco(
              execPattern: '**/coverage-reports/*.exec'
          )
        }
      }
    }
  }
}