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
