package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.Util.MathUtils;

@TeleOp(name = "Main TeleOp", group = "Competition")
public class MainTeleOp extends LinearOpMode {

    private Robot robot;
    private boolean fieldCentric = RobotConfig.TeleOpConstants.FIELD_CENTRIC_DEFAULT;
    private boolean launcherActive = false;
    private Gamepad gamepadPrev = new Gamepad();

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            // Update robot state
            robot.periodic();

            // Drivetrain control
            double axial = -gamepad1.left_stick_y;  // forward/backward
            double lateral = gamepad1.left_stick_x;  // strafe
            double yaw = gamepad1.right_stick_x;     // rotation

            // Apply deadband
            axial = MathUtils.applyDeadband(axial, RobotConfig.TeleOpConstants.STICK_DEADBAND);
            lateral = MathUtils.applyDeadband(lateral, RobotConfig.TeleOpConstants.STICK_DEADBAND);
            yaw = MathUtils.applyDeadband(yaw, RobotConfig.TeleOpConstants.STICK_DEADBAND);

            // Scale speeds
            axial *= RobotConfig.TeleOpConstants.DRIVE_SPEED;
            lateral *= RobotConfig.TeleOpConstants.DRIVE_SPEED;
            yaw *= RobotConfig.TeleOpConstants.TURN_SPEED;

            // Drive
            if (fieldCentric) {
                robot.drivetrain.driveFieldCentric(lateral, axial, yaw, 
                        robot.localizer.getPose().heading);
            } else {
                robot.drivetrain.driveRobotCentric(axial, lateral, yaw);
            }

            // Toggle field-centric mode
            if (gamepad1.y && !gamepadPrev.y) {
                fieldCentric = !fieldCentric;
            }

            // Intake control
            if (gamepad2.right_trigger > 0.5) {
                robot.intake.collect();
            } else if (gamepad2.left_trigger > 0.5) {
                robot.intake.eject();
            } else {
                robot.intake.stop();
            }

            // Transfer control
            if (gamepad2.right_bumper) {
                robot.transfer.feed();
            } else if (gamepad2.left_bumper) {
                robot.transfer.reverse();
            } else {
                robot.transfer.stop();
            }

            // Launcher control
            if (gamepad2.y && !gamepadPrev.y) {
                launcherActive = !launcherActive;
            }

            if (launcherActive) {
                robot.launcher.spinUp();
            } else {
                robot.launcher.stop();
            }

            // Reset localizer
            if (gamepad1.guide) {
                robot.localizer.resetPoseAndImu();
            }

            // Telemetry
            telemetry.addLine("=== FIELD STATE ===");
            telemetry.addData("Mode", fieldCentric ? "FIELD CENTRIC" : "ROBOT CENTRIC");
            telemetry.addData("Launcher", launcherActive ? "ACTIVE" : "IDLE");
            telemetry.addLine();
            robot.addTelemetry(telemetry);
            telemetry.addLine();
            telemetry.addLine("CONTROLS: Y = Toggle Field-Centric | Guide = Reset Localizer");
            telemetry.update();

            // Update previous gamepad state
            gamepadPrev.copy(gamepad1);
            gamepadPrev.copy(gamepad2);
        }

        robot.stop();
    }
}
