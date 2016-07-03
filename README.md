# scalajs-locales

[![Build Status](https://api.travis-ci.org/cquiroz/scalajs-locales.svg?branch=master)](https://travis-ci.org/cquiroz/scalajs-locales)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.8.svg)](https://www.scala-js.org/)

`scalajs-locales` is a clean-room BSD-licensed implementation of the `java.util.Locale` API and related classes as defined on JDK8, mostly for Scala.js usage. It enables the locale API in Scala.js projects and supports usage requiring locales like number and dates formatting.

## Usage

Simply add the following line to your sbt settings:

```scala
libraryDependencies += "com.github.cquiroz" %%% "scalajs-locales" % "0.1.0-SNAPSHOT"
```

If you have a `crossProject`, the setting must be used only in the JS part:

```scala
lazy val myCross = crossProject.
  ...
  jsSettings.(
    libraryDependencies += "com.github.cquiroz" %%% "scalajs-locales" % "0.1.0-SNAPSHOT"
  )
```

**Requirement**: you must use a host JDK8 to *build* your project, i.e., to
launch sbt. `scalajs-locales` does not work on earlier JDKs.

## Work in Progress / linking errors

This library is a work in progress and there are some unimplemented methods. If you use any of those on your Scala.js code, you will get linking errors.

## Usage

The API follows the Java API for Locales, any major difference should be considered a bug. However, to avoid loading all the data for locales you need to explicitly install locales you want to use outside the set of standard locales

You can do that by, e.g. installing the Finnish locale

```scala
import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.cldr.data.fi_FI

// Install the locale
LocaleRegistry.installLocale(fi_FI)

// Now you can use the locale
val dfs = DecimalFormatSymbols.getInstance(Locale.forLanguageTag("fi_FI"))
```

***Note:*** that calls to `Locale.forLanguageTag("fi_FI")` will succeed regardless of the installation due to the requirements on the `Locale` API

## Default Locale

It is **highly** recommended that you set a default locale for your application with, e.g.

```
Locale.setDefault(Locale.forLanguageTag(<my-locale>))
```

The Java API requires a default `Locale` though it doesn't mandate a specific one, instead, the runtime should select it depending on the platform.

On `Scala.js` usual platforms like browsers or node.js, there is no reliable way to identify the default locale, however, `scalajs-locales` uses `en` by default. This is a decision based on compatibility with `Scala.js`, which _de facto_ uses `en` for number formatting.

It could be considered to have no default locale, however, this will greatly limit the ability to implement the many API calls that require it.

## CLDR

`java.util.Locale` is a relatively simple class and by itself it doesn't provide too much functionality. The key for its usefulness is on providing data about the locale especially in terms of classes like `java.text.DecimalFormatSymbols`, `java.text.DateFormatSymbols`, etc. The [Unicode CLDR](http://cldr.unicode.org/) project is a large repository of locale data that can be used to build the supporting classes, e.g. to get the `DecimalFormatSymbols` for a given locale.

Most of this project is in the form of code generated from the CLDR data. While many similar projects will create compact text or binary representation, this project will generate class instances for locale. While this maybe larger at first, Scala.js code optimization should be able to remove the unused code during optimization.

Starting on Java 8, [CLDR](https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr) is also used by the JVM, for comparisons the java flag `-Djava.locale.providers=CLDR` should be set.

**Note:** Java 8 ships with an older CLDR version, specifically version 21. `scalajs-locales` uses the latest available version, hence there are some differences between the results and there are new available locales in `scalajs-locales`.

## Disclaimer

Locales and the CLDR specifications are vast subjects. The locales in this project are as good as the data and the interpretation of the specification is. While the data and implementation has been tested as much as possible, it is possible and likely that there are errors. Please post an issue or submit a PR if you find such errors.

In general the API attempts to behave be as close as possible to what happens on the JVM, e.g. the numeric system in Java seems to default to `latn` unless explicitly requested on the locale name.

## Demo

A very simple sample project is available at [scalajs-locales-demo](https://github.com/cquiroz/scalajs-locales-demo)

## Dependencies

`scalajs-locales` explicitly doesn't have any dependencies. The `sbt` project has some dependencies for code generation, in particular [treehugger](https://github.com/eed3si9n/treehugger) but they don't carry over to the produced code

## License

Copyright &copy; 2016 Carlos Quiroz

`scalaj-locales` is distributed under the
[BSD 3-Clause license](./LICENSE.txt).