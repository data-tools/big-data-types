package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{
  BigQuery,
  BigQueryError,
  BigQueryException,
  BigQueryOptions,
  Table,
  TableDefinition,
  TableId,
  TableInfo
}
import org.datatools.cc2db.types.BigQueryTypes
import org.datatools.cc2db.bigquery.BigQueryDefinitions._

import scala.util.{Failure, Try}

object BigQueryTable {

  lazy val service: BigQuery = BigQueryOptions.getDefaultInstance.getService

  /** Create a table without partitions
    */
  def createTable[T: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] =
    createTable[T](datasetName, tableName, None)

  /** Create partitioned table
    */
  def createTable[T: BigQueryTypes](datasetName: String,
                                    tableName: String,
                                    timePartitionColumn: String
  ): Either[BigQueryError, Table] =
    createTable[T](datasetName, tableName, Some(timePartitionColumn))

  /** Create a table in BigQuery
    */
  private def createTable[A: BigQueryTypes](datasetName: String,
                                            tableName: String,
                                            timePartitionColumn: Option[String]
  ): Either[BigQueryError, Table] =
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A](timePartitionColumn))

  /** Giving a `TableId` and a `TableDefinition` tries to create the table in BigQuery
    *
    * @param tableId         desired table
    * @param tableDefinition definition of the table
    * @return `Either[BigQueryError, Table]`
    */
  def tryTable(tableId: TableId, tableDefinition: TableDefinition): Either[BigQueryError, Table] = {
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

}
