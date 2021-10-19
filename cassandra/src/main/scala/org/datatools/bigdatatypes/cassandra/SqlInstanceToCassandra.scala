package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.DataType
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.conversions.SqlInstanceConversion
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes received as instance into Cassandra specific fields
  * This uses [[SqlTypeToCassandra]] to create Cassandra Fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlInstanceToCassandra[A] {

  /** @param value an instance of [[A]]
    * @return a list of [[(String, DataType)]]s that represents [[A]]
    */
  def cassandraFields(value: A): List[(String, DataType)]
}

object SqlInstanceToCassandra {

  /** Summoner method */
  def apply[A](implicit a: SqlInstanceToCassandra[A]): SqlInstanceToCassandra[A] = a

  /** Instance derivation via SqlTypeConversion. It uses `getSchema` from SqlTypeToCassandra Type Class
    */
  implicit def cassandraFromSqlInstanceConversion[A: SqlInstanceConversion](implicit
      f: Formats
  ): SqlInstanceToCassandra[A] =
    new SqlInstanceToCassandra[A] {

      /** @param value an instance of [[A]]
        * @return a list of [[(String, DataType)]]s that represents [[A]]
        */
      override def cassandraFields(value: A): List[(String, DataType)] =
        SqlTypeToCassandra.getSchema(SqlInstanceConversion[A].getType(value))
    }

  /** Simplify syntax for SqlType, allows:
    * SqlInstanceToCassandra[SqlType].cassandraFields(mySqlTypeInstance)
    */
  implicit def fieldsFromSqlType(implicit f: Formats): SqlInstanceToCassandra[SqlType] =
    new SqlInstanceToCassandra[SqlType] {

      override def cassandraFields(value: SqlType): List[(String, DataType)] =
        SqlTypeToCassandra.getSchema(value)
    }

  /** Allows the syntax myInstance.asCassandra for any instance of type A: SqlInstanceConversion
    */
  implicit class InstanceSyntax[A: SqlInstanceToCassandra](value: A) {
    def asCassandra: List[(String, DataType)] = SqlInstanceToCassandra[A].cassandraFields(value)
  }
}

