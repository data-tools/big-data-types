package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable
import com.datastax.oss.driver.api.querybuilder.schema.{CreateTable, CreateTableStart}

object CassandraTables {

  /**
    * Build a CreateTable object with the given Product, table name and primary Key.
    * If the primary key is not found in the Product, it will be created as Text
    * @param tableName name of the table
    * @param primaryKey field name for the primary key, if it doesn't exist, it will be created as Text
    * @tparam A any Product type
    * @return CreateTable object
    */
  def table[A: SqlTypeToCassandra](tableName: String, primaryKey: String): CreateTable = {
    val tuples: Seq[(String, DataType)] = SqlTypeToCassandra[A].cassandraFields
    val t: CreateTableStart = createTable(tableName)
    val pk = tuples.find(tuple => tuple._1 == primaryKey)
    //if partition is not found, create it as Text
    val tmpTable = pk.fold(t.withPartitionKey(primaryKey, DataTypes.TEXT))(tuple => t.withPartitionKey(tuple._1, tuple._2))
    val withoutPk = tuples.dropWhile(tuple => tuple._1 == primaryKey)
    withoutPk.foldLeft(tmpTable)((tmpTable,tuple) => tmpTable.withColumn(tuple._1, tuple._2))
  }
}
