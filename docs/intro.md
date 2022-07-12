---
sidebar_position: 1
---

# Big Data Types
[![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/ci-tests.yml)
[![BQ IT](https://github.com/data-tools/big-data-types/workflows/BigQuery-Integration/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/bigquery-integration.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.data-tools/big-data-types-core_2.13)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A library to transform Case Classes into Database schemas and to convert implemented types into another types

This is a type safe library that converts basic Scala types and product types into different database types and schemas,
it also allows converting implemented types into another types, for example, a Spark Schema can be automatically converted into a BigQuery table, 
or a BigQuery table into a Cassandra table without having code that relates those types between them.


### What we can do with this library:
- Using multiple modules:
    - Probably the most powerful thing of the library, any implemented type can be converted to any other implemented type. e.g:
      A **Spark Schema** can be converted into a **BigQuery Table**
    - If new types are implemented in the library (e.g: Avro & Parquet schemas, Json Schema, ElasticSearch templates, etc)
      they will get automatically conversions for the rest of the types
- BigQuery: Create BigQuery Tables (or Schemas) using Case Classes or other types.
    - BigQuery module has also a complete integration with the system, so tables can be created using only this library.
- Spark: Create Spark Schemas from Case Classes or from any other implemented type.
- Cassandra: Create `CreateTable` objects from Case Classes. (They can be printed as a `create table` statement too) or from any other type
- Transformations:
    - On all modules, during a conversion (from one type to another) apply custom transformations. 
  e.g: convert field names from camelCase into snake_case, decide Timestamp formats or numerical precisions
    


### Available conversions:

|    From / To    |                                                                                                                      |Scala Types       |BigQuery          |Spark             |Cassandra         | Circe (JSON) |
|:---------------:|:--------------------------------------------------------------------------------------------------------------------:|:----------------:|:----------------:|:----------------:|:----------------:|:------------:|
|      Scala      |               <img src="/img/logos/scala.png" style="max-height:50px;max-width:70px">               |       -          |:white_check_mark:|:white_check_mark:|:white_check_mark:|              |
|    BigQuery     |             <img src="/img/logos/bigquery.png" style="max-height:50px;max-width:70px">              |                  |        -         |:white_check_mark:|:white_check_mark:|              |
|      Spark      |  <img src="/img/logos/spark.png" style="background-color:white;max-height:100px;max-width:100px">   |                  |:white_check_mark:|        -         |:white_check_mark:|              |
|    Cassandra    | <img src="/img/logos/cassandra.png" style="background-color:white;max-height:50px;max-width:100px"> |                  |:white_check_mark:|:white_check_mark:|        -         |              |
|  Circe (JSON)   |    <img src="/img/logos/circe.png" style="background-color:gray;max-height:50px;max-width:70px">    |                  |:white_check_mark:|:white_check_mark:|:white_check_mark:|              |


