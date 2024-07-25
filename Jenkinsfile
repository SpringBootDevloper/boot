pipeline {
    agent any

    tools {
        maven "jenkins-maven"
    }

    stages {
        stage('Checkout'){
            steps{
			       git branch: 'main', 
                   url:'https://github.com/SpringBootDevloper/boot.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
		stage('Test'){
			steps{
			    bat 'mvn test'
				}
		}
		stage('Docker create image and run conatiner using compose'){
		    steps{
		       bat 'docker compose up -d'
		    }
		}
    }
		    post {
			     always{
				   echo "Always executing"
				 }
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
				failure{
				   echo "Failed"
				}
            }
        }
