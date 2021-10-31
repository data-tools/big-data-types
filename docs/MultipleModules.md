---
sidebar_position: 7
---
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
