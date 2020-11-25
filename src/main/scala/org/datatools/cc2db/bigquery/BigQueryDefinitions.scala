package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{Schema, StandardTableDefinition, TableDefinition, TimePartitioning}
import org.datatools.cc2db.types.BigQueryTypes

import scala.jdk.CollectionConverters.IterableHasAsJava

object BigQueryDefinitions {


  /** Given a column name created a TimePartition object */
  def generateTimePartitionColumn(columnName: String): TimePartitioning = TimePartitioning.newBuilder(TimePartitioning.Type.DAY).setField(columnName).build()

  /**
   * Generates a TAble definition without partitions
   */
  def generateTableDefinition[A]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A]).build()
  def generateTableDefinition[A, B]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B]).build()
  def generateTableDefinition[A, B, C]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C]).build()
  def generateTableDefinition[A, B, C, D]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D]).build()
  def generateTableDefinition[A, B, C, D, E]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D, E]).build()

  /**
   * Generates a Table definition time partitioned
   * @param partitionColumn name of the column that will be a time partition
   */
  def generateTableDefinition[A](partitionColumn: String): StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A]).setTimePartitioning(generateTimePartitionColumn(partitionColumn)).build()
  def generateTableDefinition[A, B](partitionColumn: String): StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B]).setTimePartitioning(generateTimePartitionColumn(partitionColumn)).build()
  def generateTableDefinition[A, B, C](partitionColumn: String): StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C]).setTimePartitioning(generateTimePartitionColumn(partitionColumn)).build()
  def generateTableDefinition[A, B, C, D](partitionColumn: String): StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D]).setTimePartitioning(generateTimePartitionColumn(partitionColumn)).build()
  def generateTableDefinition[A, B, C, D, E](partitionColumn: String): StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D, E]).setTimePartitioning(generateTimePartitionColumn(partitionColumn)).build()

  def generateSchema[A]: Schema = Schema.of(BigQueryTypes[A].getFields.asJava)
  def generateSchema[A, B]: Schema = Schema.of((BigQueryTypes[A].getFields ++ BigQueryTypes[B].getFields).asJava)
  def generateSchema[A, B, C]: Schema =
    Schema.of((BigQueryTypes[A].getFields ++ BigQueryTypes[B].getFields ++ BigQueryTypes[C].getFields).asJava)
  def generateSchema[A, B, C, D]: Schema = Schema.of(
    (BigQueryTypes[A].getFields ++
      BigQueryTypes[B].getFields ++
      BigQueryTypes[C].getFields ++
      BigQueryTypes[D].getFields).asJava
  )
  def generateSchema[A, B, C, D, E]: Schema = Schema.of(
    (BigQueryTypes[A].getFields ++
      BigQueryTypes[B].getFields ++
      BigQueryTypes[C].getFields ++
      BigQueryTypes[D].getFields ++
      BigQueryTypes[E].getFields).asJava
  )

}
