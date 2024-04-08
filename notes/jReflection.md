### Creating instance of class with private constructor

```java
PrivateConstructorClass obj = null;
Constructor<PrivateConstructorClass> constructor;
try{
constructor =PrivateConstructorClass .
class.

getDeclaredConstructor();
    constructor.

setAccessible(true);

obj =constructor.

newInstance();
}catch(
Exception a_lot_of){}
```

### Protecting from instantiating private constructor classes

```java
    private SomeUtilityClass() {
    throw new IllegalAccessException("Unable to create instance of utility class");
}
```

### Check if class is abstrct or interface

```java
Class<? super T> cls = (Class<? super T>) Class.forName(className);
				if(Modifier.

isAbstract(cls.getModifiers())||cls.

isInterface()){
        continue;
        }
        return(Class<? super T>)Class.

forName(className);
```

Exceptions:

- __IllegalAccessException__: Access control is
  enforced, and the caller does not have the
  correct access.
- __InstantiationException__: The constructor can
  be
  accessed, but an object cannot be created, for
  example, because the underlying class is
  abstract.
- __InvocationTargetException__: The execution of
  the
  constructor body threw an exception.

### Autoboxing and casting.

The Core Reflection API
contains several variadic methods for lookup and
invocation. For example,
`Constructor.newInstance()` and `invoke()` both
take
`Object...` as their variadic parameter, and this
immediately raises the question of what to do
about primitive values when you call code
reflectively.

First off, the lookup methods (such as
`getConstructor()` and `getMethod()`)
take `Class<?>`
objects as parameters, so you can simply pass the
class literals corresponding to primitive types,
such as `int.class`.

It’s worth noting here that almost all class
literals are singletons. For
example, `String.class`
is the only instance of the type `Class<String>`.
However, there are two instances of Class that are
parameterized by each wrapper type (such as
`Integer`): one for the wrapper type and one for
the
corresponding primitive.

That is, the following code prints false:

``` java
Class<Integer> intClz = int.class;
Class<Integer> integerClz = Integer.class;
System.out.println(intClz == integerClz);
```

The second example above provided a clue as to how
this applies to reflective code in practice.

```java
Class<?> selfClazz = Class.forName("javamag.reflection.ex1.Person");
Constructor<?> ctor = selfClazz.getConstructor(
        String.class, String.class, int.class);
Object o = ctor.newInstance("Robert", "Smith", 63);
```

This code looks up the primary constructor for the
record type and then instantiates an object by
passing a primitive value as the second argument.
This argument will need to be boxed to match the
signature of `Constructor.newInstance()` (which
takes `Object[]`), so the call is really the
following:

```java
Object o = ctor.newInstance("Robert", "Smith",
Integer.valueOf(63));
```
The final argument will then be unboxed in the
reflection implementation code prior to the actual
call to the constructor being made. This approach
is fairly simple once you get used to it, but it
is a little clumsy, and it does require extra
unnecessary boxing operations.

The return value of reflective calls also requires
careful handling; the return type of invoke() is
Object, so any return value needs to be downcast
to an expected, more useful, type.

This cast operation, of course, may fail with a
ClassCastException (which is a runtime exception).