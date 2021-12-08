---
sidebar_position: 2
---
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

In order to transform one type into another, both modules have to be imported.

## How it works

The library internally uses a generic ADT ([SqlType](https://github.com/data-tools/big-data-types/blob/main/core/src/main/scala_3/org/datatools/bigdatatypes/basictypes/SqlType.scala))
that can store any schema representation, and from there, it can be converted into any other. 
Transformations are done through 2 different type-classes.

### Quick examples
Case Classes to other types
```scala
//Spark
val s: StructType = SparkSchemas.schema[MyCaseClass]
//BigQuery
val bq: List[Field] = SqlTypeToBigQuery[MyCaseClass].bigQueryFields // just the schema
BigQueryTable.createTable[MyCaseClass]("myDataset", "myTable") // Create a table in a BigQuery real environment
//Cassandra
val c: CreateTable = CassandraTables.table[MyCaseClass]
```

There are also `extension methods` that make easier the transformation between types when there are instances
```scala
//from Case Class instance
val foo: MyCaseClass = ???
foo.asBigQuery // List[Field]
foo.asSparkSchema // StructType
foo.asCassandra("TableName", "primaryKey") // CreateTable
```

**Conversion between types** works in the same way
```scala
// From Spark to others
val foo: StructType = myDataFrame.schema
foo.asBigQuery // List[Field]
foo.asCassandra("TableName", "primaryKey") // CreateTable

//From BigQuery to others
val foo: Schema = ???
foo.asSparkFields // List[StructField]
foo.asSparkSchema // StructType
foo.asCassandra("TableName", "primaryKey") // CreateTable

//From Cassandra to others
val foo: CreateTable = ???
foo.asSparkFields // List[StructField]
foo.asSparkSchema // StructType
foo.asBigQuery // List[Field]
foo.asBigQuery.schema // Schema
```

Check the [complete guide on how to create a new type](./Contributing/CreateNewType) to understand how the library works internally
 