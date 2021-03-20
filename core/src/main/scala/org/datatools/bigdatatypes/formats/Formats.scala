package org.datatools.bigdatatypes.formats

import org.datatools.bigdatatypes.types.basic.SqlType

trait Formats {
  /** Used to transform field names */
  def transformKey(name: String): String = name

  /** Used to transform field names based on their type */
  def transformKey[A <: SqlType](name: String, t: A): String = name

  /** Define precision for BigDecimal types */
  case class BigDecimalPrecision(precision: Int, scale:Int)
  val bigDecimal: BigDecimalPrecision = BigDecimalPrecision(10, 0)
}

/** A list of predefined formats to be imported. Only one can be imported at the same time
  */
object Formats {
  implicit val implicitDefaultFormats: Formats = DefaultFormats
  implicit val implicitSnakifyFormats: Formats = SnakifyFormats
}

/** Default Formats transforms nothing
  */
trait DefaultFormats extends Formats {

  override def transformKey(name: String): String = name
}
object DefaultFormats extends DefaultFormats


