/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js Test Suite        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */
package testsuite.utils

object Platform {

  /** Returns `true` if and only if the code is executing on a JVM. Note: Returns `false` when
    * executing on any other platform.
    */
  final val executingInJVM = System.getProperty("java.vm.name") match {
    case "Scala Native" | "Scala.js" => false
    case _                           => true
  }

  final val executingInScalaNative = System.getProperty("java.vm.name") == "Scala Native"

}
