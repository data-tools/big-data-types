package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.StructField

/** Type class to convert generic SqlTypes into BigQuery specific fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SparkTypesInstance[A] {

  /** @return a list of [[StructField]]s that represents [[A]]
    */
  def sparkFields(value: A): List[StructField]
}

object SparkTypesInstance {
  //TODO implement this (similar to [[BigQueryTypesInstance]]) that will allow to convert an instance of SqlType into an SparkSchema
  //TODO and doing so, any type that implement SqlInstanceConversion will be able to be converted into SparkSchema
}