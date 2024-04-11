package com.nixiedroid.modules.models;

import com.nixiedroid.modules.Const;
import com.nixiedroid.modules.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public abstract class SetAccessible {
    public abstract void set(AccessibleObject obj, boolean flag) throws Throwable;
    private MethodHandle accessibleSetterMethodHandle;

    public class ForJava7 extends SetAccessible{

        public ForJava7() throws NoSuchMethodException, SecurityException, IllegalAccessException {
            final Method accessibleSetterMethod =
                    AccessibleObject.class.getDeclaredMethod(
                            "setAccessible0",
                            AccessibleObject.class,
                            boolean.class);
              accessibleSetterMethodHandle = Context.i().trustedLookup().unreflect(accessibleSetterMethod);
        }

        @Override
        public void set(AccessibleObject accessibleObject, boolean flag) throws Throwable {
            accessibleSetterMethodHandle.invokeWithArguments(accessibleObject, flag);
        }

    }

    @SuppressWarnings("unchecked")
    public static class ForJava9 extends SetAccessible {
        private BiConsumer<AccessibleObject, Boolean> setter;

        public ForJava9() throws Throwable {
            try (
                    InputStream inputStream = SetAccessible.class.getResourceAsStream(
                            Const.ACCESSIBLE_BLOB
                    );
            ) {
                byte[] classBytes;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while (-1 != (bytesRead = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    classBytes = outputStream.toByteArray();
                }
                Class<?> methodHandleWrapperClass = Context.i().useHookClass(AccessibleObject.class, classBytes);
                MethodHandles.Lookup target = Context.i().targetedLookup(methodHandleWrapperClass);
                Context.i().setFieldValue(
                        methodHandleWrapperClass,
                        methodHandleWrapperClass.getDeclaredField("methodHandleRetriever"),
                        target
                );
                setter = (BiConsumer<AccessibleObject, Boolean>) Context.i().allocateInstance(methodHandleWrapperClass);
            }
        }

        @Override
        public void set(AccessibleObject accessibleObject, boolean flag) {
            setter.accept(accessibleObject, flag);
        }
    }
}
