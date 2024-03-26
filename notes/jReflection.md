### Creating instance of class with private constructor
```java
PrivateConstructorClass obj = null;
Constructor<PrivateConstructorClass> constructor;
try {
    constructor = PrivateConstructorClass.
        class.getDeclaredConstructor();
    constructor.setAccessible(true);
    obj =  constructor.newInstance();
} catch (Exception a_lot_of) {}
```

### Protecting from instantiating private constructor classes
```java
    private SomeUtilityClass() {
       throw new IllegalAccessException("Unable to create instance of utility class");
    }
```
