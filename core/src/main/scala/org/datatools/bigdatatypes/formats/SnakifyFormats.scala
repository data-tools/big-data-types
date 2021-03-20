package org.datatools.bigdatatypes.formats

/** Converts CamelCase field names to snake_case
  */
trait SnakifyFormats extends Formats {

  override def transformKey(name: String): String = name
    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
    .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
    .toLowerCase
}

object SnakifyFormats extends SnakifyFormats
