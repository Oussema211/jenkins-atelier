pipeline {
  agent any

  environment {
    DOCKER_IMAGE = "oussema22/atelier-devops"
    DOCKER_CRED  = "docker-hub-creds"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        script { echo "Branch: ${env.BRANCH_NAME ?: 'unknown'}" }
      }
    }

    stage('Clean + Build Maven') {
      steps {
        sh 'mvn -B -DskipTests=false clean package'
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }

    stage('Unit tests') {
      steps {
        sh 'mvn test'
      }
    }

    stage('Docker build') {
      steps {
        script {
          def tag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()

          // Build image with commit tag
          sh "docker build -t ${DOCKER_IMAGE}:${tag} ."

          // Tag latest (added)
          sh "docker tag ${DOCKER_IMAGE}:${tag} ${DOCKER_IMAGE}:latest"
        }
      }
    }

    stage('Docker push') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED}", usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
          sh '''
            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
            TAG=$(git rev-parse --short HEAD)

            # Push commit tag
            docker push ${DOCKER_IMAGE}:$TAG

            # Push latest (added)
            docker push ${DOCKER_IMAGE}:latest

            docker logout
          '''
        }
      }
    }
  }

  post {
    success { echo "Pipeline succeeded: ${DOCKER_IMAGE}" }
    failure { echo "Pipeline failed" }
  }
}

