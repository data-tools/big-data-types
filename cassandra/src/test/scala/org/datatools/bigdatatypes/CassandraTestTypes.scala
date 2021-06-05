package org.datatools.bigdatatypes

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}

object CassandraTestTypes {

  val basicFields: Seq[(String, DataType)] =
    List(
      ("myInt", DataTypes.INT),
      ("myLong", DataTypes.BIGINT),
      ("myFloat", DataTypes.FLOAT),
      ("myDouble", DataTypes.DOUBLE),
      ("myDecimal", DataTypes.DECIMAL),
      ("myBoolean", DataTypes.BOOLEAN),
      ("myString", DataTypes.TEXT)
    )

  // There is no nullable parameter for Cassandra
  // val basicOptionTypes: Seq[(String, DataType)]

  /*
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

   */

}
