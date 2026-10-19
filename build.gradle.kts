plugins {
    application
}

group = "com.studentapp"
version = "1.0"

application {
    mainClass = "com.studentapp.Main"
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
