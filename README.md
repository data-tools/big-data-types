# Big Data Types
![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg)
[![codecov](https://codecov.io/gh/data-tools/big-data-types/branch/main/graph/badge.svg)](https://codecov.io/gh/data-tools/big-data-types)
![Maven Central](https://img.shields.io/maven-central/v/io.github.data-tools/big-data-types_2.13)

A library to transform Case Classes into Database schemas

This is a type safe library that converts basic Scala types and Case Classes into different database types and schemas using Shapeless, 
making possible to extract a database schema from a Case Class and to work with Case Classes when writing,
reading or creating tables in different databases. 

For now, it supports BigQuery and Spark.

# Quick Start
```
libraryDependencies += "io.github.data-tools" % "big-data-types_2.13" % "0.1.0"
```
Versions for Scala 2.12 and 2.13 are available in Maven
 
## BigQuery

### Create BigQuery Tables

```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats

case class MyTable(field1: Int, field2: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
```
This also works with Structs, Lists and Options.
See more examples in [Tests](https://github.com/data-tools/big-data-types/blob/main/src/it/scala/org/datatools/bigdatatypes/bigquery/BigQueryTableSpec.scala)

#### Transform field names
There is a `Format` object that allows us to decide how to transform field names, for example, changing CamelCase for snake case
```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.TransformKeys.snakifyFields

case class MyTable(myIntField: Int, myStringField: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
//This table will have my_int_field and my_string_field fields
```

#### Time Partitioned tables
Using a `Timestamp` or `Date` field, tables can be partitioned in BigQuery using a [Time Partition Column](https://cloud.google.com/bigquery/docs/creating-column-partitions)
```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.TransformKeys.snakifyFields

case class MyTable(field1: Int, field2: String, myPartitionField: java.sql.Timestamp)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name", "my_partition_field")
```
#### Create a table with more than one Case Class
In many cases we work with a Case Class that represents our data but we also want to add 
some metadata fields like `updated_at`, `received_at`, `version` and so on.
In these cases we can work with multiple Case Classes and fields will be concatenated:

```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats

case class MyData(field1: Int, field2: String)
case class MyMetadata(updatedAt: Long, version: Int)
BigQueryTable.createTable[MyData, MyMetadata]("dataset_name", "table_name")
```
This can be done up to 5 concatenated classes


### Create BigQuery schema from a Case Class
```scala
import com.google.cloud.bigquery.{Field, Schema}
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats
import org.datatools.bigdatatypes.bigquery.BigQueryTypes

case class MyTable(field1: Int, field2: String)
//List of BigQuery Fields, it can be used to construct an Schema
val fields: List[Field] = BigQueryTypes[MyTable].bigQueryFields
//BigQuery Schema, it can be used to create a table
val schema: Schema = Schema.of(fields.asJava)
```

### From a Case Class instance
```scala
import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats
import org.datatools.bigdatatypes.bigquery.BigQueryTypes._

case class MyTable(field1: Int, field2: String)
val data = MyTable(1, "test")
val fields: List[Field] = data.getBigQueryFields
```

See more info about [creating tables on BigQuery](https://cloud.google.com/bigquery/docs/tables#java) in the official documentation

### Connecting to your BigQuery environment
If you want to create tables using the library you will need to connect to your BigQuery environment 
through any of the GCloud options. 
Probably the most common will be to specify a service account and a project id.
It can be added on environment variables. The library expects:
- PROJECT_ID: <your_project_id>
- GOOGLE_APPLICATION_CREDENTIAL: <path_to_your_service_account_json_file>

---

## Spark

### Spark Schema from Case Class

With Spark module, Spark Schemas can be created from Case Classes.
```scala
import org.apache.spark.sql.types.StructField
import org.datatools.bigdatatypes.spark.SparkTypes._
//an implicit Formats class is needed, defaultFormats does no transformations
//it can be created as implicit val instead of using this import
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats

case class MyModel(myInt: Integer, myString: String)
val schema: StructField = SparkTypes[MyModel].sparkSchema
```
It works for Options, Sequences and any level of nested objects

Also, a Spark Schema can be extracted from a Case Class instance
```scala
val model = MyModel(1, "test")
model.sparkSchema
```
### Field transformations
Also, custom transformations can be applied to field names, something that usually is quite hard to do with Spark Datasets.
For example, working with CamelCase Case Classes but using snake_case field names in Spark Schema.

```scala
import org.apache.spark.sql.types.StructField
import org.datatools.bigdatatypes.spark.SparkTypes._
//implicit formats for transform keys to snake_case
import org.datatools.bigdatatypes.formats.TransformKeys.snakifyFields

case class MyModel(myInt: Integer, myString: String)
val schema: StructField = SparkTypes[MyModel].sparkSchema
//schema will have "my_int" and "my_string" fields
```
