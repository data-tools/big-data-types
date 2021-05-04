package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable

object CassandraTables {

  //def table[A: SqlTypeToCassandra](schema: String, table: String): CreateTable = SqlTypeToCassandra[A].cassandraFields
}
