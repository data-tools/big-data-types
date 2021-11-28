package org.datatools.bigdatatypes

import com.google.cloud.bigquery.Schema
import org.datatools.bigdatatypes.BigQueryTestTypes.basicFields
import org.datatools.bigdatatypes.bigquery.BigQueryTypeConversion.schema
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.cassandra.CassandraTables
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class BigQueryToOthers extends UnitSpec {

  behavior of "BigQuery types to other types"

  // basicFields is a object that we have defined in every module
  val bqSchema: Schema = Schema.of(toJava(basicFields.toList))

  "BigQuery Schema" should "be converted into Cassandra Table" in {
    CassandraTables.table[Schema](bqSchema, "testTable", "myLong").toString shouldBe
      "CREATE TABLE testtable (myint bigint,mylong bigint PRIMARY KEY,myfloat float,mydouble float,mydecimal decimal,myboolean boolean,mystring text)"
  }

}
