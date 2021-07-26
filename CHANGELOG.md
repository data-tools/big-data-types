### Big Data Types v0.5.1
- Spark: Added methods to convert Instances into Spark inside `SparkSchemas`.
- Spark: New extension methods that will allow any other instance to converted into Spark types
- BigQuery: Fixed some names in extension methods
- New cross module examples in Examples module

### Big Data Types v0.5.0
- BigQuery: Reverse conversion. 
  This allows any BigQuery object (Schema or Field) to be converted into any of the other implemented types
- Upgrade to Scala 3.0.1

### Big Data Types v0.4.0
- Cassandra module added
- More cross examples added

### Big Data Types v0.3.5
- Scala 3.0.0

### Big Data Types v0.3.4
- Scala 3.0.0-RC3

### Big Data Types v0.3.3
- BigQuery module compiled for Scala 3
  -- Some imports refactored in Core Scala2 to be equal to Scala3 (related to enums)
  
### Big Data Types v0.3.2
- Core module migrated to Scala 3
-- Build changed for Scala 3 versions and Shapeless removed

### Big Data Types v0.3.1

- BigDecimal precision configurable via implicit Formats
- Changed Key transformation in `Formats`, now key transformations can be based on types

### Big Data Types v0.3.0

- Spark: Added reverse conversion (From Spark schemas to generic SqlTypes)
- Added SqlDouble as a new type in `Core` and `Spark`. (BigQuery does not have Doubles)
- Added a new Type Class for conversions using instances in `Core` and `BigQuery`
- BigQuery: Now it accepts Spark schemas or anything implementing SqlTypeConversion as input
- New Examples module for cross types tests


### Big Data Types v0.2.1

- Project split into multiple projects
    - core
    - bigquery
    - spark

### Big Data Types v0.1.1

- Spark: Added SparkSchemas as a public interface for conversions.
- Spark: SparkSchemas accepts multiple case classes when generating schemas
- Core: Formats refactored, variables and class names have changed

### Big Data Types v0.1.0

- Spark: Added support for Spark Schemas (only Spark 2.12)

### Big Data Types v0.0.7

- BigQuery: TypeClass syntax for case classes instances

### Big Data Types v0.0.6

- BigQuery: JavaConverters for cross version builds
- BigQuery: Formats object that allows different keys transformation like CamelCase -> snake_case for fields