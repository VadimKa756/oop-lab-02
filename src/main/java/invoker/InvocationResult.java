package invoker;

/**
 * Outcome of a single reflective method call: which method, which
 * repetition (1-based), and what it returned.
 */
public record InvocationResult(String methodName, int callNumber, Object returnValue) {
}