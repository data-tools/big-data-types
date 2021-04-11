package org.datatools.bigdatatypes.basictypes

import org.datatools.bigdatatypes.basictypes.SqlTypeMode.{Nullable, Repeated, Required}

/** The mode of a sql type. e.g: Required, Nullable, Repeated.
  */
enum SqlTypeMode {

  /** Tells you if you can change the mode for another.
    *
    * e.g:
    * [[[Option[String] ]] should be SqlString(Nullable)
    * [[List[Option[String] ] ]] should be SqlString(Repeated).
    *
    * @param newMode the mode we want to convert to
    * @return true if this conversion makes sense, false if not
    */
  def isValidConversion(newMode: SqlTypeMode): Boolean = (this, newMode) match {
    case (Repeated, _)        => false
    case (Nullable, Required) => false
    case (_, _)               => true
  }

  /** Nullable field */
  case Nullable

  /** Repeated or array field */
  case Repeated

  /** Mandatory field */
  case Required
}

