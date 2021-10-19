package bigdatatypes

import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.{BigQueryTestTypes, CassandraTestTypes, TestTypes, UnitSpec}
import org.datatools.bigdatatypes.bigquery.{BigQueryTable, SqlInstanceToBigQuery}
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery._
import org.datatools.bigdatatypes.cassandra.{CassandraTables, SqlInstanceToCassandra}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.{SparkSchemas, SqlTypeToSpark}
import org.datatools.bigdatatypes.spark.SparkTypeConversion._

class SparkToOthers extends UnitSpec {

  behavior of "Spark Schemas to others"

  "Sparck Schema" should "be converted into BigQuery Fields" in {
    val schema: StructType = SqlTypeToSpark[BasicTypes].sparkSchema
    val bq = SqlInstanceToBigQuery[StructType].bigQueryFields(schema)
    bq shouldBe BigQueryTestTypes.basicFields
  }

  /* Example using a DataFrame or Dataset
  "Spark Dataframe" should "be converted into BigQuery Fields" in {
    val myDataFrame: DataFrame = ???
    val spark: SparkSession = ???
    import spark.implicits._
    myDataFrame.schema.asBigQuery shouldBe ???
  }
  */

  it should "have a method to get BigQuery Fields" in {
    val schema: StructType = SqlTypeToSpark[BasicTypes].sparkSchema
    schema.asBigQuery shouldBe BigQueryTestTypes.basicFields
  }

  it should "be converted into SqlType Instance" in {
    val schema: StructType = SqlTypeToSpark[BasicTypes].sparkSchema
    schema.asSqlType shouldBe TestTypes.basicTypes
  }

  "Spark to SqlType instance" should "be converted into BigQuery Fields" in {
    val sql: SqlType = SparkSchemas.schema[BasicTypes].asSqlType
    SqlInstanceToBigQuery[SqlType].bigQueryFields(sql) shouldBe BigQueryTestTypes.basicFields
  }

  "Spark Schema" should "create a Big Query Table" in {
    val schema = SqlTypeToSpark[BasicTypes].sparkSchema
    //Just an example, it will be left as there is no BigQuery Environment set up
    BigQueryTable.createTable(schema, "dataset", "table").isLeft shouldBe true
  }

  "Spark Schema" should "be converted into BigQuery Fields" in {
    val schema: StructType = SparkSchemas.schema[BasicTypes]
    SqlInstanceToBigQuery[StructType].bigQueryFields(schema) shouldBe BigQueryTestTypes.basicFields
  }

  it should "be converted into Cassandra tuples" in {
    val schema: StructType = SparkSchemas.schema[BasicTypes]
    SqlInstanceToCassandra[StructType].cassandraFields(schema) shouldBe CassandraTestTypes.basicFields
  }

  it should "be converted into Cassandra Table" in {
    val sparkSchema: StructType = SparkSchemas.schema[BasicTypes]
    //this should be the public one, it accepts any type from the library
    CassandraTables
      .table(sparkSchema, "testTable", "myLong")
      .toString shouldBe "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }

}
