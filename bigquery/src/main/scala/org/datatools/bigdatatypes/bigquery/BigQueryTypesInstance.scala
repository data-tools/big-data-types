package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes into BigQuery specific fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait BigQueryTypesInstance[A] {

  /** @return a list of [[Field]]s that represents [[A]]
    */
  def bigQueryFields(value: A): List[Field]
}

object BigQueryTypesInstance {

  /** Summoner method
    */
  def apply[A](implicit a: BigQueryTypesInstance[A]): BigQueryTypesInstance[A] = a

  /** Factory constructor - allows easier construction of instances
    */
  def instance[A](f: A => List[Field]): BigQueryTypesInstance[A] =
    new BigQueryTypesInstance[A] {
      def bigQueryFields(value: A): List[Field] = f(value)
    }

  /** Instance derivation via SqlTypeConversion. It uses `getSchema` from BigQueryTypes Type Class
    */
  implicit def fieldsFromSqlInstanceConversion[A: SqlInstanceConversion](implicit
      f: Formats
  ): BigQueryTypesInstance[A] =
    new BigQueryTypesInstance[A] {

      /** @return a list of [[Field]]s that represents [[A]]
        */
      override def bigQueryFields(value: A): List[Field] =
        BigQueryTypes.getSchema(SqlInstanceConversion[A].getType(value))
    }

  /** Instance derivation via SqlTypeConversion.
    * It allows BigQueryTypesInstance[Dummy].bigQueryFields(myDummy)
    * Same as BigQueryTypes[Dummy].bigQueryFields but giving an instance of a case class
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): BigQueryTypesInstance[A] =
    new BigQueryTypesInstance[A] {

      /** @return a list of [[Field]]s that represents [[A]]
        */
      override def bigQueryFields(value: A): List[Field] =
        BigQueryTypes.getSchema(SqlTypeConversion[A].getType)
    }

  /** Allows the syntax myInstance.bigQueryFields for any instance of type A: SqlInstanceConversion
    */
  implicit class InstanceSyntax[A: BigQueryTypesInstance](value: A) {
    def bigQueryFields: List[Field] = BigQueryTypesInstance[A].bigQueryFields(value)
  }

  //TODO add another implicit class to resolve mySqlInstance.bigQueryFields
}
