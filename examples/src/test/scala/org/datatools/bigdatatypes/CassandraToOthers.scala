package org.datatools.bigdatatypes

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import org.datatools.bigdatatypes.BigQueryTestTypes.basicFields
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery.{InstanceSchemaSyntax, InstanceSyntax}
import org.datatools.bigdatatypes.cassandra.CassandraTypeConversion.cassandraCreateTable
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class CassandraToOthers extends UnitSpec {

  behavior of "Cassandra Types to other types"

  val cassandraTable: CreateTable =
    createTable("TestTable")
      .withPartitionKey("id", DataTypes.TEXT)
      .withColumn("foo", DataTypes.TEXT)
      .withColumn("bar", DataTypes.INT)

  "Cassandra table" should "be converted into BigQuery Schema" in {
    val fields = List(
      Field.newBuilder("id", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("foo", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
      Field.newBuilder("bar", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build()
    )
    val bqSchema = Schema.of(toJava(fields))
    SqlInstanceToBigQuery[CreateTable]
    cassandraTable.asBigQuery.schema shouldBe bqSchema
  }
}
