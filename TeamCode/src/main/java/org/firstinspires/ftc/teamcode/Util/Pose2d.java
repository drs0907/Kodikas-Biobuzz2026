package org.firstinspires.ftc.teamcode.Util;

import org.firstinspires.ftc.teamcode.Util.MathUtils;

import java.util.Locale;

public final class Pose2d {
    public final double x;

    public final double y;
    public final double heading;

    public Pose2d(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    /** Creează poziția de origine (0, 0, 0). */
    public Pose2d() {
        this(0.0, 0.0, 0.0);
    }
    public double distanceTo(Pose2d other) {
        return Math.hypot(other.x - this.x, other.y - this.y);
    }

    public double headingErrorTo(double targetHeading) {
        return MathUtils.angleWrap(targetHeading - heading);
    }

    public Pose2d withHeading(double newHeading) {
        return new Pose2d(x, y, newHeading);
    }
    public static Pose2d fromDegrees(double x, double y, double headingDeg) {
        return new Pose2d(x, y, Math.toRadians(headingDeg));
    }
    @Override
    public String toString() {
        return String.format(Locale.US, "x=%.1f, y=%.1f, h=%.1f°", x, y, Math.toDegrees(heading));
    }
}
