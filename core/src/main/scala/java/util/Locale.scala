package java.util

import scala.collection.{Map => SMap, Set => SSet}
import scala.collection.JavaConverters._
import scala.util.matching.Regex

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.ldml.isocodes

object Locale {

  // Default locales required by the specs
  val ENGLISH: Locale = LocaleRegistry.en.toLocale
  val FRENCH: Locale = LocaleRegistry.fr.toLocale
  val GERMAN: Locale = LocaleRegistry.de.toLocale
  val ITALIAN: Locale = LocaleRegistry.it.toLocale
  val JAPANESE: Locale = LocaleRegistry.ja.toLocale
  val KOREAN: Locale = LocaleRegistry.ko.toLocale
  val CHINESE: Locale = LocaleRegistry.zh.toLocale
  val SIMPLIFIED_CHINESE: Locale = LocaleRegistry.zh_CN_Hans.toLocale
  val TRADITIONAL_CHINESE: Locale = LocaleRegistry.zh_TW_Hant.toLocale
  val FRANCE: Locale = LocaleRegistry.fr_FR.toLocale
  val GERMANY: Locale = LocaleRegistry.de_DE.toLocale
  val ITALY: Locale = LocaleRegistry.it_IT.toLocale
  val JAPAN: Locale = LocaleRegistry.ja_JP.toLocale
  val KOREA: Locale = LocaleRegistry.ko_KR.toLocale
  val CHINA: Locale = LocaleRegistry.zh_CN_Hans.toLocale
  val PRC: Locale = LocaleRegistry.zh_CN_Hans.toLocale
  val TAIWAN: Locale = LocaleRegistry.zh_TW_Hant.toLocale
  val UK: Locale = LocaleRegistry.en_GB.toLocale
  val US: Locale = LocaleRegistry.en_US.toLocale
  val CANADA: Locale = LocaleRegistry.en_CA.toLocale
  val CANADA_FRENCH: Locale = LocaleRegistry.fr_CA.toLocale
  val ROOT: Locale = new Locale("", "", "")

  val PRIVATE_USE_EXTENSION: Char = 'x'
  val UNICODE_LOCALE_EXTENSION: Char = 'u'

  final class Category private (name: String, ordinal: Int)
    extends Enum[Category](name, ordinal)

  object Category {
    val DISPLAY = new Category("DISPLAY", 0)
    val FORMAT  = new Category("FORMAT", 1)

    private val categories = Array(DISPLAY, FORMAT)

    def values(): Array[Category] = categories

    def valueOf(name: String): Category = categories.find(_.name == name)
      .getOrElse {
        if (name == null)
          throw new NullPointerException("Argument cannot be null")
        else
          throw new IllegalArgumentException(s"No such category: $name")
      }
  }

  private[util] def checkRegex(regex: Regex, s: String): Boolean =
    s match {
      case regex() => true
      case _       => false
    }

  private[util] def checkLanguage(l: String): Boolean =
    checkRegex("[a-zA-Z]{2,8}".r, l)

  private[util] def checkScript(l: String): Boolean =
    checkRegex("[a-zA-Z]{4}".r, l)

  private[util] def checkRegion(l: String): Boolean =
    checkRegex("[a-zA-Z]{2}".r, l) || checkRegex("[0-9]{3}".r, l)

  private[util] def checkAcceptableVariantSegment(l: String): Boolean =
    checkRegex("[0-9a-zA-Z]{1,8}".r, l)

  private[util] def checkVariantSegment(l: String): Boolean = {
    checkRegex("[0-9][0-9a-zA-Z]{3}".r, l) ||
    checkRegex("[0-9a-zA-Z]{5,8}".r, l)
  }

  private[util] def checkVariant(l: String): Boolean = {
    val parts = l.split("-|_")
    parts.forall(checkVariantSegment)
  }

  private[util] def checkExtKey(key: Char): Boolean = key.isLetterOrDigit

  private[util] def checkExtValue(value: String): Boolean =
    value.split("-").forall(checkRegex("[0-9a-zA-Z]{2,8}".r, _))

  private[util] def checkUnicodeKey(l: String): Boolean =
    checkRegex("[a-zA-Z]{2}".r, l)

  private[util] def checkUnicodeType(l: String): Boolean =
    l.isEmpty || l.split("-").forall(checkRegex("[0-9a-zA-Z]{3,8}".r, _))

  private[util] def checkAttribute(l: String): Boolean =
    checkRegex("[0-9a-zA-Z]{3,8}".r, l)

  class Builder () {

    private case class BuilderParams(language: Option[String] = None,
       region: Option[String]                  = None,
       variant: Option[String]                 = None,
       script: Option[String]                  = None,
       extensions: SMap[Char, String]          = SMap.empty,
       unicodeExtensions: SMap[String, String] = SMap.empty,
       unicodeAttributes: SSet[String]         = SSet.empty)

    private[this] var params = BuilderParams()

    def setLocale(locale: Locale): Builder = ???

    def setLanguageTag(languageTag: String): Builder = ???

    def setLanguage(language: String): Builder =
      if (language == null || language.isEmpty) {
        params = params.copy(language = None)
        this
      } else if (checkLanguage(language)) {
        params = params.copy(language = Some(language.toLowerCase))
        this
      } else {
        throw new IllformedLocaleException(s"Invalid language $language")
      }

    def setScript(script: String): Builder =
      if (script == null || script.isEmpty) {
        params = params.copy(script = None)
        this
      } else if (checkScript(script)) {
        // Script must be canonicalized
        params = params.copy(script =
          Some(script.charAt(0).toUpper + script.substring(1)))
        this
      } else {
        throw new IllformedLocaleException(s"Invalid script $script")
      }

    def setRegion(region: String): Builder =
      if (region == null || region.isEmpty) {
        params = params.copy(region = None)
        this
      } else if (checkRegion(region)) {
        params = params.copy(region = Some(region.toUpperCase))
        this
      } else {
        throw new IllformedLocaleException(s"Invalid region $region")
      }

    def setVariant(variant: String): Builder =
      if (variant == null || variant.isEmpty) {
        params = params.copy(variant = None)
        this
      } else if (checkVariant(variant)) {
        params = params.copy(variant = Some(variant.replace("-", "_")))
        this
      } else {
        throw new IllformedLocaleException(s"Invalid variant $variant")
      }

    def setExtension(key: Char, value: String): Builder =
      if (params.extensions.contains(key) || (value == null || value.isEmpty)) {
        // remove
        params = params.copy(extensions = params.extensions - key)
        this
      } else if (key == UNICODE_LOCALE_EXTENSION) {
        // replace all unicode extensions
        params = params.copy(extensions =
          params.extensions + (key -> value.toLowerCase),
            unicodeExtensions = SMap.empty)
        this
      } else if (checkExtKey(key) && checkExtValue(value)) {
        params = params.copy(extensions =
          params.extensions + (key -> value.toLowerCase))
        this
      } else {
        throw new IllformedLocaleException(s"Invalid extension $key: $value")
      }

    def setUnicodeLocaleKeyword(key: String, _type: String): Builder = {
      if (key == null) {
        throw new NullPointerException("Null unicode extension key")
      } else if (checkUnicodeKey(key) && checkUnicodeType(_type)) {
        params = params.copy(unicodeExtensions =
          params.unicodeExtensions + (key -> _type))
        this
      } else {
        throw new IllformedLocaleException(
          s"Invalid unicode keyword $key: ${_type}")
      }
    }

    def addUnicodeLocaleAttribute(attribute: String): Builder = {
      if (attribute == null) {
        throw new NullPointerException("Null unicode attribute")
      } else if (checkAttribute(attribute)) {
        params = params.copy(unicodeAttributes =
          params.unicodeAttributes + attribute)
        this
      } else {
        throw new IllformedLocaleException(
          s"Invalid unicode attribute $attribute")
      }
    }

    def removeUnicodeLocaleAttribute(attribute: String): Builder = {
      if (attribute == null) {
        throw new NullPointerException("Null unicode attribute")
      } else if (checkAttribute(attribute)) {
        params = params.copy(unicodeAttributes =
          params.unicodeAttributes.filterNot(_.equalsIgnoreCase(attribute)))
        this
      } else {
        throw new IllformedLocaleException(
          s"Invalid unicode attribute $attribute")
      }
    }

    def clear(): Builder = {
      params = BuilderParams()
      this
    }

    def clearExtensions(): Builder = {
      params = params.copy(extensions = SMap.empty)
      this
    }

    def build(): Locale = new Locale(
      params.language.getOrElse("") ,
      params.region.getOrElse(""),
      params.variant.getOrElse(""),
      params.script,
      params.extensions,
      params.unicodeExtensions,
      params.unicodeAttributes)
  }

  def getDefault(): Locale = LocaleRegistry.default

  def getDefault(category: Category): Locale = LocaleRegistry.default(category)

  def setDefault(newLocale: Locale): Unit = LocaleRegistry.setDefault(newLocale)

  def setDefault(category: Category, newLocale: Locale): Unit =
    LocaleRegistry.setDefault(category, newLocale)

  def getAvailableLocales(): Array[Locale] =
    LocaleRegistry.availableLocales.toArray

  def getISOCountries(): Array[String] = isocodes.isoCountries.toArray

  def getISOLanguages(): Array[String] = isocodes.isoLanguages.toArray

  private def parseLanguageTag(tag: String): Option[Locale] = {
    val parts = tag.split("-").toList
    val lang = parts match {
      case "" :: Nil => ""
      case "" :: xs => "und"
      case x :: xs => x
      case Nil => ""
    }
    println(lang)
    Some(new Locale(lang))
  }

  def forLanguageTag(languageTag: String): Locale = LocaleRegistry
    .localeForLanguageTag(languageTag).orElse(parseLanguageTag(languageTag))
    .getOrElse(ROOT)

  //class LanguageRange

  //class FilteringMode

  //def filter(priorityList: List[LanguageRange], locales: Collection[Locale], mode: FilteringMode): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], locales: Collection[Locale]): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], tags: Collection[String], mode: FilteringMode): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], tags: Collection[String]): List[Locale] = ???

  //def lookupTag(priorityList: List[LanguageRange], tags: Collection[String]): String = ???
}

class Locale private[util] (private[this] val language: String,
  private[this] val country: String, private[this] val variant: String,
  private[this] val script: Option[String],
  private[this] val _extensions: SMap[Char, String],
  private[this] val unicodeExtensions: SMap[String, String],
  private[this] val unicodeAttributes: SSet[String],
  private[this] val supportSpecialCases: Boolean = true) {

  // Required by the javadocs
  if (language == null || country == null || variant == null)
    throw new NullPointerException("Null argument to constructor not allowed")

  // Handle 2 special cases jp_JP_JP and th_TH_TH
  private[this] val extensions = {
    if ((language, country, variant) ==("ja", "JP", "JP") &&
        supportSpecialCases) {
      _extensions + (Locale.UNICODE_LOCALE_EXTENSION -> "ca-japanese")
    } else if ((language, country, variant) ==("th", "TH", "TH") &&
        supportSpecialCases) {
      _extensions + (Locale.UNICODE_LOCALE_EXTENSION -> "nu-thai")
    } else {
      _extensions
    }
  }

  private def updateSpecialLanguages(l: String) = l match {
    case "iw" => "he"
    case "ji" => "yi"
    case "in" => "id"
    case _    => l
  }

  private def translateSpecialLanguages(l: String) = l match {
    case "he" => "iw"
    case "yi" => "ji"
    case "id" => "in"
    case _    => l
  }

  // first constructor
  def this(language: String, country: String, variant: String) =
    this(language, country, variant, None, SMap.empty, SMap.empty, SSet.empty)

  // Additional constructors
  def this(language: String, country: String) = this(language, country, "")

  def this(language: String) = this(language, "", "")

  def getLanguage(): String = translateSpecialLanguages(language.toLowerCase)

  def getScript(): String = script.getOrElse("")

  def getCountry(): String = country.toUpperCase

  def getVariant(): String = variant

  def hasExtensions(): Boolean =
    extensions.nonEmpty || unicodeExtensions.nonEmpty

  def stripExtensions(): Locale =
    new Locale(language, country, variant, script, SMap.empty, SMap.empty,
          SSet.empty, false)

  def getExtension(key: Char): String = {
    if (key == Locale.UNICODE_LOCALE_EXTENSION && unicodeExtensions.nonEmpty) {
      unicodeExtensions.collect {
        case (k, v) if v.isEmpty => k
        case (k, v)              => s"$k-$v"
      }.mkString("-")
    } else extensions.get(key).orNull
  }

  def getExtensionKeys():Set[Char] = {
      if (unicodeExtensions.nonEmpty) {
        extensions.keySet + Locale.UNICODE_LOCALE_EXTENSION
      } else extensions.keySet
    }.asJava

  def getUnicodeLocaleAttributes():Set[String] = unicodeAttributes.asJava

  def getUnicodeLocaleType(key: String):String =
    unicodeExtensions.get(key).orNull

  def getUnicodeLocaleKeys():Set[String] = unicodeExtensions.keySet.asJava

  override def toString(): String = {
    // Rather complex toString following the specifications
    val hasVariant = getVariant().nonEmpty
    val hasCountry = getCountry().nonEmpty
    val hasLanguage = getLanguage().nonEmpty
    val hasScript = getScript().nonEmpty

    val countryPart =
      if (hasCountry) s"_${getCountry()}"
      else if (hasVariant) "_"
      else ""

    val variantPart =
      if (hasVariant) s"_${getVariant()}"
      else if (hasScript) "_"
      else ""

    val scriptPart =
      if (hasScript && hasExtensions()) s"#${getScript()}_"
      else if (hasScript) s"#${getScript()}"
      else if (hasExtensions()) "_#"
      else ""

    val extensionsPart = extensions.map { case (x, v) => s"$x-$v"}.mkString("")
    if (hasLanguage || hasCountry) {
      s"${getLanguage()}$countryPart$variantPart$scriptPart$extensionsPart"
    } else {
      ""
    }
  }

  def toLanguageTag(): String = {
    val language = {
      if (getLanguage().nonEmpty && Locale.checkLanguage(getLanguage))
        updateSpecialLanguages(getLanguage())
      else
        "und"
    }
    val country =
      if (Locale.checkRegion(getCountry())) s"-${getCountry()}"
      else ""
    val variantSegments = getVariant().split("-|_")
    val allSegmentsWellFormed =
      variantSegments.forall(Locale.checkVariantSegment)
    val allAcceptableSegments =
      variantSegments.forall(Locale.checkAcceptableVariantSegment)
    val variant = {
      if (allSegmentsWellFormed) {
        variantSegments.mkString("-", "-", "")
      } else if (allAcceptableSegments) {
        val (wellFormed, acceptable) =
          variantSegments.partition(Locale.checkVariantSegment)
        val pre =
          if (wellFormed.nonEmpty) wellFormed.take(1).mkString("-", "-", "-")
          else "-"
        pre + "x-lvariant" + (acceptable ++ wellFormed.drop(1)).mkString("-", "-", "")
      } else {
        ""
      }
    }
    val ext = {
      if (extensions.nonEmpty)
        extensions.map { case (x, v) => s"$x-$v" }.mkString("-", "-", "")
      else
        ""
    }
    val script = this.script.map(_ => s"-${getScript()}").getOrElse("")

    if (getLanguage == "no" && getCountry == "NO" && getVariant == "NY") "nn-NO"
    else s"$language$script$country$ext$variant"
  }

  // TODO Implement
  //def getISO3Language(): String = ???

  //def getISO3Country(): String = ???

  //final def getDisplayLanguage(): String = ???

  //final def getDisplayLanguage(inLocale: Locale): String = ???

  //final def getDisplayScript(): String = ???

  //final def getDisplayScript(inLocale: Locale): String = ???

  //final def getDisplayCountry(): String = ???

  //final def getDisplayCountry(inLocale: Locale): String = ???

  //final def getDisplayVariant(): String = ???

  //final def getDisplayVariant(inLocale: Locale): String = ???

  //final def getDisplayName(): String = ???

  //final def getDisplayName(inLocale: Locale): String = ???

  override def clone(): AnyRef = this // Locale is immutable

  private def isEqual(l: Locale): Boolean = {
    language == l.getLanguage && country == l.getCountry &&
    variant == l.getVariant && script.forall(_ == l.getScript) &&
    extensions.forall { case (k, v) => l.getExtension(k) == v } &&
    unicodeExtensions.forall { case (k, v) => l.getUnicodeLocaleType(k) == v } &&
    unicodeAttributes == l.getUnicodeLocaleAttributes.asScala
  }

  override def equals(x: Any): Boolean = x match {
    case l: Locale => isEqual(l)
    case _         => false
  }

  override def hashCode(): Int = toString.hashCode
}
