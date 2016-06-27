package org.scalajs.testsuite.utils

import java.util.Locale

import scala.scalajs.locale.LocaleRegistry

class LocaleTestSetup {
  def cleanDatabase: Unit = {
    LocaleRegistry.resetRegistry()
    // Reset the default locale to english
    Locale.setDefault(Locale.ENGLISH)
  }
}
