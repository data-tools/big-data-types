package org.datatools.bigdatatypes

import org.apache.spark.sql.types.{ArrayType, BooleanType, DataTypes, DateType, DoubleType, FloatType, IntegerType, LongType, StringType, StructField, StructType, TimestampType}

/** Test types from [[TestTypes]] converted to BigQuery. They can be used in multiple tests
  */
object SparkTestTypes {

  val basicFields: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myLong", LongType, nullable = false),
      StructField("myFloat", FloatType, nullable = false),
      StructField("myDouble", DoubleType, nullable = false),
      StructField("myDecimal", DataTypes.createDecimalType, nullable = false),
      StructField("myBoolean", BooleanType, nullable = false),
      StructField("myString", StringType, nullable = false)
    )

  /** BigQuery doesn't have a main object, it has a list of fields in the root path */
  val basicTypes: StructType = StructType(basicFields)

  val basicOptionTypes: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = true),
      StructField("myLong", LongType, nullable = true),
      StructField("myFloat", FloatType, nullable = true),
      StructField("myDouble", DoubleType, nullable = true),
      StructField("myDecimal", DataTypes.createDecimalType, nullable = true),
      StructField("myBoolean", BooleanType, nullable = true),
      StructField("myString", StringType, nullable = true)
    )

  val basicWithList: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myList", ArrayType(IntegerType), nullable = true)
    )

  val basicNested: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField(
        "myStruct",
        StructType(basicFields),
        nullable = false
      )
    )

  val basicOptionalNested: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField(
        "myStruct",
        StructType(basicFields),
        nullable = true
      )
    )

  val basicNestedWithList: Seq[StructField] =
    List(
      StructField(
        "matrix",
        ArrayType(
          StructType(
            List(
              StructField("x", IntegerType, nullable = false),
              StructField("y", IntegerType, nullable = false)
            )
          )
        ),
        nullable = true
      )
    )

  val extendedTypes: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myTimestamp", TimestampType, nullable = false),
      StructField("myDate", DateType, nullable = false)
    )
}
