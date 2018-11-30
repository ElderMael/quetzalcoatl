package io.eldermael.quetzalcoatl.dsl.builders

enum BuildTool {

  GRADLE("./gradlew"),
  MAVEN("./mvnw"),
  NPM("mvn")

  String toolBinary

  BuildTool(String toolBinary) {
    this.toolBinary = toolBinary
  }

  String getToolBinary() {
    this.toolBinary
  }


}