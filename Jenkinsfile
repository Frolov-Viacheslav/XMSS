#!groovy
properties([disableConcurrentBuilds()])

pipeline {
  agent{
    label 'master'
  }
 triggers {pollSCM('* * * * *')} 
 options {
    timestamps()
  }
  stages {
    stage('First step') {
      steps {
        echo 'Kraken'
      }
    }
  }
}
