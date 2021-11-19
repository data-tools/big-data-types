package org.datatools.bigdatatypes.cassandra.parser

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.cassandra.parser.ParsingError.{compactErrors, ErrorParsingField, ParsingErrors}

private[cassandra] object CreateTableParser extends App {

  val text =
    "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"

  val regex = """\((.*)\)""".r
  val pk = " PRIMARY KEY"

  case class NameType(name: String, t: String)

  /** Given a [[CreateTable]], uses it's representation of String to extract their fields and types
    * @param table a [[CreateTable]] instance
    * @return Either a [[ParsingError]] or a list of [[NameType]] with names and types in String
    */
  def extractComponents(table: CreateTable): Either[ParsingError, List[NameType]] =
    regex
      .findFirstIn(table.toString)
      .map(s => s.substring(1).substring(0, s.length - 1).replace(pk, ""))
      .map(s => s.split(','))
      .map(l =>
        l.map { s =>
          val both = s.split(' ')
          NameType(both.head, both.tail.head)
        }.toList
      ) match {
      case Some(value) => Right(value)
      case None        => Left(ParsingError.ErrorParsingTable("Error parsing CreateTable"))
    }

  /** Given a list of parsed fields, return ParsingErrors or the fields parsed and typed for Cassandra
    * @param fields is a list of Fields parsed
    * @return Either with [[ParsingErrors]] or a list of fields with their types
    */
  def toCassandraTypes(fields: List[NameType]): Either[ParsingError, Seq[(String, DataType)]] = {
    val typed: Seq[Either[ParsingError, (String, DataType)]] = fields.map(field => parsedToCassandra(field))

    val err = typed.filter(_.isLeft).map { case Left(value) => value }

    if (err.nonEmpty) {
      Left(compactErrors(err))
    }
    else {
      val values = typed.map { case Right(value) =>
        value
      }
      Right(values)
    }
  }

  /** Given a field parsed and it's type, return a valid Cassandra field name and type, or a Parsing error
    * @param field a field with name and type
    * @return Either a parsed type or a [[ParsingError]]
    */
  def parsedToCassandra(field: NameType): Either[ParsingError, (String, DataType)] =
    field.t.toLowerCase match {
      case "int"     => Right((field.name, DataTypes.INT))
      case "bigint"  => Right((field.name, DataTypes.BIGINT))
      case "float"   => Right((field.name, DataTypes.FLOAT))
      case "double"  => Right((field.name, DataTypes.DOUBLE))
      case "decimal" => Right((field.name, DataTypes.DECIMAL))
      case "boolean" => Right((field.name, DataTypes.BOOLEAN))
      case "text"    => Right((field.name, DataTypes.TEXT))
      case _         => Left(ErrorParsingField(field.name, s"Error parsing field: Type ${field.t} not implemented"))
    }

}
