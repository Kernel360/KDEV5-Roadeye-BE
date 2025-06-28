import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	`java-library`
	`java-test-fixtures`
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

allprojects {
	plugins.apply("java")
	plugins.apply("java-library")
	plugins.apply("java-test-fixtures")
	plugins.apply("io.spring.dependency-management")

	group = "org.re"
	version = "0.0.1"

	repositories {
		mavenCentral()
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}

	dependencies {
		implementation("org.jspecify:jspecify:1.0.0")

		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")

		testImplementation("org.mockito:mockito-subclass")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	dependencyManagement {
		dependencies {
			dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
		}
	}

	tasks.named<Test>("test") {
		jvmArgs(
			"-Xshare:off",
			"-XX:+EnableDynamicAgentLoading",
			"-javaagent:${configurations.testRuntimeClasspath.get().find { it.name.contains("mockito-core") }}"
		)
		jvmArgs("-Dspring.profiles.include=test")

		useJUnitPlatform()
	}
}

subprojects {
	plugins.apply("org.springframework.boot")

	tasks.withType<Jar> {
		enabled = true
	}

	dependencies {}

	tasks.withType<BootBuildImage> {
		// https://docs.spring.io/spring-boot/gradle-plugin/packaging-oci-image.html
		// https://buildpacks.io/docs/
		// https://hub.docker.com/u/paketobuildpacks

		buildpacks.set(
			listOf(
				"paketobuildpacks/java",
				"paketobuildpacks/opentelemetry"
			)
		)
		environment.set(
			mapOf(
				"BP_OPENTELEMETRY_ENABLED" to "true"
			)
		)

		imageName = "roadeye/${project.name}"
		builder = "paketobuildpacks/builder-jammy-base"
	}
}

tasks.register("bootBuildImageAll") {
	group = "build"

	val springBootProjects = subprojects.filter {
		it.tasks.any { task -> task.name == "bootJar" && task.enabled }
	}
	dependsOn(springBootProjects.map { it.tasks.named("bootBuildImage") })
}
