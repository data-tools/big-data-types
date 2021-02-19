package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{Schema, StandardTableDefinition, TimePartitioning}
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava


private[bigquery] object BigQueryDefinitions {

  /** Given a column name created a TimePartition object */
  def generateTimePartitionColumn(columnName: String): TimePartitioning =
    TimePartitioning.newBuilder(TimePartitioning.Type.DAY).setField(columnName).build()

  /** Generates a Table definition with or without Time Partitioning Column
    * @param schema [[Schema]] for BigQuery
    * @param partitionColumn name of the column that will be a time partition, if None, no partition will be created
    */
  def generateTableDefinition(schema: Schema, partitionColumn: Option[String]): StandardTableDefinition = {
    val builder: StandardTableDefinition.Builder = StandardTableDefinition.newBuilder().setSchema(schema)
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
  def generateSchema[A: BigQueryTypes]: Schema = Schema.of(toJava(BigQueryTypes[A].bigQueryFields))

  def generateSchema[A: BigQueryTypes, B: BigQueryTypes]: Schema =
    Schema.of(toJava(BigQueryTypes[A].bigQueryFields ++ BigQueryTypes[B].bigQueryFields))

  def generateSchema[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes]: Schema =
    Schema.of(toJava(BigQueryTypes[A].bigQueryFields ++ BigQueryTypes[B].bigQueryFields ++ BigQueryTypes[C].bigQueryFields))

  def generateSchema[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes]: Schema =
    Schema.of(
      toJava(BigQueryTypes[A].bigQueryFields ++
        BigQueryTypes[B].bigQueryFields ++
        BigQueryTypes[C].bigQueryFields ++
        BigQueryTypes[D].bigQueryFields)
    )

  def generateSchema[A: BigQueryTypes, B: BigQueryTypes, C: BigQueryTypes, D: BigQueryTypes, E: BigQueryTypes]: Schema =
    Schema.of(
      toJava(BigQueryTypes[A].bigQueryFields ++
        BigQueryTypes[B].bigQueryFields ++
        BigQueryTypes[C].bigQueryFields ++
        BigQueryTypes[D].bigQueryFields ++
        BigQueryTypes[E].bigQueryFields)
    )

  /** For BigQueryTypesInstance
    * Multiples instances not supported for now
    */
  def generateSchema[A: BigQueryTypesInstance](value: A): Schema = Schema.of(toJava(BigQueryTypesInstance[A].bigQueryFields(value)))

}
