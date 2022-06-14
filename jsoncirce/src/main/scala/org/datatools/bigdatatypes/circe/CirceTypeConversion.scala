package org.datatools.bigdatatypes.circe

import io.circe
import io.circe.Json.{JArray, JBoolean, JNull, JNumber, JObject, JString}
import io.circe.{Json, JsonBigDecimal, JsonDouble, JsonFloat, JsonLong, JsonNumber, JsonObject}
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}

object CirceTypeConversion {

  /** Circe does not have an implementation of SqlTypeConversion due to its private API.
    * We can not detect if a field is JDouble, JLong and so on because they are private,
    * so the only way to detect some kind of types is based on their methods `.isNumber`, `isString` and so on
    * which are only available if we have an instance of [[Json]]
    * */


  /** Implementation of SqlInstanceConversion Type Class */
  implicit val circeJsonType: SqlInstanceConversion[Json] = (value: Json) => convertCirceType(value)


  def convertCirceType(j: Json): SqlType = {
    j match {
      case v if v.isNumber => SqlDecimal()
      case v if v.isString => SqlString()
      case v if v.isBoolean => SqlBool()
      case v if v.isObject =>
        val pairs = v.asObject.get.keys zip v.asObject.get.values
        SqlStruct(loopStructs(pairs))
    }
  }

  /**
    * For recursion, loops over all items in an object
    */
  def loopStructs(l: Iterable[(String, Json)]): List[(String, SqlType)] = {
    l.map(x => x._1 -> convertCirceType(x._2)).toList
  }


}
