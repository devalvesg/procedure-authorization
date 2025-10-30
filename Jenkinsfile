pipeline {
    agent any

    tools {
        maven 'Maven-3.8'
        jdk 'JDK-11'
    }

    environment {
        APP_NAME = 'procedure-authorization'
        DOCKER_IMAGE = "${APP_NAME}:${BUILD_NUMBER}"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 20, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Clonando repositório...'
                checkout scm
                script {
                    sh '''
                        git log -1 --pretty=format:'Autor: %an%nMensagem: %s'
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Compilando o projeto...'
                sh '''
                    mvn clean compile -B -V
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                echo '🧪 Executando testes unitários...'
                sh 'mvn test -B'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'

                    script {
                        if (fileExists('target/site/jacoco/jacoco.xml')) {
                            jacoco(
                                execPattern: '**/target/jacoco.exec',
                                classPattern: '**/target/classes',
                                sourcePattern: '**/src/main/java',
                                exclusionPattern: '**/*Test*.class'
                            )
                        }
                    }
                }
                success {
                    echo '✅ Todos os testes passaram!'
                }
                failure {
                    echo '❌ Alguns testes falharam.'
                }
            }
        }

        stage('Package') {
            steps {
                echo '📦 Gerando artefato WAR...'
                sh '''
                    mvn package -DskipTests -B
                    ls -lh target/*.war
                '''
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.war',
                                   fingerprint: true,
                                   allowEmptyArchive: false
                    echo '✓ Artefato WAR arquivado com sucesso!'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '🐳 Construindo imagem Docker...'
                script {
                    def dockerImage = docker.build(
                        "${DOCKER_IMAGE}",
                        "--no-cache ."
                    )

                    if (env.GIT_BRANCH == 'main' || env.GIT_BRANCH == 'master' || env.GIT_BRANCH == 'origin/main') {
                        dockerImage.tag('latest')
                        echo '✓ Imagem taggeada como latest'
                    }

                    echo "✓ Imagem Docker criada: ${DOCKER_IMAGE}"
                }
            }
            post {
                success {
                    echo '✅ Build da imagem Docker concluído!'
                }
            }
        }
    }

    post {
        success {
            echo '''✅ Pipeline executado com SUCESSO!'''
        }
        failure {
            echo '''❌ Pipeline FALHOU!'''
        }
        always {
            cleanWs(
                deleteDirs: true,
                patterns: [
                    [pattern: 'target/**', type: 'INCLUDE']
                ]
            )
        }
    }
}