package com.nixiedroid.modules;

import com.nixiedroid.modules.models.*;
import com.nixiedroid.modules.models.fields.GetField;
import com.nixiedroid.modules.models.fields.GetFieldValue;
import com.nixiedroid.modules.models.fields.GetFields;
import com.nixiedroid.modules.models.fields.SetFieldValue;
import com.nixiedroid.modules.models.lookups.LookupCtor;
import com.nixiedroid.modules.models.lookups.Lookup;
import com.nixiedroid.modules.models.lookups.TargetedLookup;
import com.nixiedroid.modules.models.lookups.PrivateLookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.function.Supplier;

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
    private TargetedLookup lookupSupply;
    //
    private GetFields getFields;
    private GetField getField;
    //
    private SetAccessible setAccessible;
    //
    private LookupCtor lookupCtor;
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
            this.lookupSupply = (TargetedLookup) ClassRetriever.getHandler(TargetedLookup.class);
        }
        {
            this.getFields = (GetFields) ClassRetriever.getHandler(GetFields.class);
            this.getField = (GetField) ClassRetriever.getHandler(GetField.class);
        }
        {
            this.setAccessible = (SetAccessible) ClassRetriever.getHandler(SetAccessible.class);
        }
        {
            this.lookupCtor = (LookupCtor) ClassRetriever.getHandler(LookupCtor.class);
        }
        {
            this.forName0 = (ClassFinder) ClassRetriever.getHandler(ClassFinder.class);
        }
    }


    public sun.misc.Unsafe getUnsafe() {
        return this.unsafe.get();
    }

    public MethodHandles.Lookup trustedLookup() {
        return this.lookup.getTrustedLookup();
    }
    public MethodHandle privateLookup() {
        return privateLookup.get();
    }
    public MethodHandles.Lookup targetedLookup(Class<?> clazz) throws Throwable {
        return this.lookupSupply.apply(clazz);
    }
    public MethodHandles.Lookup lookupCtor(Class<?> clazz)throws Throwable{
        return this.lookupCtor.apply(clazz);
    }

    public Field[] getFields(Class<?> cls, boolean publicOnly) throws Throwable {
        return (Field[]) this.getFields.getFields().invokeWithArguments(cls,publicOnly);
    }

    public Field getField(Class<?> cls, String name) throws Throwable {
        return getField.apply(cls,name);
    }
    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(Object target, Field f){
        return (T) this.getFieldValue.accept(target,f);
    }
    public void setFieldValue(Object target, Field field, Object value){
        this.setFieldValue.accept(target,field,value);
    }

    public Object allocateInstance(Class<?> clazz) throws Throwable {
        return this.allocateInstance.apply(clazz);
    }

    public Class<?> useHookClass(Class<?> clientClass, byte[] byteCode) throws Throwable {
        return this.hookClass.apply(clientClass,byteCode);
    }

    public void setAccessible(AccessibleObject obj, boolean flag) throws Throwable {
        this.setAccessible.accept(obj,flag);
    }

    public Class<?> getClassByName(String str, boolean bool, ClassLoader cld, Class<?> cl) throws Throwable {
        return this.forName0.apply(str,bool,cld,cl);
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