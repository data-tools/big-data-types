# Big Data Types
[![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/ci-tests.yml)
[![BQ IT](https://github.com/data-tools/big-data-types/workflows/BigQuery-Integration/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/bigquery-integration.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.data-tools/big-data-types-core_2.13)
[![codecov](https://codecov.io/gh/data-tools/big-data-types/branch/main/graph/badge.svg?token=1DUBMIAEO8)](https://codecov.io/gh/data-tools/big-data-types)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A type-safe library to transform Case Classes into Database schemas and to convert implemented types into another types


# Documentation
Check the [Documentation website](https://data-tools.github.io/big-data-types) to learn more about how to use this library
  

# Available conversions:

|  From / To   |                                                                                                                       | Scala Types |      BigQuery      |       Spark        |     Cassandra      | Circe (JSON) |
|:------------:|:---------------------------------------------------------------------------------------------------------------------:|:-----------:|:------------------:|:------------------:|:------------------:|:------------:|
|    Scala     |               <img src="./website/static/img/logos/scala.png" style="max-height:50px;max-width:70px" />               |      -      | :white_check_mark: | :white_check_mark: | :white_check_mark: |              |
|   BigQuery   |             <img src="./website/static/img/logos/bigquery.png" style="max-height:50px;max-width:70px" />              |             |         -          | :white_check_mark: | :white_check_mark: |              |
|    Spark     |  <img src="./website/static/img/logos/spark.png" style="background-color:white;max-height:100px;max-width:100px" />   |             | :white_check_mark: |         -          | :white_check_mark: |              |
|  Cassandra   | <img src="./website/static/img/logos/cassandra.png" style="background-color:white;max-height:50px;max-width:100px" /> |             | :white_check_mark: | :white_check_mark: |         -          |              |
| Circe (JSON) |    <img src="./website/static/img/logos/circe.png" style="background-color:gray;max-height:50px;max-width:70px" />    |             | :white_check_mark: | :white_check_mark: | :white_check_mark: |              |


Versions for Scala ![Scala 2.12](https://img.shields.io/badge/Scala-2.12-red) ,![Scala_2.13](https://img.shields.io/badge/Scala-2.13-red) 
and ![Scala 3.x](https://img.shields.io/badge/Scala-3.x-red) are available in Maven


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
- Circe (JSON)
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-circe" % "{version}"
```
- Core
    - To get support for abstract SqlTypes, it is included in the others, so it is not needed if you are using one of the others
```
libraryDependencies += "io.github.data-tools" %% "big-data-types-core" % "{version}"
```

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
