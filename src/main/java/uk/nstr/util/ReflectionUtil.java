package uk.nstr.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Class getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The class, " + clazz + ", does not exist.");
        }
    }

    public static Method getMethod(Class clazz, String method, Class<?>... params) {
        try {
            return clazz.getMethod(method, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The method, " + method + ", does not exist in the class, " + clazz.getName() + ".");
        }
    }

    public static Constructor getConstructor(Class clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Constructor does not exist in the class, " + clazz.getName() + ".");
        }
    }

    public static Object invokeStatic(Method method, Object... args) {
        try {

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Object ret = method.invoke(null, args);

            method.setAccessible(false);
            return ret;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not statically invoke, " + method.getName() + ".");
        }
    }

    public static Object invoke(Object object, Method method, Object... args) {
        try {

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Object ret = method.invoke(object, args);

            method.setAccessible(false);
            return ret;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not invoke, " + method.getName() + ".");
        }
    }

    public static Object invoke(Constructor constructor, Object... args) {
        try {

            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            Object ret = constructor.newInstance(args);
            constructor.setAccessible(false);

            return ret;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Could not invoke, " + constructor.getName() + ".");
        }
    }

}
