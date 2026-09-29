package invoker;

import annotation.InvokeTimes;

/**
 * Fixture with a parameter type that has no no-args constructor,
 * used to verify the invoker fails loudly instead of passing null.
 */
@SuppressWarnings("unused")
class BrokenService {

    @InvokeTimes(1)
    private String unsupportedType(Runnable action) {
        return "should not run: " + action;
    }
}