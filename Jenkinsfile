pipeline {
    agent any

    options  {
        timestamps()
    }

    stages {
        stage('Get Code') {
            steps { 
                cleanWs()
                checkout scm
            }
        }

        stage('Build') {
            steps { 
                bat '''
                    ./mvnw clean package
                '''
            }
        }
    }
}