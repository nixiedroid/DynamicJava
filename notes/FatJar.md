### Shaded-Jar vs Uber-Jar

Uber jar is a single .jar file, containing all
dependencies
Shaded jar is uber jar with renamed packages to
avoid dependency-hell
For example:

You create an uber JAR which contains `v1.0.0` of
the `Foo` library.
Someone else uses your uber JAR in their
application, `Bar`
The `Bar` application has its own dependency
on `Foo` but on `v1.2.0` of that library.
Now, if there is any clash between
versions `1.0.0` and `1.2.0` of `Foo` we may have
a problem because the owner of `Bar` cannot rely
on which one will be
loaded so either their code will misbehave or your
code - when running within their application -
will misbehave.

Shading helps to avoid issues such as this and
also allows the provider of Foo to be explicit
about the versions of the dependent libraries it
uses.