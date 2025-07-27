plugins {
    kotlin("jvm")
    application
}

// repositories are configured in settings.gradle.kts

dependencies {
    // AWS CDK v2
    implementation("software.amazon.awscdk:aws-cdk-lib:2.106.1")
    implementation("software.constructs:constructs:10.3.0")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

application {
    mainClass.set("MusicPlayerAppKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}