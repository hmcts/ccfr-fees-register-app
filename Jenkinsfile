#!groovy
@Library("Reform")
import uk.gov.hmcts.Packager
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

            def artifactVersion = readFile('version.txt').trim()
            def versionAlreadyPublished = checkJavaVersionPublished group: 'fees-register', artifact: 'fees-register-app', version: artifactVersion

            onPR {
                if (versionAlreadyPublished) {
                    print "Artifact version already exists. Please bump it."
                    error "Artifact version already exists. Please bump it."
                }
            }

            stage('Build') {
                def descriptor = Artifactory.mavenDescriptor()
                descriptor.version = artifactVersion
                descriptor.transform()

                def rtMaven = Artifactory.newMavenBuild()
                rtMaven.tool = 'apache-maven-3.3.9'
                rtMaven.deployer releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
                rtMaven.deployer.deployArtifacts = (env.BRANCH_NAME == 'master') && !versionAlreadyPublished
                rtMaven.run pom: 'pom.xml', goals: 'clean install sonar:sonar', buildInfo: buildInfo
            }

            stage('Build docker') {
                dockerImage imageName: 'fees-register/fees-api'
                dockerImage imageName: 'fees-register/fees-database', context: 'docker/database'
            }

            onMaster {
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
            notifyBuildFailure channel: '#cc-payments-tech'
            throw err
        }
    }
}
