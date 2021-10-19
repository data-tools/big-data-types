package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{StructField, StructType}
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes into Spark specific fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlInstanceToSpark[A] {

  /** @return a list of [[StructField]]s that represents [[A]]
    */
  def sparkFields(value: A): List[StructField]
}

object SqlInstanceToSpark {

  def apply[A](implicit a: SqlInstanceToSpark[A]): SqlInstanceToSpark[A] = a

  /** Instance derivation via SqlTypeConversion. It uses `getSchema` from SqlTypeToSpark Type Class
    */
  implicit def sparkFromSqlInstanceConversion[A: SqlInstanceConversion](implicit f: Formats): SqlInstanceToSpark[A] =
    new SqlInstanceToSpark[A] {

      override def sparkFields(value: A): List[StructField] =
        SqlTypeToSpark.getSchema(SqlInstanceConversion[A].getType(value))
    }

  /** Allows the syntax myInstance.sparkFields and myInstance.sparkSchemas
    * for any instance of type A: SqlInstanceConversion
    * e.g: myBigQueryTable.asSparkSchema
    */
  implicit class InstanceSyntax[A: SqlInstanceToSpark](value: A) {
    def asSparkFields: List[StructField] = SqlInstanceToSpark[A].sparkFields(value)
    def asSparkSchema: StructType = StructType(SqlInstanceToSpark[A].sparkFields(value))
  }
}
