package invoker;

/** Fixture whose only constructor requires an instance of itself, used to verify cycle detection. */
final class SelfReferencing {

    SelfReferencing(SelfReferencing other) {
    }
}