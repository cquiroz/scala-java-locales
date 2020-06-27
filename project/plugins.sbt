resolvers += Resolver.sonatypeRepo("public")

val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.1.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersion)
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("io.github.cquiroz" % "sbt-locales" % "1.0.0")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.3")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.13")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
