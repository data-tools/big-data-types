package org.datatools.bigdatatypes.circe

import io.circe.Json
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.{Repeated, Required}
import org.datatools.bigdatatypes.basictypes.{SqlType, SqlTypeMode}
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion

import scala.annotation.tailrec

object CirceTypeConversion {

  /** Circe does not have an implementation of SqlTypeConversion due to its private API.
    * We can not detect if a field is JDouble, JLong and so on because they are private,
    * so the only way to detect some kind of types is based on their methods `.isNumber`, `isString` and so on
    * which are only available if we have an instance of [[Json]]
    */

  /** Implementation of SqlInstanceConversion Type Class */
  implicit val circeJsonType: SqlInstanceConversion[Json] = (value: Json) => convertCirceType(value)

  @tailrec
  def convertCirceType(j: Json, repeated: Boolean = false): SqlType =
    j match {
      case v if v.isArray   => convertCirceType(v.asArray.get.apply(0), repeated = true)
      case v if v.isNumber  => SqlDecimal(isRepeated(repeated))
      case v if v.isString  => SqlString(isRepeated(repeated))
      case v if v.isBoolean => SqlBool(isRepeated(repeated))
      case v if v.isObject =>
        val pairs = v.asObject.get.keys zip v.asObject.get.values
        SqlStruct(loopStructs(pairs), isRepeated(repeated))
    }

  /** For recursion, loops over all items in an object
    */
  private def loopStructs(l: Iterable[(String, Json)]): List[(String, SqlType)] =
    l.map(x => x._1 -> convertCirceType(x._2)).toList

  /** From Boolean to Repeated or Required Mode
    */
  private def isRepeated(repeated: Boolean): SqlTypeMode = if (repeated) Repeated else Required

  /** Extension method. Enables val myInstance: Json -> myInstance.asSqlType
    * @param value in a Json from Circe
    */
  implicit class StructTypeSyntax(value: Json) {
    def asSqlType: SqlType = SqlInstanceConversion[Json].getType(value)
  }

}
