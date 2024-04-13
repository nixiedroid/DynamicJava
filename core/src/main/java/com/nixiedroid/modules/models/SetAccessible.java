package com.nixiedroid.modules.models;

import com.nixiedroid.interfaces.ThrowableBiConsumer;
import com.nixiedroid.modules.Const;
import com.nixiedroid.modules.Context;
import com.nixiedroid.modules.util.Resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public abstract class SetAccessible implements ThrowableBiConsumer<AccessibleObject,Boolean> {

    public static class ForJava7 extends SetAccessible{
        private final MethodHandle accessibleSetterMethodHandle;

        public ForJava7() throws NoSuchMethodException, SecurityException, IllegalAccessException {
            Method method;
            method = AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class);
            this.accessibleSetterMethodHandle = Context.i().trustedLookup().unreflect(method);
        }

        @Override
        public void accept(AccessibleObject accessibleObject, Boolean flag) throws Throwable {
            this.accessibleSetterMethodHandle.invokeWithArguments(accessibleObject, flag);
        }
    }

    @SuppressWarnings("unchecked")
    public static class ForJava9 extends SetAccessible {
        private final BiConsumer<AccessibleObject, Boolean> setter;

        public ForJava9() throws Throwable {
                byte[] classBytes = Resources.getResourceBytes(Const.ACCESSIBLE_BLOB);
                Class<?> hookClass = Context.i().useHookClass(AccessibleObject.class, classBytes);
                MethodHandles.Lookup target = Context.i().targetedLookup(hookClass);
                Context.i().setFieldValue(hookClass, hookClass.getDeclaredField("methodHandleRetriever"), target);
                this.setter = (BiConsumer<AccessibleObject, Boolean>) Context.i().allocateInstance(hookClass);
        }

        @Override
        public void accept(AccessibleObject accessibleObject, Boolean flag) {
            this.setter.accept(accessibleObject, flag);
        }
    }
}
