package org.datatools.bigdatatypes

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable

class CassandraToOthers extends UnitSpec {

  behavior of "Cassandra Types to other types"

  val cassandraTable: CreateTable =
    createTable("TestTable")
      .withPartitionKey("id", DataTypes.TEXT)
      .withColumn("foo", DataTypes.TEXT)
      .withColumn("bar", DataTypes.INT)

  // TODO missing implementation of CassandraTypeConversion that will allow any CreateTable to be transformed

}
