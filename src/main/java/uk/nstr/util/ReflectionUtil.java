package uk.nstr.util;

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

    public static Object invoke(Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not invoke, " + method.getName() + ".");
        }
    }

}
