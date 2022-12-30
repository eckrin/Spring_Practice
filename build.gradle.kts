import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21" //Querydsl
}

group = "yourssu"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//기본
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("mysql:mysql-connector-java")

	//시큐리티
	implementation("org.springframework.boot:spring-boot-starter-security")

	//jwt
//	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	//디버깅
	implementation("org.springframework.ldap:spring-ldap-core")
	implementation("org.springframework.security:spring-security-ldap")
	implementation("com.unboundid:unboundid-ldapsdk")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	//유효성 검사
	implementation("org.springframework.boot:spring-boot-starter-validation")
	//테스트 관련
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	implementation("io.springfox:springfox-boot-starter:3.0.0")
//	implementation("io.springfox:springfox-swagger-ui:3.0.0")

	//querydsl
	implementation ("com.querydsl:querydsl-jpa:5.0.0")
	annotationProcessor ("javax.persistence:javax.persistence-api")
	annotationProcessor ("javax.annotation:javax.annotation-api")
	annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jpa") //이것때문에 컴파일러 에러 발생

	api("com.querydsl:querydsl-jpa")
	kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
}

//querydsl
sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
	enabled = false
}
