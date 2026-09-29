package invoker;

import annotation.InvokeTimes;

/**
 * Minimal fixture with only synthesizable parameter types, used to
 * verify call counts in isolation from {@code domain.Robot}.
 */
@SuppressWarnings("unused")
class SampleService {

    @InvokeTimes(3)
    private String ping() {
        return "pong";
    }

    @InvokeTimes(2)
    protected String sum(int a, int b) {
        return "sum=" + (a + b);
    }

    public String notInvoked() {
        return "should not appear";
    }
}