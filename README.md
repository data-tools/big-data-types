# Big Data Types
A library to transform Case Classes into Database types.

This library converts case classes into different database types, making possible to work with case classes when writing,
 reading or creating tables in different databases. For now, it only supports BigQuery.
 
## BigQuery

### Create BigQuery Tables

```
case class MyTable(field1: Int, field2: String)
BigQueryTable.createTable[MyTable]("dataset_name", "table_name")
```
This also works with Structs, Lists and Options.


### Create a schema for a BigQuery table
```
case class MyTable(field1: Int, field2: String)
//List of BigQuery Fields, it can be used to construct an Schema
val fields: List[Field] = BigQueryTypes[MyTable].getBigQueryFields
//BigQuery Schema, it can be used to create a table
val schema: Schema = Schema.of(fields)
```
See more info on [creating tables on BigQuery](https://cloud.google.com/bigquery/docs/tables#java) in the official documentation

### Connecting to your BigQuery environment
If you want to create tables using the library you will need to specify a service account and a project id.
It can be added on environment variables. The library expects:
- PROJECT_ID: <your_project_id>
- GOOGLE_APPLICATION_CREDENTIAL: <path_to_your_service_account_json_file>

