package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes received as instance into BigQuery specific fields
  * This uses [[SqlTypeToBigQuery]] to create BigQuery Fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlInstanceToBigQuery[A] {

  /** @param value an instance of [[A]]
    * @return a list of [[Field]]s that represents [[A]]
    */
  def bigQueryFields(value: A): List[Field]
}

object SqlInstanceToBigQuery {

  /** Summoner method
    */
  def apply[A](implicit a: SqlInstanceToBigQuery[A]): SqlInstanceToBigQuery[A] = a

  //TODO change it for the compressed syntax after having everything well documented
  /** Instance derivation via SqlTypeConversion. It uses `getSchema` from BigQueryTypes Type Class
    */
  implicit def fieldsFromSqlInstanceConversion[A: SqlInstanceConversion](implicit
      f: Formats
  ): SqlInstanceToBigQuery[A] =
    new SqlInstanceToBigQuery[A] {

      /** @return a list of [[Field]]s that represents [[A]]
        */
      override def bigQueryFields(value: A): List[Field] =
        SqlTypeToBigQuery.getSchema(SqlInstanceConversion[A].getType(value))
    }

  /** Simplify syntax for SqlType, allows:
    * SqlInstanceToBigQuery[SqlType].bigQueryFields(mySqlTypeInstance)
    */
  implicit def fieldsFromSqlType(implicit f: Formats): SqlInstanceToBigQuery[SqlType] =
    new SqlInstanceToBigQuery[SqlType] {

      /** @return a list of [[Field]]s that represents [[SqlType]]
        */
      override def bigQueryFields(value: SqlType): List[Field] =
        SqlTypeToBigQuery.getSchema(value)
    }

  /** Allows the syntax myInstance.asBigQuery for any instance of type A: SqlInstanceConversion
    */
  implicit class InstanceSyntax[A: SqlInstanceToBigQuery](value: A) {
    def asBigQuery: List[Field] = SqlInstanceToBigQuery[A].bigQueryFields(value)
  }
}
