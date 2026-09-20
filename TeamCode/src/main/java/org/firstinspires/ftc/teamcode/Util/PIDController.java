package org.firstinspires.ftc.teamcode.Util;

public class PIDController {

    private double kP;

    private double kI;

    private double kD;

    private double maxIntegral = 1.0;

    private double integral = 0.0;

    private double lastError = 0.0;

    private long lastTimeNanos = 0L;

    private boolean firstRun = true;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }


    public double calculate(double error) {
        long now = System.nanoTime();
        double dt = firstRun ? 0.0 : (now - lastTimeNanos) / 1e9;
        double output;

        if (firstRun || dt <= 0.0 || dt > 0.5) {
            // prim apel (sau pauză anormală): doar termenul P, fără acumulare
            output = kP * error;
        } else {
            // termenul integral, cu anti-windup
            integral = MathUtils.clamp(integral + error * dt, -maxIntegral, maxIntegral);
            // termenul derivativ pe eroare
            double derivative = (error - lastError) / dt;
            output = kP * error + kI * integral + kD * derivative;
        }

        lastError = error;
        lastTimeNanos = now;
        firstRun = false;
        return output;
    }

    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }


    public void setMaxIntegral(double maxIntegral) {
        this.maxIntegral = Math.abs(maxIntegral);
        // re-limitează integrala deja acumulată la noua limită
        this.integral = MathUtils.clamp(this.integral, -this.maxIntegral, this.maxIntegral);
    }

    public void reset() {
        integral = 0.0;
        lastError = 0.0;
        lastTimeNanos = 0L;
        firstRun = true;
    }
}
