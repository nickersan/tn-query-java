# tn-query-java

TN Query Java provides an implementation of [tn-query](https://github.com/nickersan/tn-query#readme) that builds Java predicates.

## Usage

For each object you want to query create an instance of `com.tn.query.java.JavaQueryParser` passing:

1. the `com.tn.query.java.Getter`s used to read a given field name from the object being queried.
2. the `com.tn.query.Mapper`s used to convert the string value from the query string, to a value to be compared to the object field.

For example, given a class:
```
public class Person
{
  private int id;
  private String firstName;
  private String lastName;
  private Sex sex;
  ...
}
```

An instance of `com.tn.query.java.JavaQueryParser` would be created as follows:
```
QueryParser<Predicate<Target>> queryParser = new JavaQueryParser<>(
  List.of(
    Getter.intValue("id", target -> target.getId()),
    Getter.comparableValue("firstName", person -> person.getFirstName()),
    Getter.comparableValue("lastName", person -> person.getLastName()),
    Getter.comparableValue("sex", person -> person.getSex())
  ),
  List.of(
    Mapper.toInt("id"),
    Mapper.toEnum("sex", Sex.class),
  )
);
```

Note: when no mapper is provided, the value in the query string will be treated as a `java.lang.String`.