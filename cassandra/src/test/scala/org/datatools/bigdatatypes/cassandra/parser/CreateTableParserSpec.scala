package org.datatools.bigdatatypes.cassandra.parser

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.cassandra.CassandraTables
import org.datatools.bigdatatypes.cassandra.parser.CreateTableParser.NameType
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

import java.sql.Timestamp
import java.sql.Date

class CreateTableParserSpec extends UnitSpec {

  behavior of "CreateTableParserSpec"

  case class Dummy(id: String, version: Int)
  val t: CreateTable = CassandraTables.table[Dummy]("name", "id")

  "Parser" should "parse CreateTable objects" in {
    CreateTableParser.parse(t) shouldBe List(("id", DataTypes.TEXT), ("version", DataTypes.INT))
  }

  it should "fail on not implemented types" in {
    case class Dummy(id: String, version: Timestamp, foo: Date)
    val t: CreateTable = CassandraTables.table[Dummy]("name", "id")
    assertThrows[UnsupportedOperationException](CreateTableParser.parse(t))
  }

  "Fields and types" should "be extracted from CreateTable" in {
    val table = "CREATE TABLE name (id text PRIMARY KEY,version int)"
    CreateTableParser.extractComponents(table) shouldBe Right(List(NameType("id", "text"), NameType("version", "int")))
  }

  it should "be extracted from CreateTable ignoring spaces" in {
    val table = "CREATE TABLE name (id text PRIMARY KEY , version int)"
    CreateTableParser.extractComponents(table) shouldBe Right(List(NameType("id", "text"), NameType("version", "int")))
  }

  it should "fail on missing fields" in {
    val t = "CREATE TABLE name ()"
    CreateTableParser.extractComponents(t) shouldBe Left(ParsingError.ErrorParsingTable("Error parsing CreateTable"))
  }
}
