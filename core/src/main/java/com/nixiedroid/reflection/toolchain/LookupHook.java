package com.nixiedroid.reflection.toolchain;


import com.nixiedroid.bytes.ByteArrays;
import com.nixiedroid.function.FunctionAdapter;
import com.nixiedroid.function.ThrowableFunction;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

@SuppressWarnings("unused")
interface LookupHook extends ThrowableFunction<Class<?>, MethodHandles.Lookup> {

    abstract class Default implements LookupHook {
        protected ThrowableFunction<Class<?>, MethodHandles.Lookup> function;

        @Override
        public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
            return this.function.apply(clazz);
        }
    }

    class Java7 extends Default {
        Java7()  {
            final MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            final MethodHandle privateLookupInMethodHandle = Context.get(PrivateLookupSupplier.class).get();
            this.function = clazz -> (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(lookup, clazz);
        }
    }

    @SuppressWarnings("unchecked")
    class Java9 extends Default {
        Java9() throws Throwable {
            try (InputStream inputStream = LookupHook.class.getResourceAsStream(Const.LOOKUP_HOOK.getPath())) {
                if(inputStream == null) throw new IllegalArgumentException();
                MethodHandle lookup = Context.get(PrivateLookupSupplier.class).get();
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(Class.class, ByteArrays.toByteArray(inputStream));
                Context.get(SetFieldValueFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("method"), lookup);
                this.function = new FunctionAdapter<>(((java.util.function.Function<Class<?>, MethodHandles.Lookup>) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass)));
            }
        }
    }
}
