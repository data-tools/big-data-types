package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable
import com.datastax.oss.driver.api.querybuilder.schema.{CreateTable, CreateTableStart}

object CassandraTables {

  def table[A: SqlTypeToCassandra](table: String, primaryKey: String): CreateTable = {
    val tuples: Seq[(String, DataType)] = SqlTypeToCassandra[A].cassandraFields
    val t: CreateTableStart = createTable(table)
    val k: CreateTable = t.withPartitionKey("test", DataTypes.TEXT)
    val withoutPk = tuples.dropWhile(tuple => tuple._1 == primaryKey)
    withoutPk.foldLeft(k) ((tmpTable,tuple) => tmpTable.withColumn(tuple._1, tuple._2))
  }
  /*
try {
  val session = CqlSession.builder.build
  try {
    val createKs: CreateKeyspace = createKeyspace("cycling").withSimpleStrategy(1)
    session.execute(createKs.build)
    val table: CreateTable =
      createTable("cycling", "cyclist_name")
        .withPartitionKey("id", DataTypes.UUID)
        .withColumn("lastname", DataTypes.TEXT)
        .withColumn("firstname", DataTypes.TEXT)
    session.execute(createTable.build)
  }
  finally if (session != null) session.close()
}

 */
}
