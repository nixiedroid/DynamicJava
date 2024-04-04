### Usage

JavaAgent is a method to execute code
before `main()` class of java application

javaagent is a jar file containing:

1. MANIFEST.MF with attribute:
    ```
   'Premain-class': 'premain-class-package-and-name'
   ```
2. Class with
   method 
   ```
   public static void premain(final String agentArgs, final Instrumentation inst) {}
   ```

Code inside `premain()` will be executed
before `main()` class of application