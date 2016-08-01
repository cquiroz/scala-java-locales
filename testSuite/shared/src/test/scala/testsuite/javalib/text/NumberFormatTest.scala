package testsuite.javalib.text

import java.text.{DecimalFormat, DecimalFormatSymbols, NumberFormat}

import org.junit.Test
import org.junit.Assert._
import testsuite.utils.{LocaleTestSetup, Platform}

class NumberFormatTest extends LocaleTestSetup {
  @Test def test_constants(): Unit = {
    assertEquals(0, NumberFormat.INTEGER_FIELD)
    assertEquals(1, NumberFormat.FRACTION_FIELD)
  }

  @Test def test_default_instance(): Unit = {
    val nf = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0.###", nf.toPattern())
    assertEquals(DecimalFormatSymbols.getInstance(), nf.getDecimalFormatSymbols())

    val pf = NumberFormat.getPercentInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0%", pf.toPattern())
    assertEquals(DecimalFormatSymbols.getInstance(), pf.getDecimalFormatSymbols())
  }
}
