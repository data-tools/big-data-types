package org.datatools.bigdatatypes.formats

import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType._

/** Converts CamelCase field names to snake_case
  */
trait KeyTypeExampleFormats extends Formats {

  /** Transform booleans to "is_mybool" and dates to "mydate_at"
    */
  override def transformKey[A <: SqlType](name: String, t: A): String = t match {
    case _: SqlBool                   => "is_" + name
    case SqlDate(_) | SqlTimestamp(_) => name + "_at"
    case _                            => name
  }
}

object KeyTypeExampleFormats extends KeyTypeExampleFormats
