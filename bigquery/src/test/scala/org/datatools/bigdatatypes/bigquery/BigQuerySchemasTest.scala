package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import org.datatools.bigdatatypes.TestTypes.ListOfStruct
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.{BigQueryTestTypes, UnitSpec}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.conversions.SqlTypeConversion.*

class BigQuerySchemasTest extends UnitSpec {

  val elements1: Seq[Field] = List(
    Field.newBuilder("a", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
  )

  val elements2: Seq[Field] = List(
    Field.newBuilder("a", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("b", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
  )

  val elements3: Seq[Field] = List(
    Field.newBuilder("a", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("b", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("c", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
  )

  val elements4: Seq[Field] = List(
    Field.newBuilder("a", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("b", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("c", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("d", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
  )

  val elements5: Seq[Field] = List(
    Field.newBuilder("a", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("b", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("c", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("d", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("e", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
  )
  case class Simple1(a: Int)
  case class Simple2(b: Int)
  case class Simple3(c: Int)
  case class Simple4(d: Int)
  case class Simple5(e: Int)

  behavior of "BigQuerySchemas"

  "Case class with Struct List" should "be converted into BQ Schema" in {
    val fields: Schema = BigQuerySchemas.schema[ListOfStruct]
    fields shouldBe Schema.of(toJava(BigQueryTestTypes.basicNestedWithList))
  }

  "2 classes" should "be converted into a BQ Schema" in {
    BigQuerySchemas.schema[Simple1, Simple2] shouldBe Schema.of(toJava(elements2))
  }

  "3 classes" should "be converted into a BQ Schema" in {
    BigQuerySchemas.schema[Simple1, Simple2, Simple3] shouldBe Schema.of(toJava(elements3))
  }

  "4 classes" should "be converted into a BQ Schema" in {
    BigQuerySchemas.schema[Simple1, Simple2, Simple3, Simple4] shouldBe Schema.of(toJava(elements4))
  }

  "5 classes" should "be converted into a BQ Schema" in {
    BigQuerySchemas.schema[Simple1, Simple2, Simple3, Simple4, Simple5] shouldBe Schema.of(toJava(elements5))
  }

}
