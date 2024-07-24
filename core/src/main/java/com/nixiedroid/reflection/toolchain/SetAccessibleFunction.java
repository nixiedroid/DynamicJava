package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.bytes.ByteArrays;
import com.nixiedroid.function.BiConsumerAdapter;
import com.nixiedroid.function.ThrowableBiConsumer;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * A functional interface for setting the accessibility of {@link AccessibleObject} instances.
 * This interface provides a way to set the accessible flag of reflective objects.
 */
@SuppressWarnings("unused")
interface SetAccessibleFunction extends ThrowableBiConsumer<AccessibleObject, Boolean> {

    /**
     * Default implementation of {@link SetAccessibleFunction}.
     * Provides a base implementation where the actual logic for setting accessibility is defined by subclasses.
     */
    abstract class Default implements SetAccessibleFunction {
        protected ThrowableBiConsumer<AccessibleObject, Boolean> consumer;

        /**
         * Accepts an {@link AccessibleObject} and a {@code boolean} flag to set accessibility.
         *
         * @param accessibleObject the {@link AccessibleObject} whose accessibility is to be set
         * @param b                {@code true} to make the object accessible, {@code false} to make it inaccessible
         * @throws Throwable if an error occurs while setting accessibility
         */
        @Override
        public void accept(AccessibleObject accessibleObject, Boolean b) throws Throwable {
            this.consumer.accept(accessibleObject, b);
        }
    }

    /**
     * Java 7 implementation of {@link SetAccessibleFunction}.
     * Uses reflection and method handles to invoke the private "setAccessible0" method of {@link AccessibleObject}.
     */
    class Java7 extends Default {
        @SuppressWarnings("JavaReflectionMemberAccess")
        Java7() throws Throwable {
            final Method accessibleSetterMethod = AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class);
            final MethodHandle accessibleSetterMethodHandle = Context.get(TrustedLookupSupplier.class).get().unreflect(accessibleSetterMethod);
            this.consumer = accessibleSetterMethodHandle::invokeWithArguments;
        }
    }

    /**
     * Java 9 implementation of {@link SetAccessibleFunction}.
     * Uses bytecode manipulation and method handles to dynamically create and use a wrapper class for setting accessibility.
     */
    class Java9 extends Default {
        @SuppressWarnings("unchecked")
        Java9() throws Throwable {
            try (InputStream inputStream = SetAccessibleFunction.class.getResourceAsStream(Const.ACCESSIBLE_BLOB.getPath())) {
                if (inputStream == null) throw new IllegalArgumentException("Resource not found");
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(AccessibleObject.class, ByteArrays.toByteArray(inputStream));
                Context.get(SetFieldValueFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("methodHandleRetriever"),
                        Context.get(LookupHook.class).apply(methodHandleWrapperClass));
                this.consumer = new BiConsumerAdapter<>((java.util.function.BiConsumer<AccessibleObject, Boolean>) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass));
            }
        }
    }
}

