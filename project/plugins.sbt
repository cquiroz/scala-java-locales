val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("0.6.20")

addSbtPlugin("org.scala-native" % "sbt-crossproject" % "0.2.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersion)

// For sbt-crossproject support even with Scala.js 0.6.x
{
  if (scalaJSVersion.startsWith("0.6."))
    Seq(addSbtPlugin("org.scala-native" % "sbt-scalajs-crossproject" % "0.2.2"))
  else
    Nil
}

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.3.3")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

addSbtPlugin("org.scalastyle" % "scalastyle-sbt-plugin" % "0.9.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

// Dependencies for custom tasks
libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.5")
    case _ =>
      libraryDependencies.value
  }
}

libraryDependencies ++= Seq(
  "com.eed3si9n" %% "treehugger" % "0.4.3"
)
