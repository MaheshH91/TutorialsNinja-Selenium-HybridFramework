pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Target browser for execution'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browsers in headless mode for CI/CD'
        )
        string(
            name: 'RECIPIENT_LIST',
            defaultValue: 'holkarmahesh1@gmail.com',
            description: 'Comma-separated recipient emails for execution notification'
        )
    }

    options {
        timeout(time: 45, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15', artifactNumToKeepStr: '15'))
    }

    stages {
        stage('Clean Workspace & Pre-Flight Checks') {
            steps {
                cleanWs()
                checkout scm
                script {
                    if (!isUnix()) {
                        // Purge corrupted Windows Session 0 Selenium cache and kill orphaned browser processes
                        bat '''
                            @echo off
                            echo Cleaning up orphaned driver/browser processes...
                            taskkill /F /IM chromedriver.exe /T 2>nul || exit 0
                            taskkill /F /IM chrome.exe /T 2>nul || exit 0
                            taskkill /F /IM msedgedriver.exe /T 2>nul || exit 0
                            taskkill /F /IM msedge.exe /T 2>nul || exit 0
                            taskkill /F /IM geckodriver.exe /T 2>nul || exit 0
                            taskkill /F /IM firefox.exe /T 2>nul || exit 0

                            echo Cleaning broken Selenium Manager cache in system profiles...
                            if exist "C:\\Windows\\SysWOW64\\config\\systemprofile\\.cache\\selenium" (
                                rmdir /s /q "C:\\Windows\\SysWOW64\\config\\systemprofile\\.cache\\selenium" 2>nul || exit 0
                            )
                            if exist "C:\\WINDOWS\\system32\\config\\systemprofile\\.cache\\selenium" (
                                rmdir /s /q "C:\\WINDOWS\\system32\\config\\systemprofile\\.cache\\selenium" 2>nul || exit 0
                            )
                            if exist "C:\\Temp\\browser-profiles" (
                                rmdir /s /q "C:\\Temp\\browser-profiles" 2>nul || exit 0
                            )
                        '''
                    }
                }
            }
        }

        stage('Environment Diagnostic') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            java -version
                            mvn -version
                            google-chrome --version || echo "Chrome not directly on path, SeleniumManager will resolve"
                        '''
                    } else {
                        bat '''
                            java -version
                            mvn -version
                        '''
                    }
                }
            }
        }

        stage('Run Automation Regression Suite') {
            steps {
                script {
                    def mavenCmd = "mvn clean test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    
                    // Allow pipeline to proceed to reporting stages even on test assertions/failures
                    catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                        if (isUnix()) {
                            sh "${mavenCmd}"
                        } else {
                            bat "${mavenCmd}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // 1. Archive Extent Reports & Surefire Logs
            archiveArtifacts artifacts: 'test-output/ExtentReports/*.html, target/surefire-reports/**/*', allowEmptyArchive: true

            // 2. Publish TestNG XML Results
            testNG reportFilenamePattern: 'target/surefire-reports/testng-results.xml'

            // 3. Publish HTML Report inside Jenkins UI
            publishHTML target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output/ExtentReports',
                reportFiles: 'extentReport.html',
                reportName: 'Extent Execution Dashboard'
            ]

            // 4. Send Rich HTML Email Notification with Attached Extent Report
            emailext (
                subject: "[${currentBuild.currentResult}] TutorialsNinja Automation Suite - Build #${env.BUILD_NUMBER}",
                body: """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #24292e; line-height: 1.5; }
                            .container { max-width: 650px; margin: 20px auto; padding: 20px; border: 1px solid #e1e4e8; border-radius: 6px; }
                            .header { border-bottom: 2px solid #0366d6; padding-bottom: 12px; margin-bottom: 16px; }
                            .title { font-size: 20px; font-weight: 600; color: #0366d6; margin: 0; }
                            table { width: 100%; border-collapse: collapse; margin-top: 15px; }
                            th, td { padding: 10px; border: 1px solid #e1e4e8; text-align: left; font-size: 14px; }
                            th { background-color: #f6f8fa; }
                            .status-success { color: #28a745; font-weight: bold; }
                            .status-failure { color: #cb2431; font-weight: bold; }
                            .status-unstable { color: #b08800; font-weight: bold; }
                            .footer { margin-top: 20px; font-size: 12px; color: #586069; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h2 class="title">Test Execution Report (Java 25)</h2>
                            </div>
                            <p>Automation regression execution has completed on Jenkins.</p>
                            <table>
                                <tr><th>Project</th><td>${env.JOB_NAME}</td></tr>
                                <tr><th>Build Number</th><td>#${env.BUILD_NUMBER}</td></tr>
                                <tr>
                                    <th>Status</th>
                                    <td>
                                        <span class="${currentBuild.currentResult == 'SUCCESS' ? 'status-success' : (currentBuild.currentResult == 'UNSTABLE' ? 'status-unstable' : 'status-failure')}">
                                            ${currentBuild.currentResult}
                                        </span>
                                    </td>
                                </tr>
                                <tr><th>Target Browser</th><td>${params.BROWSER} (Headless: ${params.HEADLESS})</td></tr>
                                <tr><th>Build URL</th><td><a href="${env.BUILD_URL}">View Console Log & Artifacts</a></td></tr>
                            </table>
                            <p class="footer">
                                <i>Note: The single-file dark-themed Extent HTML Report is attached to this email.</i>
                            </p>
                        </div>
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
            echo "Regression Suite executed with 100% Pass Rate."
        }
        unstable {
            echo "Regression Suite completed with test assertions or failures."
        }
        failure {
            echo "Pipeline run terminated due to compilation, build, or infrastructure error."
        }
    }
}