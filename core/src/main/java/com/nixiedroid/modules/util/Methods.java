package com.nixiedroid.modules.util;

import com.nixiedroid.modules.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Methods {
    public static <T> T invokeStaticDirect(Class<?> targetClass, String methodName, Object... arguments) throws Throwable {
        return invokeDirect(targetClass, null, methodName, ArrayList::new, arguments);
    }

    public static <T> T invokeDirect(Object target, String methodName, Object... arguments) throws Throwable {
        return invokeDirect(Classes.retrieveFrom(target), target, methodName, () -> {
            List<Object> argumentList = new ArrayList<>();
            argumentList.add(target);
            return argumentList;
        }, arguments);
    }

    private static <T> T invokeDirect(Class<?> targetClass, Object target, String methodName, Supplier<List<Object>> listSupplier, Object... arguments) throws Throwable {
        Class<?>[] argsType = Classes.retrieveFrom(arguments);
        Method method = findDirectHandle(targetClass, methodName, argsType);
        try {
            MethodHandle methodHandle = retrieveMethodHandle(Context.i().trustedLookup(), method);
            List<Object> argumentList = getFlatArgumentList(method, listSupplier, arguments);
            return (T) methodHandle.invokeWithArguments(argumentList);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e.getCause());
        }
    }

    static MethodHandle retrieveMethodHandle(MethodHandles.Lookup consulter, Method method) throws java.lang.NoSuchMethodException, IllegalAccessException {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        return !Modifier.isStatic(method.getModifiers()) ? consulter.findSpecial(methodDeclaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()), methodDeclaringClass) : consulter.findStatic(methodDeclaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
    }

    static List<Object> getFlatArgumentList(Method member, Supplier<List<Object>> argumentListSupplier, Object... arguments) {
        Parameter[] parameters = member.getParameters();
        List<Object> argumentList = argumentListSupplier.get();
        if (arguments != null) {
            if (parameters.length > 0 && parameters[parameters.length - 1].isVarArgs()) {
                for (int i = 0; i < arguments.length && i < parameters.length - 1; i++) {
                    argumentList.add(arguments[i]);
                }
                if (arguments.length == parameters.length) {
                    Parameter lastParameter = parameters[parameters.length - 1];
                    Object lastArgument = arguments[arguments.length - 1];
                    if (lastArgument != null && lastArgument.getClass().isArray() && lastArgument.getClass().equals(lastParameter.getType())) {
                        for (int i = 0; i < Array.getLength(lastArgument); i++) {
                            argumentList.add(Array.get(lastArgument, i));
                        }
                    } else {
                        argumentList.add(lastArgument);
                    }
                } else if (arguments.length > parameters.length) {
                    for (int i = parameters.length - 1; i < arguments.length; i++) {
                        argumentList.add(arguments[i]);
                    }
                } else if (arguments.length < parameters.length) {
                    argumentList.add(null);
                }
            } else if (arguments.length > 0) {
                for (Object argument : arguments) {
                    argumentList.add(argument);
                }
            }
        } else {
            argumentList.add(null);
        }
        return argumentList;
    }

    private static Method findDirectHandle(Class<?> targetClass, String methodName, Class<?>... inputParameterTypes) throws Throwable {
        Method entry;
        try {
            entry = targetClass.getDeclaredMethod(methodName, inputParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Context.i().setAccessible(entry, true);
        return entry;
    }

}
