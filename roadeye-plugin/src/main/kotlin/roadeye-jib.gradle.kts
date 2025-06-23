import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.google.cloud.tools.jib")
}

tasks.named<BootJar>("bootJar") {
    archiveFileName = "app.jar"
}

jib {
    from {
        image = "eclipse-temurin:21-jdk@sha256:2cc80b4288f6240734ecd1acae01d20afa56f86327b9ae207a9468363422c974"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
            platform {
                architecture = "amd64"
                os = "linux"
            }
        }
    }

    to {
        tags = setOf("latest")
    }
}
