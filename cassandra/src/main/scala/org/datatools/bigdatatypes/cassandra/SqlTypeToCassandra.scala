package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder._
import com.datastax.oss.driver.api.querybuilder.schema.{CreateKeyspace, CreateTable}


trait SqlTypeToCassandra[A] {

  def cassandraFields: List[(String, DataType)]

}

object SqlTypeToCassandra {

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
    } finally if (session != null) session.close()
  }
}
