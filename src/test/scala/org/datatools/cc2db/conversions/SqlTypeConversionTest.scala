package org.datatools.cc2db.conversions

import org.datatools.cc2db.types.UnitTest
import org.datatools.cc2db.types.basic._

class SqlTypeConversionTest extends UnitTest {

  case class BasicTypes(myInt: Int, myLong: Long, myFloat: BigDecimal, myBoolean: Boolean, myString: String)
  case class BasicOption(myString: String, myOptionalString: Option[String])
  case class BasicList(myInt: Int, myList: List[Int])
  case class BasicStruct(myInt: Int, myStruct: BasicTypes)
  case class Point(x: Int, y: Int)
  case class ListOfStruct(matrix: List[Point])

  behavior of "SqlTypeConversionTest"

  it should "bigDecimalType" in {
    val sqlType: SqlType = SqlTypeConversion[Int].getType
    sqlType shouldBe SqlInt(Required)
  }

  "Int type" should "be converted into SqlInt" in {
    val sqlType: SqlType = SqlTypeConversion[Int].getType
    sqlType shouldBe SqlInt(Required)
  }
  "Long type" should "be converted into SqlLong" in {
    val sqlType: SqlType = SqlTypeConversion[Long].getType
    sqlType shouldBe SqlLong(Required)
  }
  "Double type" should "be converted into SqlFloat" in {
    val sqlType: SqlType = SqlTypeConversion[Double].getType
    sqlType shouldBe SqlFloat(Required)
  }
  "Float type" should "be converted into SqlFloat" in {
    val sqlType: SqlType = SqlTypeConversion[Float].getType
    sqlType shouldBe SqlFloat(Required)
  }
  "BigDecimal type" should "be converted into SqlDecimal" in {
    val sqlType: SqlType = SqlTypeConversion[BigDecimal].getType
    sqlType shouldBe SqlDecimal(Required)
  }
  "Boolean type" should "be converted into SqlBool" in {
    val sqlType: SqlType = SqlTypeConversion[Boolean].getType
    sqlType shouldBe SqlBool(Required)
  }
  "String type" should "be converted into SqlString" in {
    val sqlType: SqlType = SqlTypeConversion[String].getType
    sqlType shouldBe SqlString(Required)
  }

  "Basic Option type" should "be converted into Nullable type" in {
    val sqlType: SqlType = SqlTypeConversion[Option[String]].getType
    sqlType shouldBe SqlString(Nullable)
  }

  "Case Class with Option" should "be converted into SqlTypes with nullable" in {
    val sqlType: SqlType = SqlTypeConversion[BasicOption].getType
    val fields: List[(String, SqlType)] =
      List(
        ("myString", SqlString(Required)),
        ("myOptionalString", SqlString(Nullable))
      )
    sqlType shouldBe SqlStruct(fields, Required)
  }

  "basic case class" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicTypes].getType
    val fields: List[(String, SqlType)] =
      List(
        ("myInt", SqlInt(Required)),
        ("myLong", SqlLong(Required)),
        ("myFloat", SqlDecimal(Required)),
        ("myBoolean", SqlBool(Required)),
        ("myString", SqlString(Required))
      )
    sqlType shouldBe SqlStruct(fields, Required)
  }

  "Case class with List" should "be converted into Repeated type" in {
    val sqlType: SqlType = SqlTypeConversion[BasicList].getType
    val fields: List[(String, SqlType)] =
      List(
        ("myInt", SqlInt(Required)),
        ("myList", SqlInt(Repeated))
      )
    sqlType shouldBe SqlStruct(fields, Required)
  }

  "case class with nested object" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicStruct].getType
    val basicFields: List[(String, SqlType)] =
      List(
        ("myInt", SqlInt(Required)),
        ("myLong", SqlLong(Required)),
        ("myFloat", SqlDecimal(Required)),
        ("myBoolean", SqlBool(Required)),
        ("myString", SqlString(Required))
      )
    val fields: List[(String, SqlType)] =
      List(
        ("myInt", SqlInt(Required)),
        ("myStruct", SqlStruct(basicFields, Required))
      )
    sqlType shouldBe SqlStruct(fields, Required)
  }

  "Case class with Struct List" should "be converted into Repeated Struct type" in {
    val sqlType: SqlType = SqlTypeConversion[ListOfStruct].getType
    val struct: List[(String, SqlType)] =
      List(
        ("x", SqlInt(Required)),
        ("y", SqlInt(Required))
      )
    val fields: List[(String, SqlType)] =
      List(("matrix", SqlStruct(struct, Repeated)))
    sqlType shouldBe SqlStruct(fields, Required)
  }

}
