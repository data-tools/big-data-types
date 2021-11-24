---
sidebar_position: 3
---
# BigQuery

The BigQuery module is more complete than others, it allows the common transformation of the library, 
which is the transformation from other types into BigQuery, and BigQuery types into others.

This module also includes a complete integration with a BigQuery environment, meaning that if BigQuery credentials 
are given, **the library include methods to create tables in BigQuery**, directly using other types.


## Create BigQuery Tables

```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

case class MyTable(field1: Int, field2: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
```
This also works with Structs, Lists and Options.
See more examples in [Tests](https://github.com/data-tools/big-data-types/blob/main/bigquery/src/it/scala/org/datatools/bigdatatypes/bigquery/BigQueryTableSpec.scala)

### Transform field names
There is a `Format` object that allows us to decide how to transform field names, for example, changing CamelCase for snake case
```scala
import org.datatools.bigdatatypes.bigquery.BigQueryTable
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats

case class MyTable(myIntField: Int, myStringField: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
//This table will have my_int_field and my_string_field fields
```

:::tip
Tables can be created using directly an instance of any other type of the library. Example from Spark:
```scala
val df: Dataframe = ???
val schema: StructType = df.schema
BigQueryTable.createTable[StructType](schema, "dataset_name", "table_name")
```
:::


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
Or use the provided extension method for the creation of the Schema
```scala
val fields: List[Field] = BigQueryTypes[MyTable].bigQueryFields
val schema: Schema = fields.schema
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
val bqFields: List[Field] = myDataframe.schema.asBigQuery
val bqSchema: Schema = myDataframe.schema.asBigQuery.schema
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
