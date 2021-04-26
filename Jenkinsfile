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
                def description = bat(returnStdout:true, script:'git log -1 --pretty="format:%h:%s"')
                currentBuild.description = description

                bat '''
                    ./mvnw clean package verify
                '''
            }
        }
    }

    post { 
        always { 
            junit 'target/**/TEST*.xml'

            recordIssues enabledForFailure: true, tool: checkStyle()
            recordIssues enabledForFailure: true, tool: spotBugs()
        }
    }
}
