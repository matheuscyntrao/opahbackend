pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'JDK_17'
        MAVEN_HOME = tool 'MAVEN_3_8'
    }

    tools {
        jdk 'JDK_17'
        maven 'MAVEN_3_8'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '$MAVEN_HOME/bin/mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh '$MAVEN_HOME/bin/mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

    }

    post {
        failure {
            echo '### Falha nos testes! ###'
        }
        success {
            echo '### Todos os testes passaram! ###'
        }
        always {
            echo '### Fim da execução do pipeline. ###'
        }
    }
}