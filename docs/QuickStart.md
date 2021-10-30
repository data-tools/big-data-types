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

## How it works
Check the [complete guide on how to create a new type](./docs/CreateNewType.md) to understand how the library works internally
 