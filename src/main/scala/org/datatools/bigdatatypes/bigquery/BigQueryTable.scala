package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{BigQuery, BigQueryError, BigQueryException, BigQueryOptions, Table, TableDefinition, TableId, TableInfo}
import org.datatools.bigdatatypes.bigquery.BigQueryDefinitions.generateTableDefinition
import scala.util.{Failure, Try}

object BigQueryTable {

  lazy val service: BigQuery = BigQueryOptions.getDefaultInstance.getService

  /** Create a table without partitions
    */
  def createTable[A: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A](datasetName, tableName, None)
  def createTable[A: BigQueryTypes, B: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A, B](datasetName, tableName, None)
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A, B, C](datasetName, tableName, None)
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A, B, C, D](datasetName, tableName, None)
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes](datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A, B, C, D, E](datasetName, tableName, None)

  /** Create partitioned table
    */
  def createTable[A: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C, D](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C, D, E](datasetName, tableName, Some(timePartitionColumn))

  /** Create a table in BigQuery
    */
  private def createTable[A: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A](timePartitionColumn))
  private def createTable[A: BigQueryTypes, B: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A, B](timePartitionColumn))
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A, B, C](timePartitionColumn))
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A, B, C, D](timePartitionColumn))
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = tryTable(TableId.of(datasetName, tableName), generateTableDefinition[A, B, C, D, E](timePartitionColumn))

  /** Giving a `TableId` and a `TableDefinition` tries to create the table in BigQuery
   *
   * @param tableId         desired table
   * @param tableDefinition definition of the table
   * @return `Either[BigQueryError, Table]`
   */
  def tryTable(tableId: TableId, tableDefinition: TableDefinition): Either[BigQueryError, Table] = {
    val tableInfo: TableInfo = TableInfo.newBuilder(tableId, tableDefinition).build()
    val tryTable: Try[Table] = Try(service.create(tableInfo)) recoverWith {
      case bigQueryException: BigQueryException =>
        if (bigQueryException.getError.getReason == "duplicate")
          Try(service.getTable(tableId))
        else
          Failure(bigQueryException)
      case e: Exception => Failure(e)
    }
    tryTable.toEither.left.map {
      case bigQueryException: BigQueryException => bigQueryException.getError
      case e: Exception => new BigQueryError("Unknown error", e.getClass.toString, e.getMessage)
    }
  }

}
