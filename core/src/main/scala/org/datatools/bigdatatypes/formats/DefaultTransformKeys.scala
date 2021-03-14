package org.datatools.bigdatatypes.formats

//TODO add precision for Decimal types
trait Formats {
  /** Used to transform field names */
  def transformKeys(s: String): String

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

  override def transformKeys(key: String): String = key
}
object DefaultFormats extends DefaultFormats

/** Converts CamelCase field names to snake_case
  */
trait SnakifyFormats extends Formats {

  override def transformKeys(key: String): String = key
    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
    .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
    .toLowerCase
}

object SnakifyFormats extends SnakifyFormats
