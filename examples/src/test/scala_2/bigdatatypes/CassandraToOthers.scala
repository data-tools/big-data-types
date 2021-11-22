package bigdatatypes

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery
import org.datatools.bigdatatypes.cassandra.CassandraTypeConversion.cassandraCreateTable
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.InstanceSyntax

class CassandraToOthers extends UnitSpec {

  behavior of "Cassandra Types to other types"

  val cassandraTable: CreateTable =
    createTable("TestTable")
      .withPartitionKey("id", DataTypes.TEXT)
      .withColumn("foo", DataTypes.TEXT)
      .withColumn("bar", DataTypes.INT)

  "Cassandra table" should "be converted into Spark Schema" in {
    //val sparkSchema: StructType = myDataFrame.schema
    val sparkSchema: StructType = StructType(
      List(
        StructField("id", StringType, nullable = false),
        StructField("foo", StringType, nullable = false),
        StructField("bar", IntegerType, nullable = false)
      ))
    SqlInstanceToBigQuery[CreateTable]
    cassandraTable.asSparkSchema shouldBe sparkSchema
  }
}
