package invoker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodInvokerTest {

    @Test
    void invokesEachAnnotatedMethodExactlyAsManyTimesAsSpecified() {
        List<InvocationResult> results = MethodInvoker.invokeAnnotated(new SampleService());

        long pingCalls = results.stream().filter(r -> r.methodName().equals("ping")).count();
        long sumCalls = results.stream().filter(r -> r.methodName().equals("sum")).count();

        assertEquals(3, pingCalls);
        assertEquals(2, sumCalls);
    }

    @Test
    void ignoresPublicMethodsEvenIfAnnotatedTargetHasThem() {
        List<InvocationResult> results = MethodInvoker.invokeAnnotated(new SampleService());

        assertTrue(results.stream().noneMatch(r -> r.methodName().equals("notInvoked")));
    }

    @Test
    void rejectsNullTarget() {
        assertThrows(NullPointerException.class, () -> MethodInvoker.invokeAnnotated(null));
    }

    @Test
    void throwsWhenParameterTypeHasNoConstructorAtAll() {
        assertThrows(IllegalArgumentException.class,
                () -> MethodInvoker.invokeAnnotated(new BrokenService()));
    }

    @Test
    void synthesizesArgumentsForTypesWithoutNoArgsConstructorViaTheirConstructorParameters() {
        List<InvocationResult> results = MethodInvoker.invokeAnnotated(new ServiceWithComplexParameter());

        assertEquals(2, results.size());
        results.forEach(r -> assertTrue(r.returnValue().toString().startsWith("Located at Point(")));
    }

    @Test
    void throwsInsteadOfInfiniteRecursionOnCircularConstructorDependency() {
        assertThrows(IllegalArgumentException.class,
                () -> MethodInvoker.invokeAnnotated(new ServiceWithCircularParameter()));
    }
}