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
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Extract Version') {
            steps {
                echo "Extracting version from pom.xml..."
                script {
                    // Properly extract version on Windows (last line only)
                    def output = bat(
                        script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout',
                        returnStdout: true
                    ).trim().split('\r?\n')
                    def appVersion = output[-1]  // last line is actual version
                    def ebVersionLabel = "${appVersion}-build-${BUILD_NUMBER}"
                    env.APP_VERSION = appVersion
                    env.EB_VERSION_LABEL = ebVersionLabel
                    echo "App version: ${env.APP_VERSION}"
                    echo "EB version label: ${env.EB_VERSION_LABEL}"
                }
            }
        }

        stage('Bundle Artifact') {
            steps {
                echo "Preparing deployment bundle..."
                bat '''
                    if not exist deploy mkdir deploy
                    copy target\\*.jar deploy\\
                    powershell -Command "Compress-Archive -Path deploy\\* -DestinationPath app.zip -Force"
                '''
            }
        }

        stage('Deploy to Elastic Beanstalk') {
            steps {
                echo "Deploying to Elastic Beanstalk..."
                withAWS(credentials: 'aws-eb-creds', region: "${AWS_REGION}") {
                    bat """
                        aws s3 cp app.zip s3://${S3_BUCKET}/app-${EB_VERSION_LABEL}.zip
                        aws elasticbeanstalk create-application-version ^
                            --application-name ${APPLICATION_NAME} ^
                            --version-label ${EB_VERSION_LABEL} ^
                            --source-bundle S3Bucket=${S3_BUCKET},S3Key=app-${EB_VERSION_LABEL}.zip
                        aws elasticbeanstalk update-environment ^
                            --environment-name ${ENVIRONMENT_NAME} ^
                            --version-label ${EB_VERSION_LABEL}
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful! Version: ${env.EB_VERSION_LABEL}"
        }
        failure {
            echo "❌ Deployment failed ! Please try again"
        }
    }
}
