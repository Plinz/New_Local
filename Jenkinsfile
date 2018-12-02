#!/usr/bin/env groovy

node {
	environment {
		GMAIL_SECRET_PASS = credentials('jenkins-gmail-secret-pass')
	}
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }

    stage('install tools') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v8.12.0 -DnpmVersion=6.4.1"
    }

    stage('npm install') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm"
    }

    stage('backend tests') {
        try {
            sh "./mvnw test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        try {
            sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='test -- -u'"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/test-results/jest/TESTS-*.xml'
        }
    }

    stage('quality analysis') {
        withSonarQubeEnv('sonar') {
            sh "./mvnw sonar:sonar"
        }
    }

    stage('deploy') {
        sh "./mvnw package -Pprod jib:dockerBuild"
        sh "docker-compose -f src/main/docker/app.yml up"
    }
}
