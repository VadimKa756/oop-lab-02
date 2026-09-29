package domain;

import annotation.InvokeTimes;

import java.util.Objects;

/**
 * Sample domain class with public, protected and private methods of
 * varying arity and parameter types, used to demonstrate reflective
 * invocation of annotated members.
 */
public final class Robot {

    private String name;
    private int batteryPercent;
    private final boolean operational;

    public Robot(String name) {
        this.name = Objects.requireNonNull(name, "Robot name cannot be null");
        this.batteryPercent = 100;
        this.operational = true;
    }

    // ---- public methods ----

    public String getName() {
        return name;
    }

    public String charge(int percent) {
        batteryPercent = Math.min(100, batteryPercent + percent);
        return "Battery charged to " + batteryPercent + "%";
    }

    public String describe(boolean verbose) {
        if (!verbose) {
            return name;
        }
        return name + " (battery=" + batteryPercent + "%, operational=" + operational + ")";
    }

    // ---- protected methods (annotated) ----

    @InvokeTimes(2)
    protected String move(int steps) {
        return "Moved " + steps + " step(s)";
    }

    @InvokeTimes(1)
    protected String rename(String newName) {
        this.name = newName;
        return "Renamed to '" + newName + "'";
    }

    @InvokeTimes(1)
    protected String calibrate(double factor, boolean verbose) {
        return "Calibrated with factor=" + factor + ", verbose=" + verbose;
    }

    // ---- private methods (annotated) ----

    @InvokeTimes(3)
    private String selfDiagnose() {
        return "Self-diagnostics: OK";
    }

    @InvokeTimes(1)
    private String logEvent(String event, int severity) {
        return "Logged '" + event + "' with severity " + severity;
    }

    @InvokeTimes(2)
    private String adjustSpeed(double delta) {
        return "Speed adjusted by " + delta;
    }
}