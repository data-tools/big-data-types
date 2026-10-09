package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema}
import org.datatools.bigdatatypes.TestTypes.{BasicList, BasicOption, BasicStruct, BasicTypes, ExtendedTypes}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryTypeConversion.{field, schema}
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import com.google.cloud.bigquery.StandardSQLTypeName

class BigQueryTableUnitSpec extends UnitSpec {

  "TryTable" should "fail without Service Account" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    BigQueryTable.createTable(sql, "test", "sqlType_table").isLeft shouldBe true
  }

  "Typed createTable" should "fail without Service Account for every arity" in {
    BigQueryTable.createTable[BasicTypes]("test", "t").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct]("test", "t").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct, BasicList]("test", "t").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct, BasicList, BasicOption]("test", "t").isLeft shouldBe true
    BigQueryTable
      .createTable[BasicTypes, BasicStruct, BasicList, BasicOption, ExtendedTypes]("test", "t")
      .isLeft shouldBe true
  }

  "Partitioned createTable" should "fail without Service Account for every arity" in {    BigQueryTable.createTable[BasicTypes]("test", "t", "part").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct]("test", "t", "part").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct, BasicList]("test", "t", "part").isLeft shouldBe true
    BigQueryTable.createTable[BasicTypes, BasicStruct, BasicList, BasicOption]("test", "t", "part").isLeft shouldBe true
    BigQueryTable
      .createTable[BasicTypes, BasicStruct, BasicList, BasicOption, ExtendedTypes]("test", "t", "part")
      .isLeft shouldBe true
  }

  "Instance partitioned createTable" should "fail without Service Account" in {
    val myField =
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
    val bqSchema = Schema.of(myField)
    BigQueryTable.createTable(bqSchema, "test", "t", "part").isLeft shouldBe true
  }

}
