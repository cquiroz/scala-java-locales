val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("0.6.22")

addSbtPlugin("org.portable-scala" % "sbt-crossproject" % "0.3.1")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersion)

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.3.1")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.3.6")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

addSbtPlugin("org.scalastyle" % "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

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
