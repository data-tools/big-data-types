package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{BigQuery, BigQueryError, BigQueryException, BigQueryOptions, Table, TableDefinition, TableId, TableInfo}
import org.datatools.bigdatatypes.bigquery.BigQueryDefinitions.{generateSchema, generateTableDefinition}

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
  /** For Instances as input */
  def createTable[A: BigQueryTypesInstance](value: A, datasetName: String, tableName: String): Either[BigQueryError, Table] = createTable[A](value: A, datasetName, tableName, None)

  /** Create partitioned table
    */
  def createTable[A: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C, D](datasetName, tableName, Some(timePartitionColumn))
  def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A, B, C, D, E](datasetName, tableName, Some(timePartitionColumn))

  /** For Instances as input */
  def createTable[A: BigQueryTypesInstance](value: A, datasetName: String, tableName: String, timePartitionColumn: String): Either[BigQueryError, Table] = createTable[A](value, datasetName, tableName, Some(timePartitionColumn))

  /** Create a table in BigQuery
    */
  private def createTable[A: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A]
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }

  private def createTable[A: BigQueryTypes, B: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A, B]
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A, B, C]
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A, B, C, D]
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }
  private def createTable[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes](datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A, B, C, D, E]
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }

  /** For Instances - Only one is accepted for now */
  private def createTable[A: BigQueryTypesInstance](value: A, datasetName: String, tableName: String, timePartitionColumn: Option[String]): Either[BigQueryError, Table] = {
    val schema = generateSchema[A](value)
    tryTable(TableId.of(datasetName, tableName), generateTableDefinition(schema, timePartitionColumn))
  }

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
      case e: Exception => new BigQueryError("Unknown error, probably a Service Account is not configured", e.getClass.toString, e.getMessage)
    }
  }

}
