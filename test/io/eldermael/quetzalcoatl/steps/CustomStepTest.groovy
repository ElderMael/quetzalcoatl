package io.eldermael.quetzalcoatl.steps

import com.lesfurets.jenkins.unit.BasePipelineTest
import com.lesfurets.jenkins.unit.global.lib.SourceRetriever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import static groovy.test.GroovyAssert.*

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library

class CanaryTest extends BasePipelineTest {

  @Override
  @BeforeEach
  void setUp() {
    super.setUp()

    registerPipelineLibrary()
    registerAllCustomSteps()
  }

  @AfterEach
  void printStack() {
    printCallStack()
  }

  @Test
  void shouldExecuteCustomStepAndReturnResult() {
    Script sum = loadCustomStep("sum")

    def sumResult = sum(1, 2)

    assertEquals(3, sumResult)
  }

  @Test
  void shouldExecuteJenkinsShellStepWithSentMessage() {
    Script printShell = loadCustomStep("printShell")

    printShell("message")

    def shellInvocation = helper.callStack.findAll {
      it.methodName == "sh"
    }.first()

    def shellArgument = shellInvocation.args.first()

    assertTrue(shellArgument == "echo 'message'")

  }

  @Test
  void shouldRunJenkinsfileWithCustomPipelineLibrary() {

    def script = runPipelineScript("pipelineSample")
    script.setProperty("sh", {

      if(it == "wrong script") {
        throw new RuntimeException("Unexpected script")
      }

      println("Running shell script with contents: ${it}")
    })

    script.run()

  }

  Script loadCustomStep(String name) {
    loadScript("vars/" + name + ".groovy")
  }

  Script runPipelineScript(String fileName) {
    this.loadScript(
        "test/io/eldermael/quetzalcoatl/steps/${fileName}.Jenkinsfile"
    )
  }

  private registerAllCustomSteps() {
    new File("vars").eachFile { file ->
      def name = file.name.replace(".groovy", "")

      // register step with no args
      // example: toAlphanumeric()
      helper.registerAllowedMethod(name, []) { ->
        loadScript(file.path)()
      }

      // register step with Map arg
      // example: toAlphanumeric(text: "a")
      helper.registerAllowedMethod(name, [Map]) { opts ->
        loadScript(file.path)(opts)
      }
    }
  }

  private void registerPipelineLibrary() {
    def library = library()
        .name('quetzalcoatl')
        .retriever({ String repository, String branch, String targetPath ->
          def url = new File(System.getProperty('user.dir')).toURI().toURL()
          println("Using URL for loading library '${url}'")
          [url]
        } as SourceRetriever)
        .targetPath('/tmp/clone')
        .defaultVersion("master")
        .allowOverride(true)
        .implicit(false)
        .build()

    helper.registerSharedLibrary(library)
  }

}