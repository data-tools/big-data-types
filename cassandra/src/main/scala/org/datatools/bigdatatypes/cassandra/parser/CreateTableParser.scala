package org.datatools.bigdatatypes.cassandra.parser

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.cassandra.parser.ParsingError.{ErrorParsingField, ParsingErrors, compactErrors}

import scala.util.matching.Regex

private[cassandra] object CreateTableParser {

  val fieldsRegex: Regex = """\((.*)\)""".r
  val pk = " PRIMARY KEY"

  case class NameType(name: String, t: String)

  /** Try to parse a [[CreateTable]] object. It will throw an exception if the parsing fails
    * @param table [[CreateTable]]
    * @return A list of tuples with field name and type for Cassandra.
    */
  def parse(table: CreateTable): Seq[(String, DataType)] = {
    val fields = for {
      components <- extractComponents(table)
      fields <- toCassandraTypes(components)
    } yield fields
    fields match {
      case Left(value)  => throw new UnsupportedOperationException(value.msg)
      case Right(value) => value
    }
  }

  /** Given a [[CreateTable]], uses it's representation of String to extract their fields and types
    * @param table a [[CreateTable]] instance
    * @return Either a [[ParsingError]] or a list of [[NameType]] with names and types in String
    */
  def extractComponents(table: CreateTable): Either[ParsingError, List[NameType]] = {
    fieldsRegex
      .findFirstIn(table.toString)
      .map(s => s.substring(1, s.length - 1).replace(pk, ""))
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
      case _         => Left(ErrorParsingField(field.name, s"type ${field.t} not implemented"))
    }

}
