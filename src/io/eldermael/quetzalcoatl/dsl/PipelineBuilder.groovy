package io.eldermael.quetzalcoatl.dsl

import io.eldermael.quetzalcoatl.dsl.builders.BuildTool

class PipelineBuilder implements Serializable {

  final BuildTool gradle = BuildTool.GRADLE

  String applicationName = null
  BuildTool buildTool = null

  def jenkinsSteps = null

  PipelineBuilder(jenkinsSteps) {
    this.jenkinsSteps = jenkinsSteps
  }

  def executePipeline() {
    this.jenkinsSteps.sh("echo 'other steps'")
  }

}
