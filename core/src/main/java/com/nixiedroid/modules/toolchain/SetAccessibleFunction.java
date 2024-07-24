package com.nixiedroid.modules.toolchain;

import com.nixiedroid.bytes.ByteArrays;
import com.nixiedroid.function.BiConsumerAdapter;
import com.nixiedroid.function.ThrowableBiConsumer;
import com.nixiedroid.modules.Const;
import com.nixiedroid.modules.toolchain.fields.SetFieldValueFunction;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class SetAccessibleFunction implements Tool, ThrowableBiConsumer<AccessibleObject, Boolean> {

    private final ThrowableBiConsumer<AccessibleObject, Boolean> consumer;

    public SetAccessibleFunction() throws Throwable {
        if (thisVersionGEQ(JAVA_9)) {
            try (InputStream inputStream = SetAccessibleFunction.class.getResourceAsStream(Const.ACCESSIBLE_BLOB)) {
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(AccessibleObject.class, ByteArrays.toByteArray(inputStream));
                Context.get(SetFieldValueFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("methodHandleRetriever"), Context.get(LookupHook.class).apply(methodHandleWrapperClass));
                this.consumer = new BiConsumerAdapter<>((java.util.function.BiConsumer<AccessibleObject, Boolean>) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass));
            }
            return;
        } else if (thisVersionGEQ(JAVA_7)) {
            final Method accessibleSetterMethod = AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class);
            final MethodHandle accessibleSetterMethodHandle = Context.get(TrustedLookupSupplier.class).get().unreflect(accessibleSetterMethod);
            this.consumer = accessibleSetterMethodHandle::invokeWithArguments;
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void accept(AccessibleObject accessibleObject, Boolean b) throws Throwable {
        this.consumer.accept(accessibleObject, b);
    }
}
