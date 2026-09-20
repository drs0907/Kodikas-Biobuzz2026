package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;

public class Drivetrain implements Subsystem {
    private final DcMotorEx leftFront;
    private final DcMotorEx leftBack;
    private final DcMotorEx rightFront;
    private final DcMotorEx rightBack;
    private double leftFrontPower;
    private double leftBackPower;
    private double rightFrontPower;
    private double rightBackPower;
    public Drivetrain(HardwareMap hardwareMap) {
        leftFront = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LEFT_FRONT);
        leftBack = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.LEFT_BACK);
        rightFront = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.RIGHT_FRONT);
        rightBack = hardwareMap.get(DcMotorEx.class, RobotConfig.HardwareNames.RIGHT_BACK);

        // Directiile din config: motoarele din stanga sunt de obicei inversate
        // ca "putere pozitiva" sa insemne "inainte" pe toate rotile.
        leftFront.setDirection(RobotConfig.DriveConstants.LEFT_FRONT_DIRECTION);
        leftBack.setDirection(RobotConfig.DriveConstants.LEFT_BACK_DIRECTION);
        rightFront.setDirection(RobotConfig.DriveConstants.RIGHT_FRONT_DIRECTION);
        rightBack.setDirection(RobotConfig.DriveConstants.RIGHT_BACK_DIRECTION);

        // BRAKE: robotul se opreste ferm cand puterea e zero (util in TeleOp).
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // RUN_WITHOUT_ENCODER: localizarea vine de la Pinpoint, nu de la encoderele rotilor.
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void driveRobotCentric(double axial, double lateral, double yaw) {
        // Mixul mecanum din SPEC: fiecare roata combina cele trei componente.
        double lf = axial - lateral - yaw;
        double lb = axial + lateral - yaw;
        double rf = axial + lateral + yaw;
        double rb = axial - lateral + yaw;

        // Normalizare: daca vreo putere depaseste 1, impartim totul la maxim
        // ca sa pastram proportiile intre roti.
        double max = Math.max(1.0, Math.max(Math.abs(lf),
                Math.max(Math.abs(lb), Math.max(Math.abs(rf), Math.abs(rb)))));

        setMotorPowers(lf / max, lb / max, rf / max, rb / max);
    }

    public void driveFieldCentric(double vxField, double vyField, double yaw, double headingRad) {
        // Rotire cu -heading: trecem comanda din cadrul terenului in cadrul robotului.
        double cosH = Math.cos(-headingRad);
        double sinH = Math.sin(-headingRad);
        double vxR = vxField * cosH - vyField * sinH;
        double vyR = vxField * sinH + vyField * cosH;

        driveRobotCentric(vxR, vyR, yaw);
    }

    @Override
    public void periodic() {
        // gol intentionat
    }

    @Override
    public void stop() {
        setMotorPowers(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Drive LF/LB", "%.2f / %.2f", leftFrontPower, leftBackPower);
        telemetry.addData("Drive RF/RB", "%.2f / %.2f", rightFrontPower, rightBackPower);
    }

    private void setMotorPowers(double lf, double lb, double rf, double rb) {
        leftFrontPower = lf;
        leftBackPower = lb;
        rightFrontPower = rf;
        rightBackPower = rb;
        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
    }
}
