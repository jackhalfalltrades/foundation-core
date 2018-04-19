node {
    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server 'localhost'
    // Create an Artifactory Gradle instance.
    def rtGradle = Artifactory.newGradleBuild()
    def buildInfo

    stage('Clone sources') {
        git url: 'https://github.com/jackhalfalltrades/foundation-core.git'
    }

    stage('Artifactory configuration') {
        // Tool name from Jenkins configuration
        rtGradle.tool = "grade-4.6"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtGradle.deployer repo:'gradle-release-local', server: server
        rtGradle.resolver repo:'gradle-release', server: server
    }

    stage('Gradle build') {
        buildInfo = rtGradle.run rootDir: "", buildFile: 'build.gradle', tasks: 'clean build artifactoryPublish'


    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }
}

