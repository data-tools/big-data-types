package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder._
import com.datastax.oss.driver.api.querybuilder.schema.{CreateKeyspace, CreateTable}
import org.datatools.bigdatatypes.basictypes.{SqlType, SqlTypeMode}
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.Repeated
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats

trait SqlTypeToCassandra[A] {

  def cassandraFields: List[(String, DataType)]
}

object SqlTypeToCassandra {

  /** Summoner method */
  def apply[A](implicit instance: SqlTypeToCassandra[A]): SqlTypeToCassandra[A] = instance

  /** Factory constructor - allows easier construction of instances */
  def instance[A](fs: List[(String, DataType)]): SqlTypeToCassandra[A] =
    new SqlTypeToCassandra[A] {
      def cassandraFields: List[(String, DataType)] = fs
    }

  private def getSchema(sqlType: SqlType)(implicit f: Formats): List[(String, DataType)] = sqlType match {
    case SqlStruct(Nil, _) => Nil
    case SqlStruct((name, sqlType) :: records, mode) =>
      getSchemaWithName(f.transformKey(name, sqlType), sqlType) :: getSchema(SqlStruct(records, mode))
  }

  private def getSchemaWithName(name: String, sqlType: SqlType)(implicit f: Formats): (String, DataType) =
    sqlType match {
      case SqlInt(mode)       => (name, cassandraType(mode, DataTypes.INT))
      case SqlLong(mode)      => (name, cassandraType(mode, DataTypes.BIGINT))
      case SqlFloat(mode)     => (name, cassandraType(mode, DataTypes.FLOAT))
      case SqlDouble(mode)    => (name, cassandraType(mode, DataTypes.DOUBLE))
      case SqlDecimal(mode)   => (name, cassandraType(mode, DataTypes.DECIMAL))
      case SqlBool(mode)      => (name, cassandraType(mode, DataTypes.BOOLEAN))
      case SqlString(mode)    => (name, cassandraType(mode, DataTypes.TEXT))
      case SqlTimestamp(mode) => (name, cassandraType(mode, DataTypes.TIMESTAMP))
      case SqlDate(mode)      => (name, cassandraType(mode, DataTypes.DATE))
      //case SqlStruct(records, mode) => _ No nested objects in Cassandra
    }

  /** In case of repeated mode, we use Cassandra List
    */
  private def cassandraType(mode: SqlTypeMode, cassandraType: DataType): DataType = mode match {
    case Repeated => DataTypes.listOf(cassandraType)
    case _        => cassandraType
  }

  /** Instance derivation via SqlTypeConversion.
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): SqlTypeToCassandra[A] =
    instance(getSchema(SqlTypeConversion[A].getType))

  try {
    val session = CqlSession.builder.build
    try {
      val createKs: CreateKeyspace = createKeyspace("cycling").withSimpleStrategy(1)
      session.execute(createKs.build)
      val table: CreateTable =
        createTable("cycling", "cyclist_name")
          .withPartitionKey("id", DataTypes.UUID)
          .withColumn("lastname", DataTypes.TEXT)
          .withColumn("firstname", DataTypes.TEXT)
      session.execute(createTable.build)
    }
    finally if (session != null) session.close()
  }
}
