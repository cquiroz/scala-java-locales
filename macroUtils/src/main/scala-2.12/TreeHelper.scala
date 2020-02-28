package io.github.cquiroz.utils

import scala.reflect.macros.whitebox.Context

object TreeHelper {

  def resetLong(c: Context)(v: c.Expr[Long]): c.Expr[Long] =
    c.Expr[Long](v.tree.duplicate)

}
