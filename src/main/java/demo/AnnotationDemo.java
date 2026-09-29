package demo;

import domain.Robot;
import invoker.InvocationResult;
import invoker.MethodInvoker;

import java.util.List;

/** Console entry point: prints results of direct and reflective calls. */
public final class AnnotationDemo {

    public static void main(String[] args) {
        System.out.println("---- Annotation-Driven Invocation Demo ----");

        Robot robot = new Robot("R2D2");

        System.out.println("\n-- Direct calls to public methods: --");
        System.out.println(robot.getName());
        System.out.println(robot.charge(15));
        System.out.println(robot.describe(true));

        System.out.println("\n-- Reflective calls to annotated protected/private methods: --");
        List<InvocationResult> results = MethodInvoker.invokeAnnotated(robot);
        for (InvocationResult result : results) {
            System.out.printf("%s (call #%d): %s%n",
                    result.methodName(), result.callNumber(), result.returnValue());
        }
    }
}