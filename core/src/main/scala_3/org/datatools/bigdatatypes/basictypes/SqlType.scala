package org.datatools.bigdatatypes.basictypes

import org.datatools.bigdatatypes.basictypes.SqlType.{SqlBool, SqlDate, SqlDecimal, SqlDouble, SqlFloat, SqlInt, SqlLong, SqlString, SqlStruct, SqlTimestamp}
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*

/** Abstract representation of the type of a generic SQL database */
enum SqlType {

  /** @return the [[SqlTypeMode]] of this SqlType
    */
  def mode: SqlTypeMode

  /** Promotes the type to a new mode if the conversion makes sense. e.g:
    * [[List[Option[String] ] ]] and [[Option[List[String] ] ]] should be SqlString(Repeated)
    *
    * @param mode the mode we want to convert to
    * @return a new [[SqlType]] with the mode
    */
  def changeMode(mode: SqlTypeMode): SqlType =
    if (this.mode.isValidConversion(mode))
      this match {
        case SqlInt(_)             => SqlInt(mode)
        case SqlLong(_)            => SqlLong(mode)
        case SqlFloat(_)           => SqlFloat(mode)
        case SqlDouble(_)          => SqlDouble(mode)
        case SqlDecimal(_)         => SqlDecimal(mode)
        case SqlBool(_)            => SqlBool(mode)
        case SqlString(_)          => SqlString(mode)
        case SqlTimestamp(_)       => SqlTimestamp(mode)
        case SqlDate(_)            => SqlDate(mode)
        case SqlStruct(records, _) => SqlStruct(records, mode)
      }
    else this

  case SqlInt(mode: SqlTypeMode = Required)
  case SqlLong(mode: SqlTypeMode = Required)
  case SqlFloat(mode: SqlTypeMode = Required)
  case SqlDouble(mode: SqlTypeMode = Required)
  case SqlDecimal(mode: SqlTypeMode = Required)
  case SqlBool(mode: SqlTypeMode = Required)
  case SqlString(mode: SqlTypeMode = Required)
  case SqlTimestamp(mode: SqlTypeMode = Required)
  case SqlDate(mode: SqlTypeMode = Required)
  case SqlStruct(records: List[(String, SqlType)], mode: SqlTypeMode = Required)
}


