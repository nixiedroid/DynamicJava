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

### Check if class is abstrct or interface
```java
Class<? super T> cls = (Class<? super T>)Class.forName(className);
				if (Modifier.isAbstract(cls.getModifiers()) || cls.isInterface()) {
					continue;
				}
				return (Class<? super T>)Class.forName(className);
```