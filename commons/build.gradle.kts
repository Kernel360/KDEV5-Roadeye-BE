import org.springframework.boot.gradle.tasks.bundling.BootJar

springBoot {
	tasks.withType<BootJar> {
		enabled = false
	}
}

dependencies {

}
