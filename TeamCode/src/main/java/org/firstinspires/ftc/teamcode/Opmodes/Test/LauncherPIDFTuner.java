package org.firstinspires.ftc.teamcode.Opmodes.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config.RobotConfig;

@TeleOp(name = "Launcher PIDF Tuner", group = "Tuning")
public class LauncherPIDFTuner extends LinearOpMode {

    private DcMotorEx motor1;
    private DcMotorEx motor2;

    private double kP = RobotConfig.LauncherConstants.VELOCITY_P;
    private double kI = RobotConfig.LauncherConstants.VELOCITY_I;
    private double kD = RobotConfig.LauncherConstants.VELOCITY_D;
    private double kF = RobotConfig.LauncherConstants.VELOCITY_F;

    private double targetRpm = 1000.0;

    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LAUNCHER1);
        motor2 = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LAUNCHER2);

        motor1.setDirection(RobotConfig.LauncherConstants.DIRECTION1);
        motor2.setDirection(RobotConfig.LauncherConstants.DIRECTION2);

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        while (opModeIsActive()) {
            // Update PIDF coefficients
            motor1.setVelocityPIDFCoefficients(kP, kI, kD, kF);
            motor2.setVelocityPIDFCoefficients(kP, kI, kD, kF);

            // Set velocity
            double ticksPerSecond = targetRpm / 60.0 * RobotConfig.LauncherConstants.TICKS_PER_REV;
            motor1.setVelocity(ticksPerSecond);
            motor2.setVelocity(ticksPerSecond);

            // Gamepad controls
            if (gamepad1.dpad_up) {
                targetRpm += 50;
                sleep(200);
            }
            if (gamepad1.dpad_down) {
                targetRpm -= 50;
                sleep(200);
            }
            targetRpm = Math.max(0, Math.min(targetRpm, RobotConfig.LauncherConstants.MAX_RPM));

            // P coefficient
            if (gamepad1.x) {
                kP += 0.01;
                sleep(100);
            }
            if (gamepad1.y) {
                kP -= 0.01;
                sleep(100);
            }
            kP = Math.max(0, kP);

            // I coefficient
            if (gamepad1.a) {
                kI += 0.001;
                sleep(100);
            }
            if (gamepad1.b) {
                kI -= 0.001;
                sleep(100);
            }
            kI = Math.max(0, kI);

            // D coefficient
            if (gamepad1.left_bumper) {
                kD += 0.001;
                sleep(100);
            }
            if (gamepad1.right_bumper) {
                kD -= 0.001;
                sleep(100);
            }
            kD = Math.max(0, kD);

            // F coefficient (feedforward)
            if (gamepad1.left_trigger > 0.5) {
                kF += 0.1;
                sleep(100);
            }
            if (gamepad1.right_trigger > 0.5) {
                kF -= 0.1;
                sleep(100);
            }
            kF = Math.max(0, kF);

            // Reset with menu button
            if (gamepad1.guide || gamepad1.start) {
                kP = 0.0;
                kI = 0.0;
                kD = 0.0;
                kF = 0.0;
                targetRpm = 1000.0;
            }

            // Telemetry
            double rpm1 = motor1.getVelocity() / RobotConfig.LauncherConstants.TICKS_PER_REV * 60.0;
            double rpm2 = motor2.getVelocity() / RobotConfig.LauncherConstants.TICKS_PER_REV * 60.0;
            double avgRpm = (rpm1 + rpm2) / 2.0;

            telemetry.addLine("=== LAUNCHER PIDF TUNER ===");
            telemetry.addLine();
            telemetry.addLine("TARGET RPM: " + (int)targetRpm + " (dpad up/down)");
            telemetry.addLine();
            telemetry.addData("Motor 1 RPM", "%.0f", rpm1);
            telemetry.addData("Motor 2 RPM", "%.0f", rpm2);
            telemetry.addData("Average RPM", "%.0f", avgRpm);
            telemetry.addLine();
            telemetry.addData("P (X/Y)", "%.4f", kP);
            telemetry.addData("I (A/B)", "%.4f", kI);
            telemetry.addData("D (LB/RB)", "%.4f", kD);
            telemetry.addData("F (LT/RT)", "%.2f", kF);
            telemetry.addLine();
            telemetry.addLine("GUIDE: Reset all to 0");
            telemetry.addLine();
            telemetry.addLine("Final values to copy to RobotConfig:");
            telemetry.addLine("  VELOCITY_P = " + String.format("%.4f", kP) + ";");
            telemetry.addLine("  VELOCITY_I = " + String.format("%.4f", kI) + ";");
            telemetry.addLine("  VELOCITY_D = " + String.format("%.4f", kD) + ";");
            telemetry.addLine("  VELOCITY_F = " + String.format("%.2f", kF) + ";");
            telemetry.update();
        }

        motor1.setPower(0.0);
        motor2.setPower(0.0);
    }
}
