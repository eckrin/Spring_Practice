import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//왜 gradle에 compileQuerydsl이 안들어오지?
//querydsl
//buildscript {
//	repositories {
//		maven("https://plugins.gradle.org/m2/")
//		mavenCentral()
//	}
//
//	dependencies {
//		classpath("gradle.plugin.com.ewerk.gradle.plugins:querydsl-plugin:1.0.10")
//		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
//	}
//}

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21" //Querydsl
//	idea //querydsl
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

//	//querydsl
//	api("com.querydsl:querydsl-jpa:5.0.0")
//	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	implementation("com.querydsl:querydsl-jpa:5.0.0")
//	kapt("com.querydsl:querydsl-apt:5.0.0:jpa") //jpaannotationprocessor 설정
//	kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

	//querydsl2
	implementation ("com.querydsl:querydsl-jpa:4.4.0")
	//querydsl-apt가 어노테이션을 알 수 있도록 추가
	annotationProcessor ("javax.persistence:javax.persistence-api")
	annotationProcessor ("javax.annotation:javax.annotation-api")
	annotationProcessor ("com.querydsl:querydsl-apt:4.4.0:jpa")
//	annotationProcessor ("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

	//querydsl3 - Q클래스 생성!
	api("com.querydsl:querydsl-jpa")
	kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")

}

////querydsl: 생성된 QClass들을 intelliJ IDEA가 사용할 수 있도록 소스코드 경로에 추가해 준다.
//idea {
//	module {
//		val kaptMain = file("build/generated/source/kapt/main")
//		sourceDirs.add(kaptMain)
//		generatedSourceDirs.add(kaptMain)
//	}
//}

//querydsl
sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class){
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

