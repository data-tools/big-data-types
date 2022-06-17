package org.datatools.bigdatatypes.circe

import io.circe.{Json, JsonObject}
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.circe.CirceTypeConversion.*
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion
import org.datatools.bigdatatypes.{CirceTestTypes as C, SqlTestTypes as S, UnitSpec}

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

  "Basic Json fields with Arrays" should "be converted into SqlTypes" in {
    SqlInstanceConversion[Json].getType(C.basicWithList) shouldBe S.basicWithList
  }

  "Basic Json fields with Nested objects" should "be converted into SqlTypes" in {
    SqlInstanceConversion[Json].getType(C.basicNested) shouldBe S.basicNested
  }

  "Basic Json fields with Nested Arrays" should "be converted into SqlTypes" in {
    SqlInstanceConversion[Json].getType(C.basicNestedWithList) shouldBe S.basicNestedWithList
  }

  "Extension method asSqlType" should "convert a Json into SqlTypes" in {
    C.basicTypes.asSqlType shouldBe S.basicTypes
  }

}
