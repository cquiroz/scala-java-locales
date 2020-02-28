import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import sbt.Keys._
import locales._

val cldrVersion = settingKey[String]("The version of CLDR used.")

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers in Global += Resolver.sonatypeRepo("public")

val commonSettings: Seq[Setting[_]] = Seq(
  cldrVersion := "36",
  version := s"0.6.0-cldr${cldrVersion.value}-SNAPSHOT",
  organization := "io.github.cquiroz",
  scalaVersion := "2.13.1",
  crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1"),
  scalacOptions ~= (_.filterNot(
    Set(
      "-Wdead-code",
      "-Ywarn-dead-code",
      "-Wunused:params",
      "-Ywarn-unused:params",
      "-Wvalue-discard",
      "-Ywarn-value-discard"
    )
  )),
  scalacOptions in (Compile, doc) := Seq()
)

inThisBuild(
  List(
    organization := "io.github.cquiroz",
    homepage := Some(url("https://github.com/cquiroz/scala-java-locales")),
    licenses := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
    developers := List(
      Developer("cquiroz",
                "Carlos Quiroz",
                "carlos.m.quiroz@gmail.com",
                url("https://github.com/cquiroz")),
      Developer("er1c", "Eric Peters", "", url("https://github.com/er1c")),
      Developer("alonsodomin", "A. Alonso Dominguez", "", url("https://github.com/alonsodomin")),
      Developer("mkotsbak", "Marius B. Kotsbak", "", url("https://github.com/mkotsbak")),
      Developer("TimothyKlim", "Timothy Klim", "", url("https://github.com/TimothyKlim"))
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/cquiroz/scala-java-locales"),
        "scm:git:git@github.com:cquiroz/scala-java-locales.git"
      )
    )
  )
)

lazy val scalajs_locales: Project = project
  .in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "locales",
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
  // don't include scala-native by default
  .aggregate(core.js,
             core.jvm,
             testSuite.js,
             testSuite.jvm,
             localesFullDb.js,
             localesMinimalEnDb.js)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(commonSettings: _*)
  .settings(
    name := "scala-java-locales",
    libraryDependencies += "io.github.cquiroz" %%% "cldr-api" % "0.0.8",
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )
  .jsSettings(
    scalacOptions ++= {
      val tagOrHash =
        if (isSnapshot.value) sys.process.Process("git rev-parse HEAD").lineStream_!.head
        else s"v${version.value}"
      (sourceDirectories in Compile).value.map { dir =>
        val a = dir.toURI.toString
        val g =
          "https://raw.githubusercontent.com/cquiroz/scala-java-locales/" + tagOrHash + "/core/src/main/scala"
        s"-P:scalajs:mapSourceURI:$a->$g/"
      }
    }
  )
  .nativeSettings(
    sources in (Compile, doc) := Seq.empty
  )

lazy val localesFullCurrenciesDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name := "locales-full-currencies-db",
    dbVersion := CLDRVersion.Version(cldrVersion.value),
    localesFilter := LocalesFilter.All,
    nsFilter := NumberingSystemFilter.All,
    calendarFilter := CalendarFilter.All,
    currencyFilter := CurrencyFilter.All,
    supportDateTimeFormats := true,
    supportNumberFormats := true,
    supportISOCodes := true,
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )

lazy val localesFullDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name := "locales-full-db",
    dbVersion := CLDRVersion.Version(cldrVersion.value),
    localesFilter := LocalesFilter.All,
    nsFilter := NumberingSystemFilter.All,
    calendarFilter := CalendarFilter.All,
    currencyFilter := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats := true,
    supportISOCodes := true,
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )

lazy val localesMinimalEnDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name := "locales-minimal-en-db",
    dbVersion := CLDRVersion.Version(cldrVersion.value),
    localesFilter := LocalesFilter.Minimal,
    nsFilter := NumberingSystemFilter.Minimal,
    calendarFilter := CalendarFilter.Minimal,
    currencyFilter := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats := true,
    supportISOCodes := false,
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )

lazy val testSuite = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    name := "scala-java-locales test",
    libraryDependencies += "org.scalameta" %%% "munit" % "0.5.2",
    testFrameworks += new TestFramework("munit.Framework"),
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )
  .jsSettings(parallelExecution in Test := false,
              name := "scala-java-locales testSuite on JS",
              scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .jsConfigure(_.dependsOn(core.js, macroUtils, localesFullDb.js))
  .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
  .jvmSettings(
    // Fork the JVM test to ensure that the custom flags are set
    fork in Test := true,
    // Use CLDR provider for locales
    // https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr
    javaOptions in Test ++= Seq(
      "-Duser.language=en",
      "-Duser.country=",
      "-Djava.locale.providers=CLDR",
      "-Dfile.encoding=UTF8"
    ),
    name := "scala-java-locales testSuite on JVM",
    libraryDependencies += "io.github.cquiroz" %%% "cldr-api" % "0.0.8"
  )
  .jvmConfigure(_.dependsOn(macroUtils))

lazy val macroUtils = project
  .in(file("macroUtils"))
  .settings(commonSettings)
  .settings(
    name := "macroutils",
    organization := "io.github.cquiroz",
    version := "0.0.1",
    libraryDependencies := {
      Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value) ++ {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
          case Some((2, scalaMajor)) if scalaMajor >= 11 =>
            libraryDependencies.value
          // in Scala 2.10, quasiquotes are provided by macro paradise
          case Some((2, 10)) =>
            libraryDependencies.value ++ Seq(
              compilerPlugin(("org.scalamacros" % "paradise" % "2.1.0").cross(CrossVersion.full)),
              ("org.scalamacros" %% "quasiquotes" % "2.1.0").cross(CrossVersion.binary)
            )
        }
      }
    },
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )
