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

rootProject.name = "roadeye"

include("application")
include("core")
include("hub")
include("consumer")
include("batch")
