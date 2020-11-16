resolvers += Resolver.sonatypeRepo("public")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.3.1")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("io.github.cquiroz" % "sbt-locales" % "1.0.0")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.5")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.16")
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.6")
