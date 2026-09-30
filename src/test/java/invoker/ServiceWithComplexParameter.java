package invoker;

import annotation.InvokeTimes;

/** Fixture whose annotated method takes a type with no no-args constructor. */
@SuppressWarnings("unused")
class ServiceWithComplexParameter {

    @InvokeTimes(2)
    private String locate(Point point) {
        return "Located at " + point;
    }
}