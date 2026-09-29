package invoker;

import annotation.InvokeTimes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reflectively invokes every {@link InvokeTimes}-annotated protected or
 * private method of a target object, the number of times its annotation
 * specifies. Neither the method count, their parameter count, nor their
 * parameter types are known ahead of time — everything is discovered
 * through reflection.
 */
public final class MethodInvoker {

    private MethodInvoker() {
    }

    public static List<InvocationResult> invokeAnnotated(Object target) {
        Objects.requireNonNull(target, "Target object cannot be null");

        List<InvocationResult> results = new ArrayList<>();

        for (Method method : target.getClass().getDeclaredMethods()) {
            InvokeTimes annotation = method.getAnnotation(InvokeTimes.class);
            if (annotation == null || !isProtectedOrPrivate(method)) {
                continue;
            }

            method.setAccessible(true);
            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int callIndex = 0; callIndex < annotation.value(); callIndex++) {
                Object[] arguments = buildArguments(parameterTypes, callIndex);
                Object returnValue = invoke(target, method, arguments);
                results.add(new InvocationResult(method.getName(), callIndex + 1, returnValue));
            }
        }

        return results;
    }

    private static boolean isProtectedOrPrivate(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
    }

    private static Object[] buildArguments(Class<?>[] parameterTypes, int callIndex) {
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = ArgumentFactory.create(parameterTypes[i], callIndex);
        }
        return arguments;
    }

    private static Object invoke(Object target, Method method, Object[] arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Failed to invoke " + method.getName() + " via reflection", e);
        }
    }
}