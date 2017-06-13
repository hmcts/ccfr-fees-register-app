#!groovy

@Library("Reform")
import uk.gov.hmcts.Packager

def packager = new Packager(this, 'cc')

def server = Artifactory.server 'artifactory.reform'
def buildInfo = Artifactory.newBuildInfo()

properties(
    [[$class: 'GithubProjectProperty', displayName: 'Fees Register API', projectUrlStr: 'https://git.reform.hmcts.net/fees-register/fees-register-app'],
     pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

milestone()
lock(resource: "fees-register-app-${env.BRANCH_NAME}", inversePrecedence: true) {
    node {
        try {
            stage('Checkout') {
                deleteDir()
                checkout scm
            }

            stage('Build') {
                def descriptor = Artifactory.mavenDescriptor()
                descriptor.version = "1.0.0.${env.BUILD_NUMBER}"
                descriptor.transform()

                def rtMaven = Artifactory.newMavenBuild()
                rtMaven.tool = 'apache-maven-3.3.9'
                rtMaven.deployer releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
                rtMaven.run pom: 'pom.xml', goals: 'clean install sonar:sonar', buildInfo: buildInfo
            }

            stage('Build docker') {
                dockerImage imageName: 'fees-register/fees-api'
            }

            ifMaster {
                def rpmVersion

                stage('Publish JAR') {
                    server.publishBuildInfo buildInfo
                }

                stage("Publish RPM") {
                    rpmVersion = packager.javaRPM('master', 'fees-register-api', '$(ls api/target/fees-register-api-*.jar)', 'springboot', 'api/src/main/resources/application.properties')
                    packager.publishJavaRPM('fees-register-api')
                }

                stage("Trigger acceptance tests") {
                    build job: '/fees-register/fees-register-app-acceptance-tests/master', parameters: [[$class: 'StringParameterValue', name: 'rpmVersion', value: rpmVersion]]
                }
            }

            milestone()
        } catch (err) {
            notifyBuildFailure channel: '#cc_tech'
            throw err
        }
    }
}

private ifMaster(Closure body) {
    if ("master" == "${env.BRANCH_NAME}") {
        body()
    }
}
