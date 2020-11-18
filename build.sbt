import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import sbt.Keys._
import locales._

val cldrDbVersion = settingKey[String]("The version of CLDR used.")

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers in Global += Resolver.sonatypeRepo("public")

val scalaJSVersion06 = Option(System.getenv("SCALAJS_VERSION")).exists(_.startsWith("0.6"))

val commonSettings: Seq[Setting[_]] = Seq(
  organization := "io.github.cquiroz",
  scalaVersion := "2.13.3",
  crossScalaVersions := Seq("2.11.12", "2.12.11", "2.13.3"),
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
                url("https://github.com/cquiroz")
      ),
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
             localesFullCurrenciesDb.js,
             localesMinimalEnDb.js,
             demo
  )

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .settings(commonSettings: _*)
  .settings(
    name := "scala-java-locales",
    libraryDependencies ++= Seq(
      "io.github.cquiroz"     %%% "cldr-api"                % "1.0.0",
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.2.0"
    ),
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )
  .jsSettings(
    scalacOptions ++= {
      if (scalaJSVersion06) Seq.empty else Seq("-P:scalajs:genStaticForwardersForNonTopLevelObjects")
    },
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
  .jvmSettings(
    skip.in(publish) := scalaJSVersion06
  )

lazy val localesFullCurrenciesDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    cldrDbVersion := "36",
    name := "locales-full-currencies-db",
    cldrVersion := CLDRVersion.Version(cldrDbVersion.value),
    localesFilter := LocalesFilter.All,
    nsFilter := NumberingSystemFilter.All,
    calendarFilter := CalendarFilter.All,
    currencyFilter := CurrencyFilter.All,
    supportDateTimeFormats := true,
    supportNumberFormats := true,
    supportISOCodes := true,
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )
  .jvmSettings(
    skip.in(publish) := scalaJSVersion06
  )

lazy val localesFullDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    cldrDbVersion := "36",
    name := "locales-full-db",
    cldrVersion := CLDRVersion.Version(cldrDbVersion.value),
    localesFilter := LocalesFilter.All,
    nsFilter := NumberingSystemFilter.All,
    calendarFilter := CalendarFilter.All,
    currencyFilter := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats := true,
    supportISOCodes := true,
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )
  .jvmSettings(
    skip.in(publish) := scalaJSVersion06
  )

lazy val localesMinimalEnDb = crossProject(JVMPlatform, JSPlatform)
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    cldrDbVersion := "36",
    name := "locales-minimal-en-db",
    cldrVersion := CLDRVersion.Version(cldrDbVersion.value),
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
    libraryDependencies += "org.scalameta" %%% "munit" % "0.7.18" % Test,
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
              scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )
  .jsConfigure(_.dependsOn(core.js, macroUtils, localesFullCurrenciesDb.js))
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
    libraryDependencies += "io.github.cquiroz" %%% "cldr-api" % "1.0.0"
  )
  .jvmConfigure(_.dependsOn(macroUtils))

lazy val macroUtils = project
  .in(file("macroUtils"))
  .settings(commonSettings)
  .settings(
    name := "macroutils",
    organization := "io.github.cquiroz",
    version := "0.0.1",
    libraryDependencies := Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value),
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )

lazy val demo = project
  .in(file("demo"))
  .configure(_.enablePlugins(ScalaJSPlugin))
  .settings(commonSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    scalaJSUseMainModuleInitializer := true,
    name := "scala-java-locales demo"
  )
  .dependsOn(core.js)
