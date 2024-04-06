package com.nixiedroid;

import com.nixiedroid.parts.*;
import com.nixiedroid.parts.lookups.DeepLookup;
import com.nixiedroid.parts.lookups.Lookup;
import com.nixiedroid.parts.lookups.LookupSupplyFunction;
import com.nixiedroid.parts.lookups.PrivateLookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class Context  {
    private UnsafeGetter unsafe;
    //
    private AllocateInstance allocateInstance;
    private Lookup lookup;
    private SetFieldValue setFieldValue;
    private GetFieldValue getFieldValue;
    //
    private PrivateLookup privateLookup;
    //
    private HookClassFromBytes hookClass;
    //
    private LookupSupplyFunction lookupSupply;
    //
    private FieldsFinder getFields;
    private FieldFinder getField;
    //
    private SetAccessible setAccessible;
    //
    private DeepLookup deepLookup;
    //
    private ClassFinder forName0;




    private Context() {
    }

    public static Context i() {
        return Holder.getInstance();
    }

    private void init() {
        {
            this.unsafe = (UnsafeGetter) ClassRetriever.getHandler(UnsafeGetter.class);
        }
        {
            this.allocateInstance = (AllocateInstance) ClassRetriever.getHandler(AllocateInstance.class);
            this.lookup = (Lookup) ClassRetriever.getHandler(Lookup.class);
            this.getFieldValue = (GetFieldValue) ClassRetriever.getHandler(GetFieldValue.class);
            this.setFieldValue = (SetFieldValue) ClassRetriever.getHandler(SetFieldValue.class);
        }
        {
            this.privateLookup = (PrivateLookup) ClassRetriever.getHandler(PrivateLookup.class);
        }
        {
            this.hookClass = (HookClassFromBytes) ClassRetriever.getHandler(HookClassFromBytes.class);
        }
        {
            this.lookupSupply = (LookupSupplyFunction) ClassRetriever.getHandler(LookupSupplyFunction.class);
        }
        {
            this.getFields = (FieldsFinder) ClassRetriever.getHandler(FieldsFinder.class);
            this.getField = (FieldFinder) ClassRetriever.getHandler(FieldFinder.class);
        }
        {
            this.setAccessible = (SetAccessible) ClassRetriever.getHandler(SetAccessible.class);
        }
        {
            this.deepLookup = (DeepLookup) ClassRetriever.getHandler(DeepLookup.class);
        }
        {
            this.forName0 = (ClassFinder) ClassRetriever.getHandler(ClassFinder.class);
        }


    }

    public sun.misc.Unsafe getUnsafe() {
        return unsafe.get();
    }

    public MethodHandles.Lookup lookup() {
        return lookup.get();
    }
    public MethodHandle privateLookup() {
        return privateLookup.get();
    }
    public MethodHandles.Lookup targetedLookup(Class<?> clazz) throws Throwable {
        return lookupSupply.apply(clazz);
    }
    public MethodHandles.Lookup deepLookup(Class<?> clazz)throws Throwable{
        return deepLookup.apply(clazz);
    }

    public Field[] getFields(Class<?> cls, boolean publicOnly) throws Throwable {
        return (Field[]) getFields.getFields().invokeWithArguments(cls,publicOnly);
    }

    public Field getField(Class<?> cls, String name) {
        return getField.getField(cls,name);
    }


    public Object allocateInstance(Class<?> clazz)  {
        try {
            return allocateInstance.allocate(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> useHookClass(Class<?> clientClass, byte[] byteCode) throws Throwable {
        return hookClass.create(clientClass,byteCode);
    }

    public void setAccessible(AccessibleObject obj, boolean flag)  {
        try {
            setAccessible.set(obj,flag);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(Object target, Field f){
        return (T) getFieldValue.accept(target,f);
    }
    public void setFieldValue(Object target, Field field, Object value){
        setFieldValue.accept(target,field,value);
    }
    public Class<?> getClassByName(String str, boolean bool, ClassLoader cld, Class<?> cl){
        try {
            return forName0.apply(str,bool,cld,cl);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public static void poke(){
        i();
    }

    private static class Holder {
        private static final Context INSTANCE;

        static {
            INSTANCE = new Context();
            INSTANCE.init();
        }

        private static Context getInstance() {
            return INSTANCE;
        }
    }

}