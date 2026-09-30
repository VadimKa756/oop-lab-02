package invoker;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Produces argument values for reflective method calls without ever
 * resorting to null. Primitive/wrapper values vary with the call index
 * so repeated invocations are observably distinct. A type without a
 * no-args constructor is instantiated by picking its simplest available
 * constructor and recursively synthesizing arguments for it the same
 * way; a circular constructor dependency is detected and rejected
 * instead of causing infinite recursion.
 */
final class ArgumentFactory {

    private ArgumentFactory() {
    }

    static Object create(Class<?> type, int callIndex) {
        return create(type, callIndex, new HashSet<>());
    }

    private static Object create(Class<?> type, int callIndex, Set<Class<?>> inProgress) {
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
        return instantiate(type, callIndex, inProgress);
    }

    private static Object instantiate(Class<?> type, int callIndex, Set<Class<?>> inProgress) {
        if (!inProgress.add(type)) {
            throw new IllegalArgumentException(
                    "Cannot synthesize an argument of type " + type.getName()
                            + ": its constructors form a circular dependency");
        }

        try {
            Constructor<?> constructor = pickConstructor(type);
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            Object[] arguments = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                arguments[i] = create(parameterTypes[i], callIndex, inProgress);
            }

            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Cannot synthesize a non-null argument of type " + type.getName(), e);
        } finally {
            inProgress.remove(type);
        }
    }

    /**
     * Prefers a no-args constructor when available (zero parameters is the
     * minimum); otherwise falls back to the constructor with the fewest
     * parameters, keeping the recursive synthesis as shallow as possible.
     */
    private static Constructor<?> pickConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException(
                    "Cannot synthesize an argument of type " + type.getName() + ": no accessible constructor");
        }

        return Arrays.stream(constructors)
                .min(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow();
    }
}