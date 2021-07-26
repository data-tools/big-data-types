package org.datatools.bigdatatypes

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import org.datatools.bigdatatypes.BigQueryTestTypes.basicFields
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.bigquery.BigQueryTypeConversion.schema
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.bigquery.SqlTypeToBigQuery
import org.datatools.bigdatatypes.cassandra.CassandraTables
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.{InstanceSchemaSyntax, InstanceSyntax}

class BigQueryToOthers extends UnitSpec {

  behavior of "BigQuery types to other types"

  //basicFields is a object that we have defined in every module
  val bqSchema: Schema = Schema.of(toJava(basicFields.toList))

  "BigQuery Schema" should "be converted into Spark Schema" in {
    SparkSchemas.schema[Schema](bqSchema) shouldBe SparkTestTypes.basicTypes
  }

  it should "be converted into Spark Fields" in {
    SparkSchemas.fields[Schema](bqSchema) shouldBe SparkTestTypes.basicFields
  }

  it should "be converted into Spark Schema using extension method" in {
    bqSchema.sparkSchema shouldBe SparkTestTypes.basicTypes
  }

  it should "be converted into Spark Fields using extension method" in {
    bqSchema.sparkFields shouldBe SparkTestTypes.basicFields
  }

  it should "be converted into Cassandra Table" in {
    CassandraTables.table[Schema](bqSchema, "testTable", "myLong").toString shouldBe
      "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }




}
