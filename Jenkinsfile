pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        APPLICATION_NAME = 'springboot-api'
        ENVIRONMENT_NAME = 'Springboot-api-env'
        S3_BUCKET = 'elasticbeanstalk-us-east-1-575030572075'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Cloning Git repo..."
                git branch: 'master', url: 'https://github.com/AbhinavKoritala3131/Portal-Prod'
            }
        }

        stage('Build') {
            steps {
                echo "Building Spring Boot app with Maven..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Extract Version') {
            steps {
                echo "Extracting version from pom.xml..."
                script {
                    APP_VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    echo "App version: ${APP_VERSION}"
                    EB_VERSION_LABEL = "${APP_VERSION}-build-${BUILD_NUMBER}"
                    echo "EB version label: ${EB_VERSION_LABEL}"
                }
            }
        }

        stage('Bundle Artifact') {
            steps {
                echo "Preparing deployment bundle..."
                sh '''
                    mkdir -p deploy
                    cp target/*.jar deploy/
                    cd deploy
                    zip -r ../app.zip .
                '''
            }
        }

        stage('Deploy to Elastic Beanstalk') {
            steps {
                echo "Deploying to Elastic Beanstalk..."
                withAWS(credentials: 'aws-eb-creds', region: "${AWS_REGION}") {
                    sh """
                        aws s3 cp app.zip s3://${S3_BUCKET}/app-${EB_VERSION_LABEL}.zip

                        aws elasticbeanstalk create-application-version \
                            --application-name ${APPLICATION_NAME} \
                            --version-label ${EB_VERSION_LABEL} \
                            --source-bundle S3Bucket=${S3_BUCKET},S3Key=app-${EB_VERSION_LABEL}.zip

                        aws elasticbeanstalk update-environment \
                            --environment-name ${ENVIRONMENT_NAME} \
                            --version-label ${EB_VERSION_LABEL}
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful! Version: ${EB_VERSION_LABEL}"
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}
