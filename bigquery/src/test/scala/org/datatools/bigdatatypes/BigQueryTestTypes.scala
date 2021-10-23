package org.datatools.bigdatatypes

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, FieldList, StandardSQLTypeName}

/** Test types from [[TestTypes]] converted to BigQuery. They can be used in multiple tests
  */
object BigQueryTestTypes {

  val basicFields: Seq[Field] = List(
    Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myLong", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myFloat", StandardSQLTypeName.FLOAT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myDouble", StandardSQLTypeName.FLOAT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myDecimal", StandardSQLTypeName.NUMERIC).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myBoolean", StandardSQLTypeName.BOOL).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build()
  )

  /** BigQuery doesn't have a main object, it has a list of fields in the root path */
  val basicTypes: Seq[Field] = basicFields

  val basicOption: Seq[Field] =
    List(
      Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("myOptionalString", StandardSQLTypeName.STRING).setMode(Mode.NULLABLE).build()
    )

  val basicOptionTypes: Seq[Field] =
    List(
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myLong", StandardSQLTypeName.INT64).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myFloat", StandardSQLTypeName.FLOAT64).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myDouble", StandardSQLTypeName.FLOAT64).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myDecimal", StandardSQLTypeName.NUMERIC).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myBoolean", StandardSQLTypeName.BOOL).setMode(Mode.NULLABLE).build(),
      Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.NULLABLE).build()
    )

  val basicWithList: Seq[Field] =
    List(
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("myList", StandardSQLTypeName.INT64).setMode(Mode.REPEATED).build()
    )

  val basicNested: Seq[Field] =
    List(
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
      Field
        .newBuilder(
          "myStruct",
          StandardSQLTypeName.STRUCT,
          FieldList.of(
            basicFields*
          )
        )
        .setMode(Mode.REQUIRED)
        .build()
    )

  val basicOptionalNested: Seq[Field] =
    List(
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
      Field
        .newBuilder(
          "myStruct",
          StandardSQLTypeName.STRUCT,
          FieldList.of(
            basicTypes*
          )
        )
        .setMode(Mode.NULLABLE)
        .build()
    )

  val basicNestedWithList: Seq[Field] =
    List(
      Field
        .newBuilder(
          "matrix",
          StandardSQLTypeName.STRUCT,
          FieldList.of(
            Field.newBuilder("x", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
            Field.newBuilder("y", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
          )
        )
        .setMode(Mode.REPEATED)
        .build()
    )

  val extendedTypes: Seq[Field] =
    List(
      Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("myTimestamp", StandardSQLTypeName.TIMESTAMP).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("myDate", StandardSQLTypeName.DATE).setMode(Mode.REQUIRED).build()
    )
}
