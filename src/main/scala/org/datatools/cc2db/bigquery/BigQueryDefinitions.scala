package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{Schema, StandardTableDefinition}
import org.datatools.cc2db.types.BigQueryTypes
import scala.jdk.CollectionConverters.IterableHasAsJava

object BigQueryDefinitions {

  /**
   * TODO: Add partition columns
   */
  def generateTableDefinition[A]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A]).build()
  def generateTableDefinition[A, B]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B]).build()
  def generateTableDefinition[A, B, C]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C]).build()
  def generateTableDefinition[A, B, C, D]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D]).build()
  def generateTableDefinition[A, B, C, D, E]: StandardTableDefinition = StandardTableDefinition.newBuilder().setSchema(generateSchema[A, B, C, D, E]).build()

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
