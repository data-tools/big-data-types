package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{StructField, StructType}


/**
  * Wrapper for SparkTypes, it makes easier to use and give `schema` and `fields` method for multiple case classes
  */
object SparkSchemas {
  def schema[A: SparkTypes]: StructType = SparkTypes[A].sparkSchema
  def schema[A: SparkTypes, B: SparkTypes]: StructType = StructType(fields[A, B])
  def schema[A: SparkTypes, B: SparkTypes, C: SparkTypes]: StructType = StructType(fields[A, B, C])
  def schema[A: SparkTypes, B: SparkTypes, C: SparkTypes, D: SparkTypes]: StructType = StructType(fields[A, B, C, D])
  def schema[A: SparkTypes, B: SparkTypes, C: SparkTypes, D: SparkTypes, E: SparkTypes]: StructType = StructType(fields[A, B, C, D, E])

  def fields[A: SparkTypes]: List[StructField] = SparkTypes[A].sparkFields
  def fields[A: SparkTypes, B: SparkTypes]: List[StructField] = SparkTypes[A].sparkFields ++ SparkTypes[B].sparkFields
  def fields[A: SparkTypes, B: SparkTypes, C: SparkTypes]: List[StructField] = SparkTypes[A].sparkFields ++ SparkTypes[B].sparkFields ++ SparkTypes[C].sparkFields
  def fields[A: SparkTypes, B: SparkTypes, C: SparkTypes, D: SparkTypes]: List[StructField] = SparkTypes[A].sparkFields ++ SparkTypes[B].sparkFields ++ SparkTypes[C].sparkFields ++ SparkTypes[D].sparkFields
  def fields[A: SparkTypes, B: SparkTypes, C: SparkTypes, D: SparkTypes, E: SparkTypes]: List[StructField] = SparkTypes[A].sparkFields ++ SparkTypes[B].sparkFields ++ SparkTypes[C].sparkFields ++ SparkTypes[D].sparkFields ++ SparkTypes[E].sparkFields
}
