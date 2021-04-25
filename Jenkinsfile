pipeline {
    agent any

    options  {
        timestamps()
    }

    stages {
        stage('Build') {
            steps { 
                bat '''
                    ./mvnw clean package verify
                '''
            }
        }
    }
}
