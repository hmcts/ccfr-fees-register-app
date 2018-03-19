#!groovy
@Library("Reform")
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager
import uk.gov.hmcts.RPMTagger

def packager = new Packager(this, 'cc')
def ansible = new Ansible(this, 'ccfr')
RPMTagger rpmTagger = new RPMTagger(this, 'fees-register-api', packager.rpmName('fees-register-api', params.rpmVersion), 'cc-local')

def server = Artifactory.server 'artifactory.reform'
def buildInfo = Artifactory.newBuildInfo()

properties(
    [[$class: 'GithubProjectProperty', displayName: 'Fees Register API', projectUrlStr: 'https://github.com/hmcts/ccfr-fees-register-app'],
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
                def rtGradle = Artifactory.newGradleBuild()
                rtGradle.tool = 'gradle-4.2'
                rtGradle.deployer repo: 'libs-release', server: server
                rtGradle.deployer.deployArtifacts = (env.BRANCH_NAME == 'master') && !versionAlreadyPublished
                rtGradle.run buildFile: 'build.gradle', tasks: 'clean build dependencyCheck artifactoryPublish sonarqube', buildInfo: buildInfo
            }

            def feesApiDockerVersion
            def feesDatabaseDockerVersion

            stage('Build docker') {
                feesApiDockerVersion = dockerImage imageName: 'fees-register/fees-api'
                feesDatabaseDockerVersion = dockerImage imageName: 'fees-register/fees-database', context: 'docker/database'
            }

            /*stage("Trigger acceptance tests") {
                build job: '/fees-register/fees-register-app-acceptance-tests/master', parameters: [
                    [$class: 'StringParameterValue', name: 'feesApiDockerVersion', value: feesApiDockerVersion],
                    [$class: 'StringParameterValue', name: 'feesDatabaseDockerVersion', value: feesDatabaseDockerVersion]
                ]
            }*/

            onMaster {
                stage('Publish JAR') {
                    server.publishBuildInfo buildInfo
                }

                def rpmVersion

                stage("Publish RPM") {
                    rpmVersion = packager.javaRPM('master', 'fees-register-api', '$(ls build/libs/fees-register-app.jar)', 'springboot', 'api/src/main/resources/application.properties')
                    packager.publishJavaRPM('fees-register-api')
                }

                stage('Deploy to Dev') {
                    ansible.runDeployPlaybook("{fees_register_api_version: ${rpmVersion}}", 'dev')
                    rpmTagger.tagDeploymentSuccessfulOn('dev')
                }

                stage("Trigger smoke tests in Dev") {
                    sh 'curl -f https://dev.fees-register.reform.hmcts.net:4411/health'
                    rpmTagger.tagTestingPassedOn('dev')
                }

                stage('Deploy to Test') {
                    ansible.runDeployPlaybook("{fees_register_api_version: ${rpmVersion}}", 'test')
                    rpmTagger.tagDeploymentSuccessfulOn('test')
                }

                stage("Trigger smoke tests in Test") {
                    sh 'curl -f https://test.fees-register.reform.hmcts.net:4431/health'
                    rpmTagger.tagTestingPassedOn('test')
                }
            }

            milestone()
        } catch (err) {
            notifyBuildFailure channel: '#cc-payments-tech'
            throw err
        }
    }
}
