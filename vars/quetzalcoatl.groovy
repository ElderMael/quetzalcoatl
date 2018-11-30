import io.eldermael.quetzalcoatl.dsl.PipelineBuilder

def call(Closure closure) {

  PipelineBuilder builder = new PipelineBuilder(this)

  def code = closure.rehydrate(builder, builder, builder)

  code.resolveStrategy = Closure.DELEGATE_FIRST

  code()

  println("Application name: ${builder.applicationName}")
  println("Tool: ${builder.buildTool}")

  sh("./${builder.buildTool.toolBinary} :${builder.applicationName}:build")

  builder.executePipeline()

}