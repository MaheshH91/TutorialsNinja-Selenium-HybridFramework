pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-25'
    }

    parameters {
        choice(
            name: 'BROWSER', 
            choices: ['chrome', 'firefox', 'edge'], 
            description: 'Select browser for test execution'
        )
        booleanParam(
            name: 'HEADLESS', 
            defaultValue: true, 
            description: 'Run tests in headless mode (recommended for CI)'
        )
        string(
            name: 'RECIPIENT_LIST', 
            defaultValue: 'your-email@example.com', 
            description: 'Recipient email addresses separated by comma'
        )
    }

    options {
        timeout(time: 60, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15', artifactNumToKeepStr: '15'))
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                checkout scm
            }
        }

        stage('Build & Test Execution') {
            steps {
                script {
                    def mavenCmd = "mvn clean test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    
                    if (isUnix()) {
                        sh "${mavenCmd}"
                    } else {
                        bat "${mavenCmd}"
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/ExtentReports/*.html, target/surefire-reports/**/*', allowEmptyArchive: true

            testNG reportFilenamePattern: '**/testng-results.xml'

            emailext (
                subject: "[${currentBuild.currentResult}] TutorialsNinja Automation Suite (Java 25) - Build #${env.BUILD_NUMBER}",
                body: """
                    <!DOCTYPE html>
                    <html>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <h2 style="color: #0056b3;">Test Execution Summary (Java 25)</h2>
                        <table style="border-collapse: collapse; width: 100%; max-width: 600px;">
                            <tr><td style="padding: 8px; border: 1px solid #ddd;"><b>Job Name:</b></td><td style="padding: 8px; border: 1px solid #ddd;">${env.JOB_NAME}</td></tr>
                            <tr><td style="padding: 8px; border: 1px solid #ddd;"><b>Build Number:</b></td><td style="padding: 8px; border: 1px solid #ddd;">#${env.BUILD_NUMBER}</td></tr>
                            <tr><td style="padding: 8px; border: 1px solid #ddd;"><b>Status:</b></td><td style="padding: 8px; border: 1px solid #ddd; font-weight: bold; color: ${currentBuild.currentResult == 'SUCCESS' ? 'green' : 'red'};">${currentBuild.currentResult}</td></tr>
                            <tr><td style="padding: 8px; border: 1px solid #ddd;"><b>Browser:</b></td><td style="padding: 8px; border: 1px solid #ddd;">${params.BROWSER}</td></tr>
                            <tr><td style="padding: 8px; border: 1px solid #ddd;"><b>Console URL:</b></td><td style="padding: 8px; border: 1px solid #ddd;"><a href="${env.BUILD_URL}">View Build Details</a></td></tr>
                        </table>
                        <p><i>The Extent Execution Report has been attached to this email.</i></p>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                to: "${params.RECIPIENT_LIST}",
                attachmentsPattern: 'test-output/ExtentReports/extentReport.html',
                attachLog: false
            )
        }
        success {
            echo "Pipeline finished successfully on Java 25."
        }
        failure {
            echo "Pipeline finished with failures."
        }
    }
}