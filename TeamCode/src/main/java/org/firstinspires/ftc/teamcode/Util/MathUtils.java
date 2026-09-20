package org.firstinspires.ftc.teamcode.Util;

public final class MathUtils {

    private MathUtils() {
    }


    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static double angleWrap(double radians) {
        double angle = radians % (2.0 * Math.PI);
        // aducem restul în (-PI, PI]
        while (angle <= -Math.PI) {
            angle += 2.0 * Math.PI;
        }
        while (angle > Math.PI) {
            angle -= 2.0 * Math.PI;
        }
        return angle;
    }

    public static double applyDeadband(double value, double deadband) {
        if (Math.abs(value) < deadband) {
            return 0.0;
        }
        // re-scalare liniară: la |value| = deadband ieșirea e 0, la |value| = 1 ieșirea e ±1
        double scaled = (Math.abs(value) - deadband) / (1.0 - deadband);
        return Math.signum(value) * clamp(scaled, 0.0, 1.0);
    }
}
