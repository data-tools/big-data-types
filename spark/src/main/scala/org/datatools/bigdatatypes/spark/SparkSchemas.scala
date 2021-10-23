package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{StructField, StructType}
import org.datatools.bigdatatypes.formats.Formats


/**
  * Wrapper for SparkTypes, it makes easier to use and give `schema` and `fields` method for multiple case classes
  */
object SparkSchemas {
  def schema[A: SqlTypeToSpark]: StructType = SqlTypeToSpark[A].sparkSchema
  def schema[A: SqlTypeToSpark, B: SqlTypeToSpark]: StructType = StructType(fields[A, B])
  def schema[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark]: StructType = StructType(fields[A, B, C])
  def schema[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark, D: SqlTypeToSpark]: StructType = StructType(fields[A, B, C, D])
  def schema[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark, D: SqlTypeToSpark, E: SqlTypeToSpark]: StructType = StructType(fields[A, B, C, D, E])

  def fields[A: SqlTypeToSpark]: List[StructField] = SqlTypeToSpark[A].sparkFields
  def fields[A: SqlTypeToSpark, B: SqlTypeToSpark]: List[StructField] = SqlTypeToSpark[A].sparkFields ++ SqlTypeToSpark[B].sparkFields
  def fields[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark]: List[StructField] = SqlTypeToSpark[A].sparkFields ++ SqlTypeToSpark[B].sparkFields ++ SqlTypeToSpark[C].sparkFields
  def fields[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark, D: SqlTypeToSpark]: List[StructField] = SqlTypeToSpark[A].sparkFields ++ SqlTypeToSpark[B].sparkFields ++ SqlTypeToSpark[C].sparkFields ++ SqlTypeToSpark[D].sparkFields
  def fields[A: SqlTypeToSpark, B: SqlTypeToSpark, C: SqlTypeToSpark, D: SqlTypeToSpark, E: SqlTypeToSpark]: List[StructField] = SqlTypeToSpark[A].sparkFields ++ SqlTypeToSpark[B].sparkFields ++ SqlTypeToSpark[C].sparkFields ++ SqlTypeToSpark[D].sparkFields ++ SqlTypeToSpark[E].sparkFields

  /** These methods work for specific instances of other types
   * e.g: SparkSchemas.schema(myBigQuerySchema)
   */

  def fields[A: SqlInstanceToSpark](value: A)(implicit f: Formats): List[StructField] = SqlInstanceToSpark[A].sparkFields(value)
  def schema[A: SqlInstanceToSpark](value: A)(implicit f: Formats): StructType = StructType(fields(value))
  //TODO make these methods available for multiple concatenated instances


}
