package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.Util.MathUtils;

public class Launcher implements Subsystem {

    private final DcMotorEx motor1;
    private final DcMotorEx motor2;

    private double targetRpm = 0.0;


    public Launcher(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LAUNCHER1);
        motor2 = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LAUNCHER2);
        
        motor1.setDirection(RobotConfig.LauncherConstants.DIRECTION1);
        motor2.setDirection(RobotConfig.LauncherConstants.DIRECTION2);
        
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        motor1.setVelocityPIDFCoefficients(
                RobotConfig.LauncherConstants.VELOCITY_P,
                RobotConfig.LauncherConstants.VELOCITY_I,
                RobotConfig.LauncherConstants.VELOCITY_D,
                RobotConfig.LauncherConstants.VELOCITY_F);
        motor2.setVelocityPIDFCoefficients(
                RobotConfig.LauncherConstants.VELOCITY_P,
                RobotConfig.LauncherConstants.VELOCITY_I,
                RobotConfig.LauncherConstants.VELOCITY_D,
                RobotConfig.LauncherConstants.VELOCITY_F);
        
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void spinUp() {
        setTargetRpm(RobotConfig.LauncherConstants.TARGET_RPM);
    }


    public void setTargetRpm(double rpm) {
        targetRpm = MathUtils.clamp(rpm, 0.0, RobotConfig.LauncherConstants.MAX_RPM);
        if (targetRpm > 0.0) {
            motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // conversie RPM -> ticks/secundă
            double ticksPerSecond = targetRpm / 60.0 * RobotConfig.LauncherConstants.TICKS_PER_REV;
            motor1.setVelocity(ticksPerSecond);
            motor2.setVelocity(ticksPerSecond);
        } else {
            motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    public double getTargetRpm() {
        return targetRpm;
    }

    public double getCurrentRpm1() {
        return motor1.getVelocity() / RobotConfig.LauncherConstants.TICKS_PER_REV * 60.0;
    }

    public double getCurrentRpm2() {
        return motor2.getVelocity() / RobotConfig.LauncherConstants.TICKS_PER_REV * 60.0;
    }

    public double getAverageRpm() {
        return (getCurrentRpm1() + getCurrentRpm2()) / 2.0;
    }

    public boolean isAtSpeed() {
        return targetRpm > 0.0
                && Math.abs(getCurrentRpm1() - targetRpm) < RobotConfig.LauncherConstants.RPM_TOLERANCE
                && Math.abs(getCurrentRpm2() - targetRpm) < RobotConfig.LauncherConstants.RPM_TOLERANCE;
    }

    @Override
    public void periodic() {
        // gol intenționat
    }

    @Override
    public void stop() {
        setTargetRpm(0.0);
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Launcher target RPM", "%.0f", targetRpm);
        telemetry.addData("Launcher RPM motor1", "%.0f", getCurrentRpm1());
        telemetry.addData("Launcher RPM motor2", "%.0f", getCurrentRpm2());
        telemetry.addData("Launcher RPM avg", "%.0f", getAverageRpm());
        telemetry.addData("Launcher la viteza", isAtSpeed());
    }
}
