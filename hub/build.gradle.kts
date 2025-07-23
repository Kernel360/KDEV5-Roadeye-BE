dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(testFixtures(project(":core")))
    testImplementation("org.springframework.amqp:spring-rabbit-test")

    testFixturesImplementation(project(":core"))
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