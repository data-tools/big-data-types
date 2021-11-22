# Big Data Types
[![CI Tests](https://github.com/data-tools/big-data-types/workflows/ci-tests/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/ci-tests.yml)
[![BQ IT](https://github.com/data-tools/big-data-types/workflows/BigQuery-Integration/badge.svg)](https://github.com/data-tools/big-data-types/actions/workflows/bigquery-integration.yml)
![Maven Central](https://img.shields.io/maven-central/v/io.github.data-tools/big-data-types-core_2.13)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A type-safe library to transform Case Classes into Database schemas and to convert implemented types into another types


# Documentation
Check the [Documentation website](https://data-tools.github.io/big-data-types) to learn more about how to use this library
  

# Available conversions:

| From / To  |Scala Types       |BigQuery          |Spark             |Cassandra         |
|------------|:----------------:|:----------------:|:----------------:|:----------------:|
|Scala Types |       -          |:white_check_mark:|:white_check_mark:|:white_check_mark:|
|BigQuery    |                  |        -         |:white_check_mark:|:white_check_mark:|
|Spark       |                  |:white_check_mark:|        -         |:white_check_mark:|
|Cassandra   |                  |:white_check_mark:|:white_check_mark:|        -         |


Versions for Scala ![Scala 2.12](https://img.shields.io/badge/Scala-2.12-red) ,![Scala_2.13](https://img.shields.io/badge/Scala-2.13-red) 
and ![Scala 3.0](https://img.shields.io/badge/Scala-3.0-red) are available in Maven
