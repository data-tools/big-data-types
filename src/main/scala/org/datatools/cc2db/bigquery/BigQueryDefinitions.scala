package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{Schema, StandardTableDefinition, TimePartitioning}
import org.datatools.cc2db.types.BigQueryTypes

import scala.jdk.CollectionConverters.IterableHasAsJava

object BigQueryDefinitions {

  /** Given a column name created a TimePartition object */
  def generateTimePartitionColumn(columnName: String): TimePartitioning =
    TimePartitioning.newBuilder(TimePartitioning.Type.DAY).setField(columnName).build()

  /** Generates a Table definition with or without Time Partitioning Column
    * @param partitionColumn name of the column that will be a time partition, if None, no partition will be created
    */
  def generateTableDefinition[A](partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder = StandardTableDefinition.newBuilder().setSchema(generateSchema[A])
    addPartitionToBuilder(builder, partitionColumn).build()
  }

  def generateTableDefinition[A, B](partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B])
    addPartitionToBuilder(builder, partitionColumn).build()
  }

  def generateTableDefinition[A, B, C](partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder =
      StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C])
    addPartitionToBuilder(builder, partitionColumn).build()
  }

  def generateTableDefinition[A, B, C, D](partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder =
      StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D])
    addPartitionToBuilder(builder, partitionColumn).build()
  }

  def generateTableDefinition[A, B, C, D, E](partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder =
      StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D, E])
    addPartitionToBuilder(builder, partitionColumn).build()
  }

  /** Given a builder and a column name, add a Time Partitioning column to the builder
    *
    * @param builder         for BigQuery tables
    * @param partitionColumn name of the column to be used as Time Partitioning column
    */
  def addPartitionToBuilder(builder: StandardTableDefinition.Builder,
                            partitionColumn: Option[String]
  ): StandardTableDefinition.Builder =
    partitionColumn.map(v => builder.setTimePartitioning(generateTimePartitionColumn(v))).getOrElse(builder)

  /** Generates a BigQuery Table Schema given a type A
    */
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
