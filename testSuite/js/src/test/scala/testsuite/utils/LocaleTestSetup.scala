package testsuite.utils

import java.util.Locale

import locales.LocaleRegistry

class LocaleTestSetup {
  def cleanDatabase: Unit = {
    LocaleRegistry.resetRegistry()
    // Reset the default locale to english
    Locale.setDefault(Locale.ENGLISH)
  }
}
