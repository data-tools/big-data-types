package org.datatools.bigdatatypes

import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.bigquery.SqlTypeToBigQuery
import org.datatools.bigdatatypes.cassandra.CassandraTables
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkSchemas

class ScalaToOthers extends UnitSpec {

  behavior of "Scala types to other types"

  "Case Class" should "be converted into BigQuery Fields" in {
    val bq = SqlTypeToBigQuery[BasicTypes].bigQueryFields
    bq shouldBe BigQueryTestTypes.basicFields
  }

  it should "be converted into Spark Schema" in {
    SparkSchemas.schema[BasicTypes] shouldBe SparkTestTypes.basicTypes
  }

  it should "be converted into Cassandra Table" in {
    CassandraTables.table[BasicTypes]("testTable", "myLong").toString shouldBe
      "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }




}
