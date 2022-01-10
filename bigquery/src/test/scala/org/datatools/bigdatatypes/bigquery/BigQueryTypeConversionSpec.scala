package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import org.datatools.bigdatatypes.BigQueryTestTypes.*
import org.datatools.bigdatatypes.TestTypes.{BasicList, BasicOptionTypes, BasicOptionalStruct, BasicStruct, BasicTypes, ExtendedTypes, ListOfStruct}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.Required
import org.datatools.bigdatatypes.bigquery.BigQueryTypeConversion.{FieldTypeSyntax, SchemaFieldSyntax, field, intType}
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

import scala.annotation.tailrec

/** This class tests all the conversions between BigQuery objects ([[Schema]] or [[Field]]) into SqlType objects
  */
class BigQueryTypeConversionSpec extends UnitSpec {

  /** As BigQuery has less types (e.g: Integer is always Int64, there is no difference between Double and Float)
    * This is just a "hack" to reduce the types on the Test Types. Without this, we should rewrite all the test examples only for BigQuery
    * Using this, we can use the Test examples
    * @param field SqlType that will be converted with Ints to Longs and Doubles to Floats
    * @return
    */
  def reduceBQTypes(field: SqlType): SqlType = {

    /** small method to loop over structs */
    def loop(records: List[(String, SqlType)]): List[(String, SqlType)] =
      records.map(record => record._1 -> reduceBQTypes(record._2))

    field match {
      case SqlInt(mode)             => SqlLong(mode)
      case SqlDouble(mode)          => SqlFloat(mode)
      case SqlStruct(records, mode) => SqlStruct(loop(records), mode)
      case _                        => field
    }
  }

  "Simple BQ Field" should "be converted into SqlType" in {
    SqlTypeConversion[StandardSQLTypeName.INT64.type].getType shouldBe SqlLong(Required)
  }

  it should "be converted into SqlType using an instance" in {
    val myField = Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
    SqlInstanceConversion[Field].getType(myField) shouldBe SqlLong(Required)
  }

  it should "be converted into SqlType using extension method" in {
    val myField = Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
    myField.asSqlType shouldBe SqlLong(Required)
  }

  "Basic BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicFields.toList))
    val sqlType: SqlType = bqSchema.asSqlType
    sqlType shouldBe reduceBQTypes(SqlTypeConversion[BasicTypes].getType)
  }

  "Optional fields in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicOptionTypes.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[BasicOptionTypes].getType)
  }

  "Repeated field in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicWithList.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[BasicList].getType)
  }

  "Nested field in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicNested.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[BasicStruct].getType)
  }

  "Optional Nested field in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicOptionalNested.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[BasicOptionalStruct].getType)
  }

  "Nested field with repeated records in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(basicNestedWithList.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[ListOfStruct].getType)
  }

  "Extended type fields in BQ Schema" should "be converted into SqlType" in {
    val bqSchema: Schema = Schema.of(toJava(extendedTypes.toList))
    bqSchema.asSqlType shouldBe reduceBQTypes(SqlTypeConversion[ExtendedTypes].getType)
  }

}
