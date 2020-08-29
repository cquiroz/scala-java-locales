package locales

object Implicits extends Implicits {

  /** 
   * Used to swallow unused warnings. 
   * 
   * e.g. 
   * import locales.Implicits._
   * ...
   * void(foo) 
   **/
  @inline def void(as: Any*): Unit = (as, ())._2
}

trait Implicits extends ImplicitsPlatform {

}