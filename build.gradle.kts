import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.vertx"
version = "1.0.0-SNAPSHOT"

repositories {
  maven {
    url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenContent {
      snapshotsOnly()
    }
  }
  mavenCentral()
}

val vertxVersion = "4.4.0-SNAPSHOT"
val junitJupiterVersion = "5.9.1"
val nettyDnsProvider = "4.1.87.Final"
var log4jVersion = "2.19.0"
var jacksonVersion = "2.14.0"

val mainVerticleName = "com.vertx.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencyManagement {
  imports {
    mavenBom ("org.apache.logging.log4j:log4j-bom:$log4jVersion")
  }
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("org.apache.logging.log4j:log4j-api")
  implementation("org.apache.logging.log4j:log4j-core")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
  implementation("junit:junit:4.13.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
