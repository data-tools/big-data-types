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