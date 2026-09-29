package invoker;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

/**
 * Produces argument values for reflective method calls without ever
 * resorting to null. Primitive/wrapper values vary with the call index
 * so repeated invocations are observably distinct. Any type without a
 * no-args constructor cannot be synthesized safely, so the factory
 * fails loudly instead of silently defaulting to null.
 */
final class ArgumentFactory {

    private ArgumentFactory() {
    }

    static Object create(Class<?> type, int callIndex) {
        if (type == int.class || type == Integer.class) {
            return callIndex + 1;
        }
        if (type == long.class || type == Long.class) {
            return (long) (callIndex + 1);
        }
        if (type == double.class || type == Double.class) {
            return (callIndex + 1) * 0.5;
        }
        if (type == float.class || type == Float.class) {
            return (callIndex + 1) * 0.5f;
        }
        if (type == boolean.class || type == Boolean.class) {
            return callIndex % 2 == 0;
        }
        if (type == char.class || type == Character.class) {
            return (char) ('a' + (callIndex % 26));
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) (callIndex + 1);
        }
        if (type == short.class || type == Short.class) {
            return (short) (callIndex + 1);
        }
        if (type == String.class) {
            return "generated-value-" + (callIndex + 1);
        }
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        return instantiateViaDefaultConstructor(type);
    }

    private static Object instantiateViaDefaultConstructor(Class<?> type) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Cannot synthesize a non-null argument of type " + type.getName()
                            + ": no no-args constructor available", e);
        }
    }
}