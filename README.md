# Big Data Types
![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg
)
[![codecov](https://codecov.io/gh/data-tools/big-data-types/branch/main/graph/badge.svg)](https://codecov.io/gh/data-tools/big-data-types)



A library to transform Case Classes into Database schemas

This library converts basic Scala types and Case Classes into different database types and schemas using Shapeless, 
making possible to extract a database schema from a Case Class and to work with Case Classes when writing,
reading or creating tables in different databases. 

For now, it only supports BigQuery.
 
## BigQuery

### Create BigQuery Tables

```scala
case class MyTable(field1: Int, field2: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
```
This also works with Structs, Lists and Options.
See more examples in [Tests](https://github.com/data-tools/big-data-types/blob/main/src/it/scala/org/datatools/bigdatatypes/bigquery/BigQueryTableSpec.scala)

#### Time Partitioned tables
Using a `Timestamp` field, tables can be partitioned in BigQuery using a [Time Partition Column](https://cloud.google.com/bigquery/docs/creating-column-partitions)
```scala
case class MyTable(field1: Int, field2: String, partitionField: java.sql.Timestamp)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name", "partition_field")
```
#### Create a table with more than one Case Class
In many cases we work with a Case Class that represents our data but we also want to add 
some metadata fields like `updated_at`, `received_at`, `version` and so on.
In these cases we can work with multiple Case Classes and fields will be concatenated:

```scala
case class MyData(field1: Int, field2: String)
case class MyMetadata(updatedAt: Long, version: Int)
BigQueryTable.createTable[MyData, MyMetadata]("dataset_name", "table_name")
```
This can be done up to 5 concatenated classes


### Create a schema for a BigQuery table
```scala
case class MyTable(field1: Int, field2: String)
//List of BigQuery Fields, it can be used to construct an Schema
val fields: List[Field] = BigQueryTypes[MyTable].getBigQueryFields
//BigQuery Schema, it can be used to create a table
val schema: Schema = Schema.of(fields)
```
See more info about [creating tables on BigQuery](https://cloud.google.com/bigquery/docs/tables#java) in the official documentation

### Connecting to your BigQuery environment
If you want to create tables using the library you will need to specify a service account and a project id.
It can be added on environment variables. The library expects:
- PROJECT_ID: <your_project_id>
- GOOGLE_APPLICATION_CREDENTIAL: <path_to_your_service_account_json_file>

