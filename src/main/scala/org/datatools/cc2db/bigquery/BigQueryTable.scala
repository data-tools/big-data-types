package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{
  BigQuery,
  BigQueryError,
  BigQueryException,
  BigQueryOptions,
  Schema,
  StandardTableDefinition,
  Table,
  TableDefinition,
  TableId,
  TableInfo
}
import org.datatools.cc2db.types.BigQueryTypes

import scala.jdk.CollectionConverters.IterableHasAsJava
import scala.util.{Failure, Try}

trait BigQueryTable {

  lazy val service: BigQuery = BigQueryOptions.getDefaultInstance.getService

  /** Create partitioned table
    */
  def createTable[T: BigQueryTypes](datasetName: String,
                                    tableName: String,
                                    timePartitionColumn: String
  ): Either[BigQueryError, Table] =
    createTable[T](datasetName, tableName, Some(timePartitionColumn))

  /** Create a table without partitions
    */
  def createTable[T: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] =
    createTable[T](datasetName, tableName, None)

  /** Create a table in BigQuery+
    * TODO split this in more functions probably
    */
  private def createTable[T: BigQueryTypes](datasetName: String,
                                            tableName: String,
                                            timePartitionColumn: Option[String]
  ): Either[BigQueryError, Table] = {
    val tableId: TableId = TableId.of(datasetName, tableName)
    val fields = BigQueryTypes[T].getFields
    // Table schema definition
    val schema: Schema = Schema.of(fields.asJava)
    //TODO add partition column
    val tableDefinition: TableDefinition =
      StandardTableDefinition.newBuilder().setSchema(schema).build()

    val tableInfo: TableInfo = TableInfo.newBuilder(tableId, tableDefinition).build()
    val tryTable: Try[Table] = Try(service.create(tableInfo)) recoverWith { case bigQueryException: BigQueryException =>
      if (bigQueryException.getError.getReason == "duplicate")
        Try(service.getTable(tableId))
      else
        Failure(bigQueryException)
    }
    tryTable.toEither.left.map { case bigQueryException: BigQueryException =>
      bigQueryException.getError
    }
  }

  //TODO add other traits like BigQueryTable[A, B] that will create a table concatenating fields from A and B

}
