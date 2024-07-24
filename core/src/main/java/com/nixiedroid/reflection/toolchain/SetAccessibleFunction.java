package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.bytes.ByteArrays;
import com.nixiedroid.function.BiConsumerAdapter;
import com.nixiedroid.function.ThrowableBiConsumer;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
interface SetAccessibleFunction  extends ThrowableBiConsumer<AccessibleObject, Boolean> {

    abstract class Default implements SetAccessibleFunction {

        protected ThrowableBiConsumer<AccessibleObject, Boolean> consumer;

        @Override
        public void accept(AccessibleObject accessibleObject, Boolean b) throws Throwable {
            this.consumer.accept(accessibleObject, b);
        }
    }

    class Java7 extends Default {
        @SuppressWarnings("JavaReflectionMemberAccess")
        Java7() throws Throwable {
            final Method accessibleSetterMethod = AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class);
            final MethodHandle accessibleSetterMethodHandle = Context.get(TrustedLookupSupplier.class).get().unreflect(accessibleSetterMethod);
            this.consumer = accessibleSetterMethodHandle::invokeWithArguments;
        }
    }

    class Java9 extends Default {
        @SuppressWarnings("unchecked")
        Java9() throws Throwable {
            try (InputStream inputStream = SetAccessibleFunction.class.getResourceAsStream(Const.ACCESSIBLE_BLOB.getPath())) {
                if(inputStream == null) throw new IllegalArgumentException();
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(AccessibleObject.class, ByteArrays.toByteArray(inputStream));
                Context.get(SetFieldValueFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("methodHandleRetriever"), Context.get(LookupHook.class).apply(methodHandleWrapperClass));
                this.consumer = new BiConsumerAdapter<>((java.util.function.BiConsumer<AccessibleObject, Boolean>) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass));
            }
        }
    }

}
