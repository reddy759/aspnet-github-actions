@Library('CISharedLibraries@v1.3.0') _

config = [
  jfrog:[
    artifactory:[
      credentialId:'nimbus-artifactory'
        ]
      ],
  app:[
    docker:[
      contextDir:'.',
      repoName:'nimbus-jenkins-core-docker-local',
      imageName:'inso-cli',
      imageTag:'1.0'
        ]
      ]
    ]
    
    def imagePullName='artifactory.cms.gov' + '/' + config.app.docker.repoName + '/' + config.app.docker.imageName +':' + config.app.docker.imageTag
    println imagePullName
pipeline {
    agent {
        kubernetes {
            defaultContainer 'simplecoreapp'
            yaml """
kind: Pod
metadata:
  name: simplecoreapp
spec:
  containers:
  - name: simplecoreapp
    image: artifactory.cms.gov/jenkins-core-docker/simplecoreapp:latest
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
  nodeSelector:
    Agents: true
"""
        }
    }
    stages {
        stage('Build with simplecoreapp') {
            steps {
                simplecoreappDockerBuildTagPush(config)
     
            }
        }
        }
    }
