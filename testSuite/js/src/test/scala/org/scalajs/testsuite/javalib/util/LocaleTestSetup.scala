package org.scalajs.testsuite.javalib.util

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.js.debugger

class LocaleTestSetup {
  def cleanDatabase: Unit = {
    LocaleRegistry.resetRegistry()
  }
}
