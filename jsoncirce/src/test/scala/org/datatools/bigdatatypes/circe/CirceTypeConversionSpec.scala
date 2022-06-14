package org.datatools.bigdatatypes.circe

import io.circe.{Json, JsonNumber, JsonObject}
import org.datatools.bigdatatypes.TestTypes.*
import org.datatools.bigdatatypes.{CirceTestTypes as C, SqlTestTypes as S, UnitSpec}
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.circe.CirceTypeConversion.*

/** Reverse conversion, from Circe types to [[SqlType]]s
  */
class CirceTypeConversionSpec extends UnitSpec {

  "Simple Json Type" should "be converted into SqlType" in {
    SqlInstanceConversion[Json].getType(Json.fromString("test")) shouldBe SqlString()
  }

  "Simple Json Object" should "be converted into SqlStruct" in {
    val j = Json.fromJsonObject(JsonObject(("myInt", Json.fromInt(1))))
    val expected = SqlStruct(List(("myInt", SqlDecimal())))
    SqlInstanceConversion[Json].getType(j) shouldBe expected
  }

  "Basic Json fields" should "be converted into Basic SqlTypes" in {
    SqlInstanceConversion[Json].getType(C.basicTypes) shouldBe S.basicTypes
  }

}
