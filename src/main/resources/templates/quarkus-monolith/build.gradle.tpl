plugins {
    id 'java'
    id 'io.quarkus'
    id 'com.diffplug.spotless' version '7.0.3'
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}")
    implementation 'org.antlr:ST4:4.3.4'
    implementation 'io.quarkus:quarkus-picocli'
    implementation 'io.quarkus:quarkus-arc'
    testImplementation 'io.quarkus:quarkus-junit5'
}

group '<packageGroup>'
version '1.0.0-SNAPSHOT'

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

test {
    systemProperty "java.util.logging.manager", "org.jboss.logmanager.LogManager"
}
compileJava {
    options.encoding = 'UTF-8'
    options.compilerArgs << '-parameters'
}

compileTestJava {
    options.encoding = 'UTF-8'
}

spotless {
    java {
        target fileTree('.') {
            include 'src/main/java/**/*.java'
            exclude '**/build/**', '**/build-*/**', 'src/main/resources/**'
        }
        toggleOffOn()
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        leadingTabsToSpaces(2)
    }
}