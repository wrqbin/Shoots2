pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'shoots2-app'
        DEPLOY_SERVER = '52.54.243.190'
        SSH_CREDENTIALS = 'deploy-server-ssh'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/wrqbin/Shoots2.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${BUILD_NUMBER}")
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(credentials: [SSH_CREDENTIALS]) {
                    script {
                        // 기존 컨테이너 정지 및 삭제
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${DEPLOY_SERVER} '
                                docker stop shoots2-container || true
                                docker rm shoots2-container || true
                                docker rmi ${DOCKER_IMAGE}:latest || true
                            '
                        """

                        // Docker 이미지 전송
                        sh """
                            docker save ${DOCKER_IMAGE}:${BUILD_NUMBER} |
                            ssh -o StrictHostKeyChecking=no ubuntu@${DEPLOY_SERVER}
                            'docker load'
                        """

                        // 새 컨테이너 실행
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${DEPLOY_SERVER} '
                                docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest
                                docker run -d \\
                                    --name shoots2-container \\
                                    -p 9090:9090 \\
                                    --restart unless-stopped \\
                                    --link mysql-container:mysql \\
                                    ${DOCKER_IMAGE}:latest
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '배포가 성공적으로 완료되었습니다!'
        }
        failure {
            echo '배포가 실패했습니다.'
        }
    }
}