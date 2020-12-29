package org.datatools.bigdatatypes.formats

trait Formats extends Serializable {
  def transformKeys(s: String): String
}

trait DefaultFormats extends Formats {

  override def transformKeys(key: String): String = key
}
object DefaultFormats extends DefaultFormats

/**
 * Converts CamelCase field names to snake_case
 */
trait SnakifyFormats extends DefaultFormats {

  override def transformKeys(key: String): String = key
    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
    .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
    .toLowerCase
}

object SnakifyFormats extends SnakifyFormats
