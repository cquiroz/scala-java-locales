import locales._
import sbt.Keys._
import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }

lazy val cldrApiVersion = "3.1.0"

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers in Global += Resolver.sonatypeRepo("public")

lazy val scalaVersion213 = "2.13.8"
lazy val scalaVersion3   = "3.1.2"
ThisBuild / scalaVersion       := scalaVersion213
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.15", scalaVersion213, scalaVersion3)

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches +=
  RefPredicate.StartsWith(Ref.Tag("v"))

lazy val java17 = JavaSpec.temurin("17")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("8"), java17)

ThisBuild / githubWorkflowBuildPreamble +=
  WorkflowStep.Run(
    List("sudo apt-get install libutf8proc-dev"),
    name = Some("Install libutf8proc")
  )
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    env = Map(
      "PGP_PASSPHRASE"    -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET"        -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)

// https://github.com/scala-native/scala-native/issues/2611
ThisBuild / githubWorkflowBuildMatrixExclusions ++= List(
  MatrixExclude(
    Map(
      "scala" -> scalaVersion3,
      "java"  -> java17.render
    )
  )
)

ThisBuild / githubWorkflowBuildMatrixFailFast := Some(false)

ThisBuild / Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat

val commonSettings: Seq[Setting[_]] = Seq(
  organization                  := "io.github.cquiroz",
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
  Compile / doc / scalacOptions := Seq(),
  // Compile / doc / sources       := { if (isDotty.value) Seq() else (Compile / doc / sources).value },
  Compile / unmanagedSourceDirectories ++= scalaVersionSpecificFolders("main",
                                                                       baseDirectory.value,
                                                                       scalaVersion.value
  ),
  Test / unmanagedSourceDirectories ++= scalaVersionSpecificFolders("test",
                                                                    baseDirectory.value,
                                                                    scalaVersion.value
  )
)

inThisBuild(
  List(
    organization := "io.github.cquiroz",
    homepage     := Some(url("https://github.com/cquiroz/scala-java-locales")),
    licenses     := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
    developers   := List(
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
    scmInfo      := Some(
      ScmInfo(
        url("https://github.com/cquiroz/scala-java-locales"),
        "scm:git:git@github.com:cquiroz/scala-java-locales.git"
      )
    )
  )
)

def scalaVersionSpecificFolders(srcName: String, srcBaseDir: java.io.File, scalaVersion: String) = {
  def extraDirs(suffix: String) =
    List(CrossType.Pure, CrossType.Full)
      .flatMap(_.sharedSrcDir(srcBaseDir, srcName).toList.map(f => file(f.getPath + suffix)))
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, y))     => extraDirs("-2.x") ++ (if (y >= 13) extraDirs("-2.13+") else Nil)
    case Some((0 | 3, _)) => extraDirs("-2.13+") ++ extraDirs("-3.x")
    case _                => Nil
  }
}

lazy val scalajs_locales: Project = project
  .in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name               := "locales",
    publish            := {},
    publishLocal       := {},
    publishArtifact    := false,
    crossScalaVersions := Nil
  )
  .aggregate(
    core.js,
    core.jvm,
    core.native,
    testSuite.js,
    testSuite.jvm,
    testSuite.native,
    localesFullDb.js,
    localesFullDb.native,
    localesFullCurrenciesDb,
    localesMinimalEnDb.js,
    localesMinimalEnDb.native,
    localesMinimalEnUSDb.js,
    localesMinimalEnUSDb.native,
    demo.js,
    demo.native
  )

def isScala3 = Def.task {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) =>
      true
    case _            =>
      false
  }
}

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "scala-java-locales",
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "cldr-api" % cldrApiVersion
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
      if (isScala3.value) Seq("-scalajs-genStaticForwardersForNonTopLevelObjects")
      else Seq("-P:scalajs:genStaticForwardersForNonTopLevelObjects")
    },
    scalacOptions ++= {
      if (isScala3.value) Seq.empty
      else {
        val tagOrHash =
          if (isSnapshot.value) sys.process.Process("git rev-parse HEAD").lineStream_!.head
          else s"v${version.value}"
        (Compile / sourceDirectories).value.map { dir =>
          val a = dir.toURI.toString
          val g =
            "https://raw.githubusercontent.com/cquiroz/scala-java-locales/" + tagOrHash + "/core/src/main/scala"
          s"-P:scalajs:mapSourceURI:$a->$g/"
        }
      }
    }
  )

lazy val cldrDbVersion = "36.0"

lazy val localesFullCurrenciesDb = project
  .in(file("localesFullCurrenciesDb"))
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .configure(_.enablePlugins(ScalaJSPlugin))
  .settings(
    name                   := "locales-full-currencies-db",
    cldrVersion            := CLDRVersion.Version(cldrDbVersion),
    localesFilter          := LocalesFilter.All,
    nsFilter               := NumberingSystemFilter.All,
    calendarFilter         := CalendarFilter.All,
    currencyFilter         := CurrencyFilter.All,
    supportDateTimeFormats := true,
    supportNumberFormats   := true,
    supportISOCodes        := true,
    libraryDependencies += ("org.portable-scala" %%% "portable-scala-reflect" % "1.1.2")
      .cross(CrossVersion.for3Use2_13)
  )

lazy val localesFullDb = crossProject(JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("localesFullDb"))
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name                   := "locales-full-db",
    cldrVersion            := CLDRVersion.Version(cldrDbVersion),
    localesFilter          := LocalesFilter.All,
    nsFilter               := NumberingSystemFilter.All,
    calendarFilter         := CalendarFilter.All,
    currencyFilter         := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats   := true,
    supportISOCodes        := true,
    libraryDependencies += ("org.portable-scala" %%% "portable-scala-reflect" % "1.1.2")
      .cross(CrossVersion.for3Use2_13)
  )

lazy val localesMinimalEnDb = crossProject(JSPlatform, NativePlatform)
  .in(file("localesMinimalEnDb"))
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name                   := "locales-minimal-en-db",
    cldrVersion            := CLDRVersion.Version(cldrDbVersion),
    localesFilter          := LocalesFilter.Minimal,
    nsFilter               := NumberingSystemFilter.Minimal,
    calendarFilter         := CalendarFilter.Minimal,
    currencyFilter         := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats   := true,
    supportISOCodes        := false,
    libraryDependencies += ("org.portable-scala" %%% "portable-scala-reflect" % "1.1.2")
      .cross(CrossVersion.for3Use2_13)
  )

lazy val localesMinimalEnUSDb = crossProject(JSPlatform, NativePlatform)
  .in(file("localesMinimalEnUSDb"))
  .settings(commonSettings: _*)
  .configure(_.enablePlugins(LocalesPlugin))
  .settings(
    name                   := "locales-minimal-en_US-db",
    cldrVersion            := CLDRVersion.Version(cldrDbVersion),
    localesFilter          := LocalesFilter.Selection(List("en_US")),
    nsFilter               := NumberingSystemFilter.Minimal,
    calendarFilter         := CalendarFilter.Minimal,
    currencyFilter         := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats   := true,
    supportISOCodes        := false,
    libraryDependencies += ("org.portable-scala" %%% "portable-scala-reflect" % "1.1.2")
      .cross(CrossVersion.for3Use2_13)
  )

lazy val testSuite = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .in(file("testSuite"))
  .settings(commonSettings: _*)
  .settings(
    publish                                 := {},
    publishLocal                            := {},
    publishArtifact                         := false,
    name                                    := "scala-java-locales test",
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0-M4" % Test,
    testFrameworks += new TestFramework("munit.Framework"),
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    ))
  )
  .jsSettings(Test / parallelExecution := false,
              name                     := "scala-java-locales testSuite on JS",
              scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )
  .jsConfigure(_.dependsOn(core.js, macroUtils, localesFullCurrenciesDb))
  .jvmSettings(
    // Fork the JVM test to ensure that the custom flags are set
    Test / fork                                 := true,
    // Use CLDR provider for locales
    // https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr
    Test / javaOptions ++= Seq(
      "-Duser.language=en",
      "-Duser.country=",
      "-Djava.locale.providers=CLDR",
      "-Dfile.encoding=UTF8",
      "-Xmx6G"
    ),
    name                                        := "scala-java-locales testSuite on JVM",
    libraryDependencies += "io.github.cquiroz" %%% "cldr-api" % cldrApiVersion
  )
  .jvmConfigure(_.dependsOn(macroUtils))
  .nativeSettings(
    nativeConfig ~= {
      _.withOptimize(false)
      // tests fail to link on Scala 2.11 and 2.12 in debug mode
      // with the optimizer enabled
    }
  )
  .nativeConfigure(_.dependsOn(core.native, macroUtils, localesFullDb.native))
  .platformsSettings(JSPlatform, NativePlatform)(
    Test / unmanagedSourceDirectories += baseDirectory.value.getParentFile / "js-native" / "src" / "test" / "scala"
  )
  .platformsSettings(JSPlatform, JVMPlatform)(
    Test / unmanagedSourceDirectories += baseDirectory.value.getParentFile / "js-jvm" / "src" / "test" / "scala"
  )

lazy val macroUtils = project
  .in(file("macroUtils"))
  .settings(commonSettings)
  .settings(
    name                    := "macroutils",
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) =>
          Seq.empty
        case _            =>
          Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
      }
    },
    scalacOptions ~= (_.filterNot(
      Set(
        "-deprecation",
        "-Xfatal-warnings"
      )
    )),
    Compile / doc / sources := { if (isScala3.value) Seq() else (Compile / doc / sources).value }
  )

lazy val demo = crossProject(JSPlatform, NativePlatform)
  .in(file("demo"))
  .settings(commonSettings: _*)
  .settings(
    publish                         := {},
    publishLocal                    := {},
    publishArtifact                 := false,
    scalaJSUseMainModuleInitializer := true,
    name                            := "scala-java-locales demo"
  )
  .dependsOn(core)
