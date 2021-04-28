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
        buildDiscarder(logRotator(numToKeepStr: '3'))
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
                    ./mvnw clean package verify
                '''
            }
        }
    }

    post { 
        always { 
            junit 'target/**/TEST*.xml'
            jacoco()

            archiveArtifacts artifacts: 'target/spline*shaded.jar', onlyIfSuccessful: true

            recordIssues enabledForFailure: true, tool: checkStyle()
            recordIssues enabledForFailure: true, tool: spotBugs()
        }
    }
}
