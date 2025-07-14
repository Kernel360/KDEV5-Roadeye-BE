dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation(testFixtures(project(":core")))
}

tasks.register("buildDockerImage") {
    group = "build"

    dependsOn("bootJar")

    doLast {
        exec {
            commandLine(
                "docker", "build",
                "-t", "${rootProject.name}/${project.name}:latest",
                "-t", "${rootProject.name}/${project.name}:${project.version}",
                ".",
                "--build-arg", "JAR_FILE=build/libs/${project.name}-${project.version}.jar"
            )
        }
    }
}