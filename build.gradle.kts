import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

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

	tasks.withType<BootJar> {
		enabled = true
	}
	tasks.withType<Jar> {
		enabled = true
	}

	dependencies {}

	tasks.withType<BootBuildImage> {
		buildWorkspace {
			bind {
				source = "/tmp/cache-${project.name}.work"
			}
		}
		buildCache {
			bind {
				source = "/tmp/cache-${project.name}.build"
			}
		}
		launchCache {
			bind {
				source = "/tmp/cache-${project.name}.launch"
			}
		}
	}
}
