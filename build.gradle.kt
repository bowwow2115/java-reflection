// 1. 플러그인 설정: 자바 프로젝트임을 선언
plugins {
    java
}

group = "com.reflection"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}