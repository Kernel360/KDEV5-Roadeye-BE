pluginManagement {
	repositories {
		gradlePluginPortal()
	}

	plugins {
		id("org.springframework.boot") version "3.4.2"
		id("io.spring.dependency-management") version "1.1.7"
	}
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "Roadeye"

include("application")
include("commons")
include("core")
include("api")
include("hub")
include("consumer")
