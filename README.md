# Big Data Types
[![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/ci-tests.yml)
[![BQ IT](https://github.com/data-tools/big-data-types/workflows/BigQuery-Integration/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/bigquery-integration.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.data-tools/big-data-types-core_2.13)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A library to transform Case Classes into Database schemas and to convert implemented types into another types

This is a type safe library that converts basic Scala types and product types into different database types and schemas using 
Shapeless for Scala 2 and Scala 3 Mirrors for Scala 3, 
it also allows converting implemented types into another types, for example, a Spark Schema can be automatically converted into a BigQuery table,
without having code that relates BigQuery and Spark directly.

You can also see [this page in web format](https://data-tools.github.io/big-data-types/)

What we can do with this library:
- Using multiple modules:
  - Probably the most powerful thing of the library, any implemented type can be converted to any other implemented type. e.g:
    A Spark Schema can be converted into a BigQuery Table
  - If new types are implemented in the library (e.g: Avro & Parquet schemas, Json Schema, ElasticSearch templates, etc)
    they will get automatically conversions for the rest of the types
- BigQuery: Create BigQuery Tables (or Schemas) using Case Classes or other types. 
  - BigQuery module has also a complete integration with the system, so tables can be created using only this library.
- Spark: Create Spark Schemas from Case Classes or from any other implemented type.
- Cassandra: Create `CreateTable` objects from Case Classes. (They can be printed as a `create table` statement too)
- Transformations:
    - On all modules, during a conversion (From Case Class to specific type) apply custom transformations. e.g: convert field names from camelCase into snake_case, decide Timestamp formats or numerical precisions


For now, it supports **BigQuery**, **Cassandra** and **Spark**.

Check also [how to create a new type](./docs/CreateNewType.md) for the library


# TL;DR:
Available conversions:

| From / To  |Scala Types       |BigQuery          |Spark             |Cassandra         |
|------------|:----------------:|:----------------:|:----------------:|:----------------:|
|Scala Types |       -          |:white_check_mark:|:white_check_mark:|:white_check_mark:|
|BigQuery    |                  |        -         |:white_check_mark:|:white_check_mark:|
|Spark       |                  |:white_check_mark:|        -         |:white_check_mark:|
|Cassandra   |                  |                  |                  |        -         |



- [Big Data Types](#big-data-types)
- [TL;DR:](#tl-dr-)
- [Quick Start](#quick-start)
- [How it works](#how-it-works)
- [BigQuery](#bigquery)
  * [Create BigQuery Tables](#create-bigquery-tables)
    + [Transform field names](#transform-field-names)
    + [Time Partitioned tables](#time-partitioned-tables)
    + [Create a table with more than one Case Class](#create-a-table-with-more-than-one-case-class)
  * [Create BigQuery schema from a Case Class](#create-bigquery-schema-from-a-case-class)
  * [From a Case Class instance](#from-a-case-class-instance)
  * [From any other type](#from-any-other-type)
  * [Connecting to your BigQuery environment](#connecting-to-your-bigquery-environment)
- [Spark](#spark)
  * [Spark Schema from Case Class](#spark-schema-from-case-class)
    + [Create a Dataframe](#create-a-dataframe)
    + [Spark Schema from Multiple Case Classes](#spark-schema-from-multiple-case-classes)
    + [Spark Schema from other types](#spark-schema-from-other-types)
  * [Field transformations](#field-transformations)
- [Cassandra](#cassandra)
  * [Case Classes to Cassandra CreateTable](#case-classes-to-cassandra-createtable)
  * [Other types to Cassandra CreateTable](#other-types-to-cassandra-createtable)
    + [Or from a product type (case class)](#or-from-a-product-type--case-class-)
    + [Or from an instance of other types](#or-from-an-instance-of-other-types)
- [Transformations](#transformations)
  * [Implicit Formats](#implicit-formats)
    + [DefaultFormats](#defaultformats)
    + [SnakifyFormats](#snakifyformats)
    + [Creating a custom Formats](#creating-a-custom-formats)
- [Multiple Modules](#multiple-modules)
  + [An example from Spark to Cassandra](#an-example-from-spark-to-cassandra)

# Quick Start
The library has different modules that can be imported separately
- BigQuery
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-bigquery" % "{version}"
```
- Spark
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-spark" % "{version}"
```
- Cassandra
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-cassandra" % "{version}"
```
- Core
    - To get support for abstract SqlTypes, it is included in the others, so it is not needed if you are using one of the others
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-core" % "{version}"
```

Versions for Scala ![Scala 2.12](https://img.shields.io/badge/Scala-2.12-red) ,![Scala_2.13](https://img.shields.io/badge/Scala-2.13-red) 
and ![Scala 3.0](https://img.shields.io/badge/Scala-3.0-red) are available in Maven

# How it works
Check the [complete guide on how to create a new type](./docs/CreateNewType.md) to understand how the library works internally
 
# BigQuery

## Create BigQuery Tables

```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

case class MyTable(field1: Int, field2: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
```
This also works with Structs, Lists and Options.
See more examples in [Tests](https://github.com/data-tools/big-data-types/blob/99b48ca00420f30ade37faa0ed03f5b464aa9e5e/bigquery/src/it/scala/org/datatools/bigdatatypes/bigquery/BigQueryTableSpec.scala)

### Transform field names
There is a `Format` object that allows us to decide how to transform field names, for example, changing CamelCase for snake case
```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats

case class MyTable(myIntField: Int, myStringField: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
//This table will have my_int_field and my_string_field fields
```

### Time Partitioned tables
Using a `Timestamp` or `Date` field, tables can be partitioned in BigQuery using a [Time Partition Column](https://cloud.google.com/bigquery/docs/creating-column-partitions)
```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats

case class MyTable(field1: Int, field2: String, myPartitionField: java.sql.Timestamp)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name", "my_partition_field")
```
### Create a table with more than one Case Class
In many cases we work with a Case Class that represents our data but we also want to add 
some metadata fields like `updated_at`, `received_at`, `version` and so on.
In these cases we can work with multiple Case Classes and fields will be concatenated:

```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

case class MyData(field1: Int, field2: String)
case class MyMetadata(updatedAt: Long, version: Int)
BigQueryTable.createTable[MyData, MyMetadata]("dataset_name", "table_name")
```
This can be done up to 5 concatenated classes


## Create BigQuery schema from a Case Class
```scala
import com.google.cloud.bigquery.{Field, Schema}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.bigquery.BigQueryTypes

case class MyTable(field1: Int, field2: String)
//List of BigQuery Fields, it can be used to construct an Schema
val fields: List[Field] = BigQueryTypes[MyTable].bigQueryFields
//BigQuery Schema, it can be used to create a table
val schema: Schema = Schema.of(fields.asJava)
```

## From a Case Class instance
```scala
import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.bigquery.BigQueryTypes._

case class MyTable(field1: Int, field2: String)
val data = MyTable(1, "test")
val fields: List[Field] = data.asBigQuery
```

## From any other type
e.g: Spark Schema
```scala
val myDataframe: Dataframe = ???
val bqSchema: Schema = myDataframe.schema.asBigQuery
```

See more info about [creating tables on BigQuery](https://cloud.google.com/bigquery/docs/tables#java) in the official documentation

## Connecting to your BigQuery environment
If you want to create tables using the library you will need to connect to your BigQuery environment 
through any of the GCloud options. 
Probably the most common will be to specify a service account and a project id.
It can be added on environment variables. The library expects:
- PROJECT_ID: <your_project_id>
- GOOGLE_APPLICATION_CREDENTIALS: <path_to_your_service_account_json_file>

---

# Spark

## Spark Schema from Case Class

With Spark module, Spark Schemas can be created from Case Classes.
```scala
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
//an implicit Formats class is needed, defaultFormats does no transformations
//it can be created as implicit val instead of using this import
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

case class MyModel(myInt: Integer, myString: String)
val schema: StructType = SparkSchemas.schema[MyModel]
```
It works for Options, Sequences and any level of nested objects

Also, a Spark Schema can be extracted from a Case Class instance
```scala
val model = MyModel(1, "test")
model.asSparkSchema
```

### Create a Dataframe
```scala
case class Dummy(myInt: Int, myString: String)

implicit val default: Formats = DefaultFormats
val schema = SparkSchemas.schema[Dummy]
val df = spark.read.schema(schema).json("dummy.json")
df.show(4)
/*
+-----+--------+
|myInt|myString|
+-----+--------+
|    1|    test|
|    2|   test2|
|    3|   test3|
|    4|   test4|
+-----+--------+
*/
```

### Spark Schema from Multiple Case Classes
Also, an schema can be created from multiple case classes. 
As an example, it could be useful for those cases where we read data using a Case Class, 
and we want to append some metadata fields, but we don't want to create another Case Class with exactly the same fields plus a few more.
```scala
import java.sql.Timestamp
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
 
case class MyModel(myInt: Integer, myString: String)
case class MyMetadata(updatedAt: Timestamp, version: Int)
val schema: StructType = SparkSchemas.schema[MyModel, MyMetadata]
/*
schema =
 List(
    StructField(myInt, IntegerType, false), 
    StructField(myString, StringType, false)
    StructField(updatedAt, TimestampType, false)
    StructField(version, IntegerType, false)
   )
*/
```

Another example, creating a Dataframe

```scala
case class Dummy(myInt: Int, myString: String)
case class Append(myTimestamp: Timestamp)

implicit val default: Formats = DefaultFormats
val schema = SparkSchemas.schema[Dummy, Append]
val df = spark.read.schema(schema).json("my_file.json")
df.show(4)
/*
+------+---------+-------------------+
|my_int|my_string|       my_timestamp|
+------+---------+-------------------+
|     1|     test|2021-01-24 10:07:39|
|     2|    test2|2021-01-24 10:07:39|
|     3|    test3|2021-01-24 10:07:39|
|     4|    test4|2021-01-24 10:07:39|
+------+---------+-------------------+
*/
```

### Spark Schema from other types

```scala
val myBigQuerySchema: Schema = ???
val schema: StructType = myBigQuerySchema.asSparkSchema
```


## Field transformations
Also, custom transformations can be applied to field names, something that usually is quite hard to do with Spark Datasets.
For example, working with CamelCase Case Classes but using snake_case field names in Spark Schema.

```scala
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
//implicit formats for transform keys to snake_case
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats

case class MyModel(myInt: Integer, myString: String)
val schema: StructType = SparkSchemas.schema[MyModel]
/*
schema =
 List(
    StructField(my_int, IntegerType, false), 
    StructField(my_string, StringType, false)
   )
*/
```

---

# Cassandra
Cassandra module is using the [Query builder in the DataStax Java Driver](https://github.com/datastax/java-driver/tree/4.x/manual/query_builder/schema) 
to return `CreateTable` objects from product types such a Case Classes (or other types from the library, like Spark Schemas)
This module doesn't have anything to connect directly to a Cassandra instance, 
this is only a bridge between Scala product types (and other types from the library) to the DataStax Query Builder

In other words, with this module you can use Case Classes to create a `CreateTable` 
object that you can use to interact with a real Cassandra instance

## Case Classes to Cassandra CreateTable
Quick example using Case Classes
```scala
case class MyTable(id: String, name: String, age: Int)
val table: CreateTable = CassandraTables.table[MyTable]("TableName", "id")
```

## Other types to Cassandra CreateTable
```scala
val sparkSchema = sparkDataFrame.schema
CassandraTables.table(sparkSchema, "tableName", "primaryKeyFieldName")
```

### Or from a product type (case class)
```scala
case class MyModel(a: Int, b: String)
val instance = MyModel(1, "test")
instance.asCassandra("TestTable", "primaryKey")
```

### Or from an instance of other types
```scala
val mySparkDataframe: Dataframe = ???
mySparkDataframe.asCassandra("TestTable", "primaryKey")
```

# Transformations
Transformations can be applied easily during conversions. For example, field names can be modified.

## Implicit Formats
Formats can handle different configurations that we want to apply to schemas, like transforming field names, 
defining precision for numeric types and so on.

They can be used by creating an implicit val with a Formats class or by importing one of the available implicit vals in `Formats` object 

### DefaultFormats
`DefaultFormats` is a trait that applies no transformation to field names
To use it, you can create an implicit val:
```scala
import org.datatools.bigdatatypes.formats.{Formats, DefaultFormats}
implicit val formats: Formats = DefaultFormats
```
or just import the one available:
```scala
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
```


### SnakifyFormats
`SnakifyFormats` is a trait that converts camelCase field names to snake_case names
To use it, you can create an implicit val:
```scala
import org.datatools.bigdatatypes.formats.{Formats, SnakifyFormats}
implicit val formats: Formats = SnakifyFormats
```
or just import the one available:
```scala
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats
```

### Creating a custom Formats
Formats can be extended, so if we want to transform keys differently, for example adding a suffix to all of our fields
```scala
import org.datatools.bigdatatypes.formats.Formats
trait SuffixFormats extends Formats {
  override transformKey[A <: SqlType](name: String, t: A): String = key + "_at"
}
object SuffixFormats extends SuffixFormats
```
All your field names will have "_at" at the end


# Multiple Modules
Importing more than one module can be a powerful tool as they have compatible transformations between them.

For example, importing BigQuery and Spark will allow a conversion between them, making possible to create BigQuery Tables using Spark Schemas
As an example:

```scala
//DataFrames
val mySparkDataFrame: DataFrame = ???
BigQueryTable.createTable(mySparkDataFrame, "dataset_name", "table_name")

//Datasets
val mySparkDataset: Dataset[A] = ???
BigQueryTable.createTable(mySparkDataset, "dataset_name", "table_name")
```
Or we can just get the BigQuery Schema
```scala
val mySparkDataFrame: DataFrame = ???
val bq: List[Field] = mySparkDataFrame.schema.asBigQuery
```

### An example from Spark to Cassandra
```scala
val mySparkDataFrame: DataFrame = ???
val cassandraTable: CreateTable = mySparkDataFrame.schema.asCassandra("TableName", "primaryKey")
```
More examples can be found in [Tests of the Examples Module](https://github.com/data-tools/big-data-types/blob/44255f99d32293f83eee333760fc31c9bd0f0d02/examples/src/test/scala_2.13-/bigdatatypes/CrossModuleExamplesSpec.scala)

