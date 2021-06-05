package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.{BasicOptionTypes, BasicTypes}
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}


class SqlTypeToCassandraSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  behavior of "SparkTypesSpec for all TestTypes"

  "Basic types" should "create Cassandra tuples" in {
    SqlTypeToCassandra[BasicTypes].cassandraFields shouldBe CassandraTestTypes.basicFields
  }


  "Basic Optional types" should "create an Spark Schema" in {
    SqlTypeToCassandra[BasicOptionTypes].cassandraFields shouldBe CassandraTestTypes.basicOptionTypes
  }
/*
  "A List field" should "be converted into Spark Array type" in {
    SparkSchemas.fields[BasicList] shouldBe SparkTestTypes.basicWithList
  }

  "Nested field" should "be converted into Spark Nested field" in {
    SparkSchemas.fields[BasicStruct] shouldBe SparkTestTypes.basicNested
  }

  "Optional Nested field" should "be converted into nullable Spark Nested field" in {
    SparkSchemas.fields[BasicOptionalStruct] shouldBe SparkTestTypes.basicOptionalNested
  }

  "List of nested objects (matrix)" should "be converted into Spark Nested Array" in {
    SparkSchemas.fields[ListOfStruct] shouldBe SparkTestTypes.basicNestedWithList
  }

  "Extended types" should "create an Spark Schema" in {
    SparkSchemas.fields[ExtendedTypes] shouldBe SparkTestTypes.extendedTypes
  }
*/
}
