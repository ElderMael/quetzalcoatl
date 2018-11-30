package io.eldermael.quetzalcoatl.dsl

import static groovy.lang.Closure.DELEGATE_FIRST

class Utils {

  private Utils() {
  }

  static def delegateFirstAndExecute(Closure config, delegate, owner) {
    def configExecution = config.rehydrate(delegate, owner, owner)

    configExecution.resolveStrategy = DELEGATE_FIRST

    configExecution()
  }

}
