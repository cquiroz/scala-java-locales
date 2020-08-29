package testsuite.javalib.lang

import locales.Implicits._

class CharacterTest extends munit.FunSuite {
  val hellowWorldLower: String = "helloworld"
  val helloWorldUpper = "HELLOWORLD"

  // Used to swallow unused imports warning on JVM platform
  void(hellowWorldLower)

}