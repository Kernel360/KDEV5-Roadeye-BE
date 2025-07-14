dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")

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