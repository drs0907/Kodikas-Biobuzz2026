package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.Util.MathUtils;


public class Intake implements Subsystem {

    private final DcMotorEx motor;
    private double power = 0.0;

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.INTAKE);
        motor.setDirection(RobotConfig.IntakeConstants.DIRECTION);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void collect() {
        setPower(RobotConfig.IntakeConstants.COLLECT_POWER);
    }

    public void eject() {
        setPower(RobotConfig.IntakeConstants.EJECT_POWER);
    }

    public void setPower(double power) {
        this.power = MathUtils.clamp(power, -1.0, 1.0);
        motor.setPower(this.power);
    }

    public double getPower() {
        return power;
    }

    @Override
    public void periodic() {
        // gol intenționat
    }

    @Override
    public void stop() {
        setPower(0.0);
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Intake putere", "%.2f", power);
    }
}
