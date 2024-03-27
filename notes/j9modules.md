### List of modules in JAVA 21
java.base@21.0.2
java.compiler@21.0.2
java.datatransfer@21.0.2
java.desktop@21.0.2
java.instrument@21.0.2
java.logging@21.0.2
java.management@21.0.2
java.management.rmi@21.0.2
java.naming@21.0.2
java.net.http@21.0.2
java.prefs@21.0.2
java.rmi@21.0.2
java.scripting@21.0.2
java.se@21.0.2
java.security.jgss@21.0.2
java.security.sasl@21.0.2
java.smartcardio@21.0.2
java.sql@21.0.2
java.sql.rowset@21.0.2
java.transaction.xa@21.0.2
java.xml@21.0.2
java.xml.crypto@21.0.2
jdk.accessibility@21.0.2
jdk.attach@21.0.2
jdk.charsets@21.0.2
jdk.compiler@21.0.2
jdk.crypto.cryptoki@21.0.2
jdk.crypto.ec@21.0.2
jdk.crypto.mscapi@21.0.2
jdk.dynalink@21.0.2
jdk.editpad@21.0.2
jdk.hotspot.agent@21.0.2
jdk.httpserver@21.0.2
jdk.incubator.vector@21.0.2
jdk.internal.ed@21.0.2
jdk.internal.jvmstat@21.0.2
jdk.internal.le@21.0.2
jdk.internal.opt@21.0.2
jdk.internal.vm.ci@21.0.2
jdk.internal.vm.compiler@21.0.2
jdk.internal.vm.compiler.management@21.0.2
jdk.jartool@21.0.2
jdk.javadoc@21.0.2
jdk.jcmd@21.0.2
jdk.jconsole@21.0.2
jdk.jdeps@21.0.2
jdk.jdi@21.0.2
jdk.jdwp.agent@21.0.2
jdk.jfr@21.0.2
jdk.jlink@21.0.2
jdk.jpackage@21.0.2
jdk.jshell@21.0.2
jdk.jsobject@21.0.2
jdk.jstatd@21.0.2
jdk.localedata@21.0.2
jdk.management@21.0.2
jdk.management.agent@21.0.2
jdk.management.jfr@21.0.2
jdk.naming.dns@21.0.2
jdk.naming.rmi@21.0.2
jdk.net@21.0.2
jdk.nio.mapmode@21.0.2
jdk.random@21.0.2
jdk.sctp@21.0.2
jdk.security.auth@21.0.2
jdk.security.jgss@21.0.2
jdk.unsupported@21.0.2
jdk.unsupported.desktop@21.0.2
jdk.xml.dom@21.0.2
jdk.zipfs@21.0.2
### Modules and reflection
Modules are preventing from modifying 
classes from outside module

For example, this code 
```java
 try {
        Class<?> cl = Class.forName("java.util.prefs.Base64",
                    false,ClassLoader.getSystemClassLoader());
            Field f = cl.getDeclaredField("intToBase64");
            f.setAccessible(true);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
```
will throw exception
```java
java.lang.reflect.InaccessibleObjectException: 
Unable to make field private static final char[] 
java.util.prefs.Base64.intToBase64 accessible: 
module java.prefs 
does not "opens java.util.prefs"
to unnamed module @63961c42
```

### Ways to allow module to be modified

[Official docs](https://openjdk.org/jeps/261)

1. Add java argument
   ```sh
   java  --add-opens java.prefs/java.util.prefs=ALL-UNNAMED -jar file.jar 
   ```
   Structure:
   `--add-opens <source-module>/<package>=<target-module>(,<target-module>)*`
   where `<source-module>` and <target-module> are module names 
   and `<package>` is the name of a package. 
2. Add MANIFEST attribute:
    (https://openjdk.org/jeps/261#Packaging:-Modular-JAR-files)
    ```
    Add-Opens: java.prefs/java.util.prefs
   ```
3. Use magic from misc.Unsafe
   (Needs investigation) 
    see 
