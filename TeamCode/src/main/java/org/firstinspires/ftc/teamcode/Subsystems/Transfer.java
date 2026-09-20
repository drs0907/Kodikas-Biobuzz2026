package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.Util.MathUtils;


public class Transfer implements Subsystem {

    private final DcMotorEx motor;
    private double power = 0.0;

    public Transfer(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.TRANSFER);
        motor.setDirection(RobotConfig.TransferConstants.DIRECTION);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void feed() {
        setMotorPower(RobotConfig.TransferConstants.FEED_POWER);
    }

    public void reverse() {
        setMotorPower(RobotConfig.TransferConstants.REVERSE_POWER);
    }

    public void stop() {
        setMotorPower(0.0);
    }

    private void setMotorPower(double power) {
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
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Transfer putere", "%.2f", power);
    }
}
