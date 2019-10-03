package org.axtin.util;

import org.axtin.container.facade.Container;
import org.bukkit.Server;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {
    public static String NMS_PATH = "net.minecraft.server." + (Container.get(Server.class) != null ? Container.get(Server.class).getClass().getPackage().getName().replace(".", ",").split(",")[3] : "UNKNOWN");
    public static String OBC_PATH = "org.bukkit.craftbukkit." + (Container.get(Server.class) != null ? Container.get(Server.class).getClass().getPackage().getName().replace(".", ",").split(",")[3] : "UNKNOWN");

    public static ReflectionObject execute(String command, Object toCallOn, Object... args) throws Exception {
        if (toCallOn instanceof ReflectionObject) {
            toCallOn = ((ReflectionObject) toCallOn).fetch();
        }

        String[] parts = command.split("\\.");
        Object obj = toCallOn;

        for (String part : parts) {
            if (part.indexOf('(') != -1) {
                if (part.charAt(part.indexOf('(') + 1) == ')') {
                    obj = getObjByFunction(obj, part.substring(0, part.length() - 2));
                } else {
                    String[] arguments = part.substring(part.indexOf('(') + 1, part.indexOf(')')).split(", ");
                    Object[] params = new Object[arguments.length];

                    int i = 0;
                    for (String arg : arguments) {
                        params[i++] = args[Integer.parseInt(arg.replace("{", "").replace("}", "")) - 1];
                    }

                    obj = getObjByFunction(obj, part.substring(0, part.indexOf('(')), params);
                }
            } else {
                obj = getObj(obj, part);
            }
        }
        return new ReflectionObject(obj);
    }

    public static ReflectionObject executeStatic(String command, Class<?> toCallOn, Object... args) throws Exception {
        String[] parts = command.split("\\.");
        Object obj = toCallOn;

        for (String part : parts) {
            if (part.indexOf('(') != -1) {
                if (part.charAt(part.indexOf('(') + 1) == ')') {
                    obj = getStaticObjByFunction(toCallOn, part.substring(0, part.length() - 2));
                } else {
                    String[] arguments = part.substring(part.indexOf('(') + 1, part.indexOf(')')).split(", ");
                    Object[] params = new Object[arguments.length];

                    int i = 0;
                    for (String arg : arguments) {
                        params[i++] = args[Integer.parseInt(arg.replace("{", "").replace("}", "")) - 1];
                    }

                    obj = getStaticObjByFunction(toCallOn, part.substring(0, part.indexOf('(')), params);
                }
            } else {
                obj = getStaticObj(toCallOn, part);
            }
        }
        return new ReflectionObject(obj);
    }

    protected static Object getObj(Object obj, String field) throws Exception {
        try {
            Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException ex) {
            Class<?> cur = obj.getClass().getSuperclass();
            while (true) {
                try {
                    Field f = cur.getDeclaredField(field);
                    f.setAccessible(true);
                    return f.get(obj);
                } catch (NoSuchFieldException ex2) {
                    if (cur.getSuperclass() != null) {
                        cur = cur.getSuperclass();
                        continue;
                    }
                    return null;
                }
            }
        }
    }

    protected static Object getStaticObj(Class<?> obj, String field) throws Exception {
        Field f = obj.getDeclaredField(field);
        f.setAccessible(true);
        return f.get(null);
    }

    protected static Object getObjByFunction(Object obj, String methodName) throws Exception {
        Method m = null;
        Class<?> c;
        for (c = obj.getClass(); c != null; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    m = method;
                }
            }
        }
        m.setAccessible(true);
        return m.invoke(obj);
    }

    protected static Object getStaticObjByFunction(Class<?> obj, String methodName) throws Exception {
        Method m = null;
        Class<?> c;
        for (c = obj; c != null; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    m = method;
                }
            }
        }
        m.setAccessible(true);
        return m.invoke(obj);
    }

    protected static Object getObjByFunction(Object obj, String name, Object[] args) throws Exception {
        Method m = null;
        Class<?> c;
        for (c = obj.getClass(); c != null; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(name) && checkForMatch(method.getParameterTypes(), args)) {
                    m = method;
                }
            }
        }
        if (m == null) {
            throw new IllegalArgumentException("Could not find function " + name + " with arguments " + Arrays.toString(args) + " on object " + obj.getClass().getName());
        }
        m.setAccessible(true);
        return m.invoke(obj, args);
    }

    protected static Object getStaticObjByFunction(Class<?> obj, String name, Object[] args) throws Exception {
        Method m = null;
        Class<?> c;
        for (c = obj; c != null; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(name) && checkForMatch(method.getParameterTypes(), args)) {
                    m = method;
                }
            }
        }
        if (m == null) {
            throw new IllegalArgumentException("Could not find function " + name + " with arguments " + Arrays.toString(args) + " on object " + obj.getClass().getName());
        }
        m.setAccessible(true);
        return m.invoke(obj, args);
    }

    private static boolean checkForMatch(Class<?>[] classes, Object[] args) {
        if (classes.length != args.length) {
            return false;
        }
        int i = 0;
        for (Class<?> cls : classes) {
            Object obj = args[i++];
            if (!cls.isAssignableFrom(obj.getClass()) && !isPrimitiveWrapper(cls, obj.getClass())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPrimitiveWrapper(Class<?> class1, Class<?> class2) {
        if ((class1 == Integer.class && class2 == int.class) || (class1 == int.class && class2 == Integer.class)) {
            return true;
        }
        if ((class1 == Boolean.class && class2 == boolean.class) || (class1 == boolean.class && class2 == Boolean.class)) {
            return true;
        }
        if ((class1 == Character.class && class2 == char.class) || (class1 == char.class && class2 == Character.class)) {
            return true;
        }
        if ((class1 == Float.class && class2 == float.class) || (class1 == float.class && class2 == Float.class)) {
            return true;
        }
        if ((class1 == Double.class && class2 == double.class) || (class1 == double.class && class2 == Double.class)) {
            return true;
        }
        if ((class1 == Long.class && class2 == long.class) || (class1 == long.class && class2 == Long.class)) {
            return true;
        }
        if ((class1 == Short.class && class2 == short.class) || (class1 == short.class && class2 == Short.class)) {
            return true;
        }
        return (class1 == Byte.class && class2 == byte.class) || (class1 == byte.class && class2 == Byte.class);
    }

    private static Constructor<?> getMatchingConstructor(Class<?> cls, Object... args) {
        for (Constructor<?> constr : cls.getConstructors()) {
            if (checkForMatch(constr.getParameterTypes(), args)) {
                return constr;
            }
        }
        throw new IllegalArgumentException("Could not create a ReflectionObject for class " + cls.getName() + ": No " + "matching constructor found!");
    }

    public static class ReflectionObject {
        private final Object object;

        public ReflectionObject(Object obj) {
            this.object = obj;
        }

        public static ReflectionObject fromNMS(String name, Object... args) {
            return from(NMS_PATH, name, args);
        }

        public static ReflectionObject fromOBC(String name, Object... args) {
            return from(OBC_PATH, name, args);
        }

        private static ReflectionObject from(String path, String name, Object... args) {
            try {
                Class<?> clazz = Class.forName(path + "." + name);
                return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
            } catch (Exception e) {
                try {
                    Class<?> clazz = Class.forName(name);
                    return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
                } catch (Exception ex) {
                    return null;
                }
            }
        }

        public Object fetch() {
            return object;
        }

        public ReflectionObject invoke(String methodName, Object... args) {
            try {
                Object obj;
                obj = getObjByFunction(object, methodName, args);
                if (obj != null) {
                    return new ReflectionObject(obj);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not invoke " + methodName + " on object of class " + this.object.getClass().getName(), e);
            }
            return null;
        }

        public Object get(String field) {
            try {
                if (object instanceof Class) {
                    return getStaticObj((Class<?>) object, field);
                } else {
                    return getObj(object, field);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not get " + field + " on object of class " + this.object.getClass().getName(), e);
            }
        }

        public ReflectionObject getAsRO(String field) {
            return new ReflectionObject(get(field));
        }

        public <T> T fetchAs(Class<T> as) {
            try {
                return castOrCreate(object, as);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        public <T> T get(String field, Class<T> as) {
            try {
                Object obj = get(field);
                return castOrCreate(obj, as);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        private <T> T castOrCreate(Object obj, Class<T> as) throws Exception {
            try {
                if (as.isAssignableFrom(obj.getClass())) {
                    return (T) obj;
                }

                Constructor<?> constr = getMatchingConstructor(as, obj);
                if (constr != null) {
                    return (T) constr.newInstance(obj);
                }

                throw new IllegalArgumentException("Could not convert object of class " + obj.getClass().getName() +
                        " to " + as.getName());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        public void set(String field, Object arg) {
            try {
                Field f = object.getClass().getDeclaredField(field);
                f.setAccessible(true);
                f.set(object, arg);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not set " + field + " on object of class " + this.object.getClass().getName(), e);
            }
        }
    }

    public static class ReflectionStatic {
        public static Class<?> fromNMS(String name) {
            return from(NMS_PATH, name);
        }

        public static Class<?> fromOBC(String name) {
            return from(OBC_PATH, name);
        }

        private static Class<?> from(String path, String name) {
            try {
                Class<?> clazz = Class.forName(path + "." + name);
                return clazz;
            } catch (Exception ignore) {
                try {
                    Class<?> clazz = Class.forName(name);
                    return clazz;
                } catch (Exception ignore2) {
                    return null;
                }
            }
        }
    }
}