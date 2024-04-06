package com.nixiedroid.waytwo.magic;

import com.nixiedroid.waytwo.magic.parts.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class Context {
    private UnsafeGetter unsafe;
    private Lookup lookup;
    private ClassFinder forName0;
    private FieldsFinder getDeclaredFields0;
    private FieldFinder getField;
    private AllocateInstance allocateInstance;
    private PrivateLookup privateLookup;
    private HookClass hookClass;
    private SetFieldValue setField;

    private Context() {
    }

    public final static Context i() {
        return Holder.getInstance();
    }

    private void init() {
        this.unsafe = (UnsafeGetter) ClassRetriever.getHandler(UnsafeGetter.class);
        this.allocateInstance = (AllocateInstance) ClassRetriever.getHandler(AllocateInstance.class);
        this.lookup = (Lookup) ClassRetriever.getHandler(Lookup.class);
        this.forName0 = (ClassFinder) ClassRetriever.getHandler(ClassFinder.class);
        this.getDeclaredFields0 = (FieldsFinder) ClassRetriever.getHandler(FieldsFinder.class);
        this.getField = (FieldFinder) ClassRetriever.getHandler(FieldFinder.class);
        this.privateLookup = (PrivateLookup) ClassRetriever.getHandler(PrivateLookup.class);
        this.hookClass = (HookClass) ClassRetriever.getHandler(HookClass.class);
        this.setField  = (SetFieldValue) ClassRetriever.getHandler(SetFieldValue.class);
    }

    public MethodHandle forname() {
        return forName0.get();
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

    public MethodHandle getFields() {
        return getDeclaredFields0.get();
    }

    public MethodHandle getField() {
        return getField.get();
    }
    public SetFieldValue setField(){
        return setField;
    }




    public AllocateInstance getAllocateInstance() {
        return allocateInstance;
    }

    public HookClass getHookClass() {
        return hookClass;
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