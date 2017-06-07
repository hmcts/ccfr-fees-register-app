#!groovy

@Library("Reform")
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager

def packager = new Packager(this, 'cc')
def ansible = new Ansible(this, 'ccfr')

def server = Artifactory.server 'artifactory.reform'
def rtMaven = Artifactory.newMavenBuild()
def buildInfo = Artifactory.newBuildInfo()

properties(
    [[$class: 'GithubProjectProperty', displayName: 'Fees Register API', projectUrlStr: 'https://git.reform.hmcts.net/fees-register/fees-register-app'],
    pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)


stageWithNotification('Checkout') {
    deleteDir()
    checkout scm
}

stageWithNotification('Build') {
    rtMaven.tool = 'apache-maven-3.3.9'
    rtMaven.deployer releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
    rtMaven.run pom: 'pom.xml', goals: 'clean install', buildInfo: buildInfo
    archiveArtifacts 'api/target/*.jar'
}


ifMaster {
    def rpmVersion

    stageWithNotification('Publish JAR') {
        server.publishBuildInfo buildInfo
    }

    stageWithNotification("Publish RPM") {
        rpmVersion = packager.javaRPM('master', 'fees-register-api', '$(ls api/target/fees-register-api-*.jar)', 'springboot', 'api/src/main/resources/application.properties')
        packager.publishJavaRPM('fees-register-api')
    }

    stage("Trigger acceptance tests") {
        build job: '/fees-register/fees-register-app-acceptance-tests/master', parameters: [[$class: 'StringParameterValue', name: 'rpmVersion', value: rpmVersion]]
    }
}

private ifMaster(Closure body) {
    if ("master" == "${env.BRANCH_NAME}") {
        body()
    }
}

private stageWithNotification(String name, Closure body) {
    stage(name) {
        node {
            try {
                body()
            } catch (err) {
                notifyBuildFailure channel: '#cc_tech'
                throw err
            }
        }
    }
}

