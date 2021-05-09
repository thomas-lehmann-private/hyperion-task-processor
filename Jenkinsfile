pipeline {
    agent any

    triggers {
        // Check for changes each 15 Minutes
        pollSCM('H/15 * * * *')
    }
  
    options  {
        // Provide timetstamps in the console output
        timestamps()
        // How many builds we want to keep
        buildDiscarder(logRotator(numToKeepStr: '5'))
        // The build NEVER should take this time
        timeout(time: 5, unit: 'MINUTES')
    }

    stages {
        stage('Build') {
            steps {
                script {
                    def description = bat(returnStdout:true, script:'@git log -1 --pretty="format:%%h - %%s"').trim()
                    currentBuild.description = description
                    echo 'GIT_URL: ' + GIT_URL
                }

                bat '''
                    ./mvnw
                '''
            }
        }
    }

    post { 
        always { 
            junit 'target/**/TEST*.xml'
            javadoc javadocDir: 'target/apidocs', keepAll: true
            jacoco()

            archiveArtifacts artifacts: 'target/hyperion*shaded.jar', onlyIfSuccessful: true
            archiveArtifacts artifacts: 'target/hyperion*javadoc.jar', onlyIfSuccessful: true

            recordIssues enabledForFailure: true, tool: checkStyle()
            recordIssues enabledForFailure: true, tool: spotBugs()
        }
    }
}
