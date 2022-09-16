package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Schema
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion

/**
  * Public API for generating BigQuery Schemas.
  * Any type implementing [[SqlTypeToBigQuery]] or [[SqlInstanceToBigQuery]] can be converted into a BigQuery [[Schema]]
  * If multiple types are given, the resulting schema will be the concatenation of them.
  */
object BigQuerySchemas {

  /**
    * Given any type that implements [[SqlTypeToBigQuery]] returns the BigQuery Schema for that type
    * @tparam A is any type implementing [[SqlTypeToBigQuery]]
    * @return [[Schema]] ready to be used in BigQuery
    */
  def schema[A: SqlTypeToBigQuery]: Schema = BigQueryDefinitions.generateSchema[A]
  def schema[A: SqlTypeToBigQuery, B: SqlTypeToBigQuery]: Schema = BigQueryDefinitions.generateSchema[A, B]
  def schema[A: SqlTypeToBigQuery, B: SqlTypeToBigQuery, C: SqlTypeToBigQuery]: Schema = BigQueryDefinitions.generateSchema[A, B, C]
  def schema[A: SqlTypeToBigQuery, B: SqlTypeToBigQuery, C: SqlTypeToBigQuery, D: SqlTypeToBigQuery]: Schema = BigQueryDefinitions.generateSchema[A, B, C, D]
  def schema[A: SqlTypeToBigQuery, B: SqlTypeToBigQuery, C: SqlTypeToBigQuery, D: SqlTypeToBigQuery, E: SqlTypeToBigQuery]: Schema = BigQueryDefinitions.generateSchema[A, B, C, D, E]

  /**
    * Given an instance of a type implementing [[SqlInstanceToBigQuery]] returns a [[Schema]] to be used in BigQuery
    * @param value an instance of type A
    * @tparam A is a type implementing [[SqlInstanceToBigQuery]]
    * @return [[Schema]] with the same structure as the given input
    */
  def schema[A: SqlInstanceToBigQuery](value: A): Schema = BigQueryDefinitions.generateSchema[A](value)
}
