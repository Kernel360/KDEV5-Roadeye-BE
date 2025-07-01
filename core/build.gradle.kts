import org.springframework.boot.gradle.tasks.bundling.BootJar

springBoot {
	tasks.withType<BootJar> {
		enabled = false
	}
}

dependencies {
    implementation("org.springframework:spring-web")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("jakarta.validation:jakarta.validation-api")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.amqp:spring-amqp")

    implementation("io.github.openfeign.querydsl:querydsl-jpa:6.12")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:6.12:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")

    testRuntimeOnly("com.h2database:h2:2.2.220")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
