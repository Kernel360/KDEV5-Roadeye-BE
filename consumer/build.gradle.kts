dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

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