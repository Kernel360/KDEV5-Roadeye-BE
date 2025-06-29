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
