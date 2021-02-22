# How to add a new conversion type

### Context

There are 2 different conversions available for each type.

- Direct conversion -> From SqlType to CustomType
- Reverse conversion -> From CustomType to SqlType

Also, there are 2 different ways of converting data

- Without instances. e.g: Case Class
    - (Usually for `direct conversion`) - This is being done through Type Class derivation
        - A Case Class is being converted into SqlType through SqlTypeConversion Type Class
        - Another Type Class has to derive from SqlTypes to CustomTypes
- With an instance as parameter. e.g: Spark has a StructType instance that defines an schema
