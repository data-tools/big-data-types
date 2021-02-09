package org.datatools.bigdatatypes.formats

//TODO add precision for Decimal types
trait Formats {
  def transformKeys(s: String): String
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
