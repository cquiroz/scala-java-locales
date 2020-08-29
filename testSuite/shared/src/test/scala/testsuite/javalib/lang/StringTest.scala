package testsuite.javalib.lang

import java.util.Locale
import locales.Implicits._

class StringTest extends munit.FunSuite {
  val hellowWorldLower: String = "helloworld"
  val helloWorldUpper = "HELLOWORLD"

  // Used to swallow unused imports warning on JVM platform
  void(hellowWorldLower)

  test("toUpperCase(Locale)") {
    assertEquals("\u0049", "\u0069".toUpperCase(Locale.ENGLISH))
    assertEquals("\u0130", "\u0069".toUpperCase(new Locale("tr", "")))
  }

  test("toLowerCase(Locale)") {
    assertEquals("\u0069", "\u0049".toLowerCase(Locale.ENGLISH))
    assertEquals("\u0069", "\u0130".toUpperCase(new Locale("tr", "")))
  }
}