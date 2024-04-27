pipeline {
    agent any

    environment {
        HOME_PATH = '/home/ubuntu'
    }

    stages {
        // Build stage
        stage('Build Service') {
            steps {
                echo 'Building Service'
                dir('auth') {
                    sh 'docker build -t hiking-service .'
                }
            }
        }

        // Test stage
        stage('Test') {
            steps {
                echo 'Testing Service'
                // 여기에 테스트 관련 작업을 추가할 수 있습니다.
            }
        }

        // Deploy stage
        stage('Deploy Service') {
            steps {
                echo 'Deploying Service'
                // 이미지 빌드 후, 컨테이너 중지 및 제거
                sh 'docker-compose -f docker-compose.yml stop hiking || true'
                sh 'docker-compose -f docker-compose.yml rm -f hiking || true'
                // 새로운 컨테이너 실행
                sh 'docker-compose -f docker-compose.yml up -d --force-recreate --no-deps hiking'
            }
        }
    }
}
