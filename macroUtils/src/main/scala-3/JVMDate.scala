package io.github.cquiroz.utils

object JVMDate:
  type JVMDateT = {
    val year: Int
    val month: Int
    val date: Int
    val hours: Int
    val minutes: Int
    val seconds: Int
  }

  // def getImpl(c: Context)(time: c.Expr[Long]) = {
  //   import c.universe._
  //
  //   val timeValue = TreeHelper.resetLong(c)(time)
  //
  //   val date = new Date(c.eval[Long](timeValue))
  //
  //   c.Expr[JVMDateT](q"""
  //     new {
  //       val year: Int = ${date.getYear}
  //       val month: Int = ${date.getMonth}
  //       val date: Int = ${date.getDate}
  //       val hours: Int = ${date.getHours}
  //       val minutes: Int = ${date.getMinutes}
  //       val seconds: Int = ${date.getSeconds}
  //     }
  //   """)
  // }

  inline def get(time: Long): JVMDateT = {
    val curDate = new java.util.Date(time)
    new {
      val year: Int = curDate.getYear
      val month: Int = curDate.getMonth
      val date: Int = curDate.getDate
      val hours: Int = curDate.getHours
      val minutes: Int = curDate.getMinutes
      val seconds: Int = curDate.getSeconds
    }
  }
end JVMDate
