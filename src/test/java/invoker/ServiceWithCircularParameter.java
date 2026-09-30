package invoker;

import annotation.InvokeTimes;

/** Fixture whose annotated method takes a type with a circular constructor dependency. */
@SuppressWarnings("unused")
class ServiceWithCircularParameter {

    @InvokeTimes(1)
    private String process(SelfReferencing value) {
        return "should not run: " + value;
    }
}