package org.scalajs.testsuite.utils

import java.util.Locale

class LocaleTestSetup {
  def cleanDatabase: Unit = {
    Locale.setDefault(Locale.ENGLISH)
  }
}
