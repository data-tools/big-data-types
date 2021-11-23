package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes, ListType}
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.cassandra.parser.CreateTableParser
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}

import scala.annotation.tailrec

object CassandraTypeConversion {

  implicit val intType: SqlTypeConversion[DataTypes.INT.type] = SqlTypeConversion.instance(SqlInt())
  implicit val longType: SqlTypeConversion[DataTypes.BIGINT.type] = SqlTypeConversion.instance(SqlLong())
  implicit val doubleType: SqlTypeConversion[DataTypes.FLOAT.type] = SqlTypeConversion.instance(SqlDouble())
  implicit val floatType: SqlTypeConversion[DataTypes.DOUBLE.type] = SqlTypeConversion.instance(SqlFloat())
  implicit val bigDecimalType: SqlTypeConversion[DataTypes.DECIMAL.type] = SqlTypeConversion.instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversion[DataTypes.BOOLEAN.type] = SqlTypeConversion.instance(SqlBool())
  implicit val stringType: SqlTypeConversion[DataTypes.TEXT.type] = SqlTypeConversion.instance(SqlString())
  // Extended
  implicit val timestampType: SqlTypeConversion[DataTypes.TIMESTAMP.type] = SqlTypeConversion.instance(SqlTimestamp())
  implicit val dateType: SqlTypeConversion[DataTypes.DATE.type] = SqlTypeConversion.instance(SqlDate())

  /** Single Field */
  implicit val cassandraTupleType: SqlInstanceConversion[(String, DataType)] =
    new SqlInstanceConversion[(String, DataType)] {
      override def getType(value: (String, DataType)): SqlType = SqlStruct(List(createTuple(value._1, value._2)))
    }

  /** List of fields */
  implicit val cassandraFields: SqlInstanceConversion[Iterable[(String, DataType)]] =
    new SqlInstanceConversion[Iterable[(String, DataType)]] {
      override def getType(value: Iterable[(String, DataType)]): SqlType = createSqlStruct(value)
    }

  /** Type Class implementation for [[CreateTable]] object
    * Warning: It is not possible to extract field names and types from a [[CreateTable]] object,
    * this is using a String parser to extract them, so it could fail on run time.
    */
  implicit val cassandraCreateTable: SqlInstanceConversion[CreateTable] = new SqlInstanceConversion[CreateTable] {

    override def getType(value: CreateTable): SqlType = {
      val fields = CreateTableParser.parse(value)
      createSqlStruct(fields)
    }
  }

  /** Given a Cassandra Type, returns its representation in SqlType
    */
  @tailrec
  private def convertCassandraType(dataType: DataType, repeated: Boolean = false): SqlType = dataType match {
    case DataTypes.INT       => SqlInt()
    case DataTypes.BIGINT    => SqlLong()
    case DataTypes.FLOAT     => SqlFloat()
    case DataTypes.DOUBLE    => SqlDouble()
    case DataTypes.DECIMAL   => SqlDecimal()
    case DataTypes.BOOLEAN   => SqlBool()
    case DataTypes.TEXT      => SqlString()
    case DataTypes.TIMESTAMP => SqlTimestamp()
    case DataTypes.DATE      => SqlDate()
    case l: ListType         => convertCassandraType(l.getElementType, repeated = true)
  }

  /** Given a Cassandra Tuple, returns the Tuple ready to be used by SqlStruct */
  private def createTuple(name: String, t: DataType): (String, SqlType) = (name, convertCassandraType(t))

  /** Given a list of Cassandra tuples, returns a SqlStruct with all the fields inside */
  private def createSqlStruct(t: Iterable[(String, DataType)]): SqlStruct =
    SqlStruct(t.toList.map(v => createTuple(v._1, v._2)))

  /** Extension method. Enables myInstance.asSqlType syntax
    * @param value is a tuple from Cassandra
    */
  implicit class CassandraTupleSyntax(value: (String, DataType)) {
    def asSqlType: SqlType = SqlInstanceConversion[(String, DataType)].getType(value)
  }

  /** Extension method. Enables myInstance.asSqlType syntax when the instance is a Iterable
    * @param value is a tuple from Cassandra
    */
  implicit class CassandraListTupleSyntax(value: Iterable[(String, DataType)]) {
    def asSqlType: SqlType = SqlInstanceConversion[List[(String, DataType)]].getType(value.toList)
  }

  /** Extension method that enables a CreateTable object to be converted into SqlType, using a parser
    * Warning: There is no way to retrieve field names and types from a CreateTable, this is using a String parser
    * to extract them, so it is less safe and it could break on run time
    * @param table [[CreateTable]] instance
    */
  implicit class CassandraCreateTableSyntax(table: CreateTable) {

    def asSqlType: SqlType = {
      val fields = CreateTableParser.parse(table)
      SqlInstanceConversion[List[(String, DataType)]].getType(fields.toList)
    }
  }

}
