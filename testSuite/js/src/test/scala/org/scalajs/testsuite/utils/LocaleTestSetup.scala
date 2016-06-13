package org.scalajs.testsuite.utils

import scala.scalajs.LocaleRegistry

class LocaleTestSetup {
  def cleanDatabase: Unit = {
    LocaleRegistry.resetRegistry()
  }
}
