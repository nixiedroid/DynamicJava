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

### Creating Custom ClassLoader

To Create Our own classloader we need to implement
at least two methods from Class Classloader:

1. `public Class<?> loadClass(String name) throws ClassNotFoundException {}`
2. `protected Class<?> findClass(String name) throws ClassNotFoundException {}`

All Classes, that our class target is dependent
are also loaded using custom classloader,
so we must direct non-processing classes to
higher-level
classloader, like

```java

@Override
public Class<?> loadClass(String name) throws ClassNotFoundException {
    Class<?> cl = findLoadedClass(name);
    if (cl != null) return cl;
    if (name.startsWith(PKG_PREFIX)) {
        cl = findClass(name);
        if (cl != null) return cl;
    }
    return super.loadClass(name);
}
```

Sadly, name of loadable class must be equal to
name inside .class file

### Get class at runtime

- `Class.forName(String className)`
  Find class, loaded by current classloader (and
  superclasses)
- `Class.forName0(String name, boolean initialize, ClassLoader loader)`
  Native internal method
- `Class.forName(String name, boolean initialize, ClassLoader loader)`
  Uses SecurityManager to verify ability to load
  classes
  SecurityManager is deprecated since java17, so igone it 






