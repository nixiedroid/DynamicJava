`javac -g  Main.java && cls && javap -v -p Main*.class`

> This process is the basis of method overriding
> in the JVM. To make the process efficient, the
> vtables are laid out in a specific way. Each
> klass
> lays out its vtable so that the first methods to
> appear are the methods that the parent type
> defines. These methods are laid out in the exact
> order that the parent type used. The methods
> that
> are new to this type and are not declared by the
> parent class come at the end of the vtable.

> The result? When a subclass overrides a method,
> it
> will be at the same offset in the vtable as the
> implementation being overridden. This makes the
> lookup of overridden methods completely trivial
> because their offset in the vtable will be the
> same as the offset of their parent.
[
_source_](https://blogs.oracle.com/javamagazine/post/mastering-the-mechanics-of-java-method-invocation)

What would happen, if one swap methods inside
vtable?

And, that __is__ the reason we cannot extend
multiple classes

However, interfaces methods are not using
inheritance, and can be implemented multiple times

Method calls:

- __invokeStatic__
  static methods
- __invokeVirtual__
  not-static method
  propagation to super-class vtable
  default method in interface. Can be overriden
- __invokeInterface__
  interface method
  The selection logic allows a non-abstract method
  declared in a superinterface to be selected
  Called on Interface class
- __invokeSpecial__
  JVM will look only in the exact place in the
  vtable for the requested method. This means that
  an __invokespecial__ is used for three cases:
    1. private methods,
    2. calls to a superclass method,
    3. calls to the constructor body (which is
       turned into a method called `<init>`)

- __invokeDynamic__
  Invoke a dynamically-computed call site
  Used by lambda expressions

Final and private methods _could_ use
invokespecial, but
they are not, to allow users remove final
modifier and not break already-compiled code

Anonymous classes stored in another .class file

Tn the case of __invokestatic__ and _
_invokespecial__,
the exact implementation method (referred to as
the call target) is known at compile time. In the
case of __invokevirtual__ and __invokeinterface__,
the
call target is determined at runtime. However, the
target selection is subject to the constraints of
the Java language inheritance rules and type
system. As a result, at least some call target
information is known at compile time.

In contrast, __invokedynamic__ is far more flexible
about which method will actually be called when
the opcode is dispatched. To allow for this
flexibility, __invokedynamic__ opcodes refer to a
special attribute in the constant pool of the
class that contains the dynamic invocation. This
attribute contains additional information to
support the dynamic nature of the call, called
bootstrap methods (BSMs)

Example of BSM:
```
  0: #64 REF_invokeStatic java/lang/invoke/LambdaMetafactory
  .metafactory:(
  Ljava/lang/invoke/MethodHandles$Lookup;
  Ljava/lang/String;
  Ljava/lang/invoke/MethodType;
  Ljava/lang/invoke/MethodType;
  Ljava/lang/invoke/MethodHandle;
  Ljava/lang/invoke/MethodType;
  )Ljava/lang/invoke/CallSite;
    Method arguments:
      #52 ()V
      #53 REF_invokeStatic Main.lambda$main$0:()V
      #52 ()V
  1: #64 REF_invokeStatic java/lang/invoke/LambdaMetafactory
  .metafactory:(
  Ljava/lang/invoke/MethodHandles$Lookup;
  Ljava/lang/String;
  Ljava/lang/invoke/MethodType;
  Ljava/lang/invoke/MethodType;
  Ljava/lang/invoke/MethodHandle;
  Ljava/lang/invoke/MethodType;
  )Ljava/lang/invoke/CallSite;
    Method arguments:
      #56 (Ljava/lang/Object;)I
      #57 REF_invokeStatic Main.lambda$main$1:(Ljava/lang/Object;)I
      #56 (Ljava/lang/Object;)I
```
from LambdaMetafactory:
``` 
public static java.lang.invoke.CallSite metafactory(     
    @NotNull  java.lang.invoke.MethodHandles.Lookup caller,
    @NotNull  String interfaceMethodName,
    @NotNull  java.lang.invoke.MethodType factoryType,
    @NotNull  java.lang.invoke.MethodType interfaceMethodType,
    @NotNull  java.lang.invoke.MethodHandle implementation,
    @NotNull  java.lang.invoke.MethodType dynamicMethodType )
```