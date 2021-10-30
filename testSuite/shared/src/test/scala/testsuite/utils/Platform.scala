/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js Test Suite        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */
package testsuite.utils

import scala.util.Properties

object Platform {

  /** Returns `true` if and only if the code is executing on a JVM. Note: Returns `false` when
    * executing on any other platform.
    */
  final val executingInJVM = Properties.javaVmName match {
    case "Scala Native" | "Scala.js" => false
    case _                           => true
  }

  final val executingInJVMOnJDK6 = executingInJVM && jdkVersion == 6

  final val executingInJVMOnJDK7OrLower = executingInJVM && jdkVersion <= 7

  final val executingInScalaNative = Properties.javaVmName == "Scala Native"

  private lazy val jdkVersion = {
    val v = Properties.javaVersion
    if (v.startsWith("1.")) Integer.parseInt(v.drop(2).takeWhile(_.isDigit))
    else throw new Exception("Unknown java.version format")
  }

}
