package org.datatools.bigdatatypes.types.basic

/** Abstract representation of the type of a generic SQL database */
sealed trait SqlType {

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

}

case class SqlInt(mode: SqlTypeMode = Required) extends SqlType
case class SqlLong(mode: SqlTypeMode = Required) extends SqlType
case class SqlFloat(mode: SqlTypeMode = Required) extends SqlType
case class SqlDouble(mode: SqlTypeMode = Required) extends SqlType
case class SqlDecimal(mode: SqlTypeMode = Required) extends SqlType
case class SqlBool(mode: SqlTypeMode = Required) extends SqlType
case class SqlString(mode: SqlTypeMode = Required) extends SqlType
case class SqlTimestamp(mode: SqlTypeMode = Required) extends SqlType
case class SqlDate(mode: SqlTypeMode = Required) extends SqlType
case class SqlStruct(records: List[(String, SqlType)], mode: SqlTypeMode = Required) extends SqlType
