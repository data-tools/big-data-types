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
val fields: List[Field] = BigQueryTypes[MyTable].getBigQueryFields
```
### Create a Table


