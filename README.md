# scala-java-locales

![build](https://github.com/cquiroz/scala-java-locales/workflows/build/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.cquiroz/scala-java-locales_sjs1_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.cquiroz/scala-java-locales_sjs1_2.13)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-1.0.0.svg)](https://www.scala-js.org/)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.29.svg)](https://www.scala-js.org/)

`scala-java-locales` is a clean-room BSD-licensed implementation of the `java.util.Locale` API and related classes as defined on JDK8, mostly for Scala.js usage. It enables the locale API in Scala.js projects and supports usage requiring locales like number and dates formatting.

## Usage

Simply add the following line to your sbt settings:

```scala
libraryDependencies += "io.github.cquiroz" %%% "scala-java-locales" % "1.1.1"
```

If you have a `crossProject`, the setting must be used only in the JS part:

```scala
lazy val myCross = crossProject.
  ...
  .jsSettings(
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-locales" % "1.1.1"
  )
```

**Requirement**: you must use a host JDK8 to _build_ your project, i.e., to
launch sbt. `scala-java-locales` does not work on earlier JDKs.

## Work in Progress / linking errors

This library is a work in progress and there are some unimplemented methods. If you use any of those on your Scala.js code, you will get linking errors.

## Usage

The API follows the Java API for Locales, any major difference should be considered a bug.

The JVM includes a large locales database derived from CLDR. That includes things like date
formats, region names, etc.
Having the full db on js is possible but expensive in terms of space and for most applications
only a few locales are needed, thus it is simpler to have a subset of them using some of the
provided locale dbs or even better via [sbt-locales](http://github.com/cquiroz/sbt-locales)
`sbt-locales` lets you build a custom db with the minimal amount you need. There is a slight
size benefit and a larger speed improvement doing so as scala.js has less code to optimize

For the common cases that you just need date formatting in angling you can just include

```scala
libraryDependencies += "io.github.cquiroz" %%% "locales-minimal-en-db" % "1.1.1"
```

## Default Locale

Starting on 0.6.0 it is no longer necessary to register locales but only a minimal locale based on English is
provided. You may want to use [sbt-locales](https://github.com/cquiroz/sbt-locales) to generate
a custom locale database.

For example see:
[gemini-locales](https://github.com/gemini-hlsw/gemini-locales/)

It is highly recommended to set your default Locale at the start of your application
```
Locale.setDefault(Locale.forLanguageTag(<my-locale>))
```
The Java API requires a default `Locale` though it doesn't mandate a specific one, instead, the runtime should select it depending on the platform.

While the Java Locales use the OS default locale, on `Scala.js` platforms like browsers or node.js, it is harder to identify the default locale . `scala-java-locales` will try to guess the locale but if it can't or it is not not the locales db it sets `en (English)` as the default locale. This is a design decision to support the many API calls that require a default locale. It seems that `Scala.js` _de facto_ uses `en` for number formatting.

## CLDR

`java.util.Locale` is a relatively simple class and by itself it doesn't provide too much functionality. The key for its usefulness is on providing data about the locale especially in terms of classes like `java.text.DecimalFormatSymbols`, `java.text.DateFormatSymbols`, etc. The [Unicode CLDR](http://cldr.unicode.org/) project is a large repository of locale data that can be used to build the supporting classes, e.g. to get the `DecimalFormatSymbols` for a given locale.

Starting on Java 8, [CLDR](https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr) is also used by the JVM, for comparisons the java flag `-Djava.locale.providers=CLDR` should be set.

**Note:** Java 8 ships with an older CLDR version, specifically version 21. `scala-java-locales` uses the latest available version, hence there are some differences between the results and there are new available locales in `scala-java-locales`.

## Disclaimer

Locales and the CLDR specifications are vast subjects. The locales in this project are as good as the data and the interpretation of the specification is. While the data and implementation has been tested as much as possible, it is possible and likely that there are errors. Please post an issue or submit a PR if you find such errors.

In general the API attempts to behave be as close as possible to what happens on the JVM, e.g. the numeric system in Java seems to default to `latn` unless explicitly requested on the locale name.

## Demo
A very simple `Scala.js` project is available at [demo](demo)

## Dependencies

`scala-java-locales` explicitly doesn't have any dependencies.

## Contributors

+ Eric Peters [@er1c](https://github.com/er1c)
+ A. Alonso Dominguez [@alonsodomin](https://github.com/alonsodomin)
+ Marius B. Kotsbak [@mkotsbak](https://github.com/mkotsbak)
+ Timothy Klim [@TimothyKlim](https://github.com/TimothyKlim)
+ Andrea Peruffo [@andreaTP](https://github.com/AndreaTP)
+ Olli Helenius [@liff](https://github.com/liff)

## License

Copyright &copy; 2020 Carlos Quiroz

`scala-java-locales` is distributed under the
[BSD 3-Clause license](./LICENSE.txt).
