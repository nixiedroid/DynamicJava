Java loads dependent classes at runtime

Example:
class Foo uses method baz() from class Bar

### Get classloader of $CLASS

```java
$CLASS.getClassLoader();
```

### Notable Java Classloaders

1. __Bootstrap classloader__
   Loads core java classes.
   Started from native code,
   so does not have corresponding java class.
   class.getClassLoader() would be equal to "null"
   in this case
2. __PlatformClassLoader__
   Loads Extended java libraries from jvm package
   ```   
   class.getClassLoader().getClass().getSimpleName() == PlatformClassLoader
   ```
3. __ApplicationClassLoader__
   Loads user code
   ```   
   class.getClassLoader().getClass().getSimpleName() == AppClassLoader
   ```

### How do ClassLoader works?

The `java.lang.ClassLoader.loadClass()` method is
responsible for loading the class definition into
runtime.
It tries to load the class based on a fully
qualified name.

If the class isn’t already loaded, it delegates
the request to the _parent_ class loader. This
process happens recursively.

Eventually, if the parent class loader doesn’t
find the class, then the child class will call the
`java.net.URLClassLoader.findClass()` method to
look
for classes in the file system itself.

If the last child class loader isn’t able to load
the class either, it throws
`java.lang.NoClassDefFoundError` or
`java.lang.ClassNotFoundException.`






