# How to develop a new type

Adding a new type to the library will allow conversions from any developed type into the new one 
and from the new one into any of the others

## How it works

There is an ADT (sealed trait) called `SqlType` that is used as a generic type for any transformation. 
It works as a bridge, so any developed type can be transformed into SqlType and SqlType can be converted into specific types.

By doing so, when we add a new type, we don't need to implement conversions between all the types, we only need to implement
`Conversion` from our type to `SqlType` and `Reverse Conversion` from `SqlType` to our type, 
and **we will get automatically conversion from / to the rest of the types in the library**.

An important note of nomenclature:

- We call `Conversion` to anything that converts `into the geneic SqlType`
- We call `Reverse Conversion` to anything that converts `from generic SqlType into specific`


### Conversion / Reverse Conversion
There are 2 type of conversions that we should cover

- `Conversion` = Generic SqlType -> Our Type
- `Reverse Conversion` = Our type -> Generic SqlType

#### Reverse Conversion
`our type -> SqlType` = `our type -> all types`

In order to implement the conversion from our new type to `SqlType` we have to implement 
an existing _Type Class_ called `SqlTypeConversion`

By doing this, we will get automatically conversion to the rest of the types of the library


#### Conversion 
`SqlType -> our type` = `All types -> our type`
For the _normal_ _Conversion_ we have to create two new _Type Classes_

- `SqlTypeToOurType` for only types (converting a Case Class into our new type) -> e.g: `Conversion[A].myNewType`
- `SqlInstanceToOurType` for convert instances into our new type -> e.g: `Conversion[A](myInstance: A).myNewType` 
(`A` could be an instance of any implemented type in the library)

One will be based on the other one, so don't worry, one will be really short.


## Implement Conversion

As covered in [Conversion](#conversion), we have to implement 2 types clases, one for types, another for instances

### Type Conversion



