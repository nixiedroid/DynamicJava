package com.nixiedroid.modules.toolchain;


import com.nixiedroid.bytes.ByteArrays;
import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.function.FunctionAdapter;
import com.nixiedroid.function.ThrowableFunction;
import com.nixiedroid.modules.Const;
import com.nixiedroid.modules.toolchain.fields.SetFieldValueFunction;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class LookupHook implements ThrowableFunction<Class<?>, MethodHandles.Lookup>, Tool {

    private ThrowableFunction<Class<?>, MethodHandles.Lookup> function;

    @SuppressWarnings("unchecked")
    public LookupHook() {
        if (thisVersionGEQ(JAVA_9)) {
            try (InputStream inputStream = LookupHook.class.getResourceAsStream(Const.LOOKUP_HOOK)) {
                MethodHandle lookup = Context.get(PrivateLookupSupplier.class).get();
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(Class.class, ByteArrays.toByteArray(inputStream));
                Context.get(SetFieldValueFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("method"), lookup);
                function = new FunctionAdapter(((java.util.function.Function<Class<?>, MethodHandles.Lookup>) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass)));
                return;
            } catch (Throwable e) {
                Thrower.throwException(e);
            }
        } else if (thisVersionGEQ(JAVA_7)) {
            final MethodHandles.Lookup consulter = Context.get(TrustedLookupSupplier.class).get();
            final MethodHandle privateLookupInMethodHandle = Context.get(PrivateLookupSupplier.class).get();
            function = new ThrowableFunction<Class<?>, MethodHandles.Lookup>() {
                @Override
                public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
                    return (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(consulter, clazz);
                }
            };
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
        return this.function.apply(clazz);
    }
}
