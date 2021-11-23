package org.datatools.bigdatatypes.conversions

import java.sql.{Date, Timestamp}
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes._
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode._

class SqlTypeConversionSpec extends UnitSpec {

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

  "Double type" should "be converted into SqlDouble" in {
    val sqlType: SqlType = SqlTypeConversion[Double].getType
    sqlType shouldBe SqlDouble(Required)
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
    sqlType shouldBe basicOption
  }

  "basic case class" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicTypes].getType
    sqlType shouldBe basicTypes
  }

  "Case Class with basic options types" should "be converted into nullable SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicOptionTypes].getType
    sqlType shouldBe basicOptionTypes
  }

  "Case class with List" should "be converted into Repeated type" in {
    val sqlType: SqlType = SqlTypeConversion[BasicList].getType
    sqlType shouldBe basicWithList
  }

  "case class with nested object" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicStruct].getType
    sqlType shouldBe basicNested
  }

  "case class with optional nested object" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeConversion[BasicOptionalStruct].getType
    sqlType shouldBe basicOptionalNested
  }

  "Case class with Struct List" should "be converted into Repeated Struct type" in {
    val sqlType: SqlType = SqlTypeConversion[ListOfStruct].getType
    sqlType shouldBe basicNestedWithList
  }

  "Option of Option" should "be just a Nullable type mode" in {
    val sqlType: SqlType = SqlTypeConversion[Option[Option[String]]].getType
    sqlType shouldBe SqlString(Nullable)
  }

  "Option List" should "be just a Repeated type mode" in {
    val sqlType: SqlType = SqlTypeConversion[Option[List[String]]].getType
    sqlType shouldBe SqlString(Repeated)
  }

  "List Option" should "be just a Repeated type mode" in {
    val sqlType: SqlType = SqlTypeConversion[List[Option[String]]].getType
    sqlType shouldBe SqlString(Repeated)
  }

  "List Option List" should "be just a Repeated type mode" in {
    val sqlType: SqlType = SqlTypeConversion[List[Option[List[String]]]].getType
    sqlType shouldBe SqlString(Repeated)
  }

  "Java SQL Timestamp" should "be converted into SqlTimestamp" in {
    val sqlType: SqlType = SqlTypeConversion[Timestamp].getType
    sqlType shouldBe SqlTimestamp(Required)
  }

  "Optional Java SQL Timestamp" should "be converted into nullable SqlTimestamp" in {
    val sqlType: SqlType = SqlTypeConversion[Option[Timestamp]].getType
    sqlType shouldBe SqlTimestamp(Nullable)
  }

  "Java SQL Date" should "be converted into nullable SqlDate" in {
    val sqlType: SqlType = SqlTypeConversion[Date].getType
    sqlType shouldBe SqlDate(Required)
  }

  "Optional Java SQL Date" should "be converted into nullable SqlDate" in {
    val sqlType: SqlType = SqlTypeConversion[Option[Date]].getType
    sqlType shouldBe SqlDate(Nullable)
  }

  "Case class with extended types" should "be converted into Struct with extended types" in {
    val sqlType: SqlType = SqlTypeConversion[ExtendedTypes].getType
    sqlType shouldBe extendedTypes
  }

}
