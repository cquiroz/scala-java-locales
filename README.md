# scala-java-locales

[![Build Status](https://api.travis-ci.org/cquiroz/scala-java-locales.svg?branch=master)](https://travis-ci.org/cquiroz/scala-java-locales)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.cquiroz/scala-java-locales_sjs0.6_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.cquiroz/scala-java-locales_sjs0.6_2.12)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.17.svg)](https://www.scala-js.org/)

`scala-java-locales` is a clean-room BSD-licensed implementation of the `java.util.Locale` API and related classes as defined on JDK8, mostly for Scala.js usage. It enables the locale API in Scala.js projects and supports usage requiring locales like number and dates formatting.

## Usage

Simply add the following line to your sbt settings:

```scala
libraryDependencies += "com.github.cquiroz" %%% "scala-java-locales" % "0.3.15-cldr35"
```

If you have a `crossProject`, the setting must be used only in the JS part:

```scala
lazy val myCross = crossProject.
  ...
  .jsSettings(
    libraryDependencies += "com.github.cquiroz" %%% "scala-java-locales" % "0.3.15-cldr35"
  )
```

**Requirement**: you must use a host JDK8 to _build_ your project, i.e., to
launch sbt. `scala-java-locales` does not work on earlier JDKs.

## Work in Progress / linking errors

This library is a work in progress and there are some unimplemented methods. If you use any of those on your Scala.js code, you will get linking errors.

## Usage

The API follows the Java API for Locales, any major difference should be considered a bug. However, to avoid loading all the data for locales you need to explicitly install locales you want to use outside the set of standard locales

You can do that by, e.g. installing the Finnish locale

```scala
import locales.LocaleRegistry
import locales.cldr.data.fi_FI

// Install the locale
LocaleRegistry.installLocale(fi_FI)

// Now you can use the locale
val dfs = DecimalFormatSymbols.getInstance(Locale.forLanguageTag("fi_FI"))
```

**_Note:_** that calls to `Locale.forLanguageTag("fi_FI")` will succeed regardless of the installation due to the requirements on the `Locale` API

## Default Locale

It is **highly** recommended that you set a default locale for your application with, e.g.

```
Locale.setDefault(Locale.forLanguageTag(<my-locale>))
```

The Java API requires a default `Locale` though it doesn't mandate a specific one, instead, the runtime should select it depending on the platform.

While the Java Locales use the OS default locale, on `Scala.js` platforms like browsers or node.js, there is no reliable way to identify the default locale. `scala-java-locales` sets `en (English)` as the default locale and **does not** attempt to determine the correct locale for the environment. This is a desigs decision to support the many API calls that require a default locale. It seems that `Scala.js` _de facto_ uses `en` for number formatting.

## CLDR

`java.util.Locale` is a relatively simple class and by itself it doesn't provide too much functionality. The key for its usefulness is on providing data about the locale especially in terms of classes like `java.text.DecimalFormatSymbols`, `java.text.DateFormatSymbols`, etc. The [Unicode CLDR](http://cldr.unicode.org/) project is a large repository of locale data that can be used to build the supporting classes, e.g. to get the `DecimalFormatSymbols` for a given locale.

Most of this project is in the form of code generated from the CLDR data. While many similar projects will create compact text or binary representation, this project will generate class instances for locale. While this maybe larger at first, Scala.js code optimization should be able to remove the unused code during optimization.

Starting on Java 8, [CLDR](https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr) is also used by the JVM, for comparisons the java flag `-Djava.locale.providers=CLDR` should be set.

**Note:** Java 8 ships with an older CLDR version, specifically version 21. `scala-java-locales` uses the latest available version, hence there are some differences between the results and there are new available locales in `scala-java-locales`.

## Disclaimer

Locales and the CLDR specifications are vast subjects. The locales in this project are as good as the data and the interpretation of the specification is. While the data and implementation has been tested as much as possible, it is possible and likely that there are errors. Please post an issue or submit a PR if you find such errors.

In general the API attempts to behave be as close as possible to what happens on the JVM, e.g. the numeric system in Java seems to default to `latn` unless explicitly requested on the locale name.

## Demo

A very simple `Scala.js` project is available at [scalajs-locales-demo](https://github.com/cquiroz/scalajs-locales-demo)

## Dependencies

`scala-java-locales` explicitly doesn't have any dependencies. The `sbt` project has some dependencies for code generation, in particular [treehugger](https://github.com/eed3si9n/treehugger) but they don't carry over to the produced code

## Versioning

`scala-java-locales` uses [Semantic Versioning](http://semver.org/) and includes the CLDR version used as a build tag, e.g.:

```
0.3.15-cldr35 // Version 0.3.15 with CLDR version 35
```

## Contributors

+ Eric Peters [@er1c](https://github.com/er1c)
+ A. Alonso Dominguez [@alonsodomin](https://github.com/alonsodomin)
+ Marius B. Kotsbak [@mkotsbak](https://github.com/mkotsbak)
+ Timothy Klim [@TimothyKlim](https://github.com/TimothyKlim)
+ Andrea Peruffo [@andreaTP](https://github.com/AndreaTP)
+ Olli Helenius [@liff](https://github.com/liff)

## Publishing
=======
on 0.6.28

```
sbt
clean
+publishSigned
++2.11.12
coreNative/publishSigned
sonatyeRelease
```

Important: Remember to clean between different scala.js versions

on 1.0.0-M8

```
SCALAJS_VERSION=1.0.0-M8 sbt
clean
+coreJS/publishSigned
sonatyeRelease
```

## License

Copyright &copy; 2019 Carlos Quiroz

`scala-java-locales` is distributed under the
[BSD 3-Clause license](./LICENSE.txt).
