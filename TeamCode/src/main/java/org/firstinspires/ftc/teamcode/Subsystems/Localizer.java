package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.Config.RobotConfig;
import org.firstinspires.ftc.teamcode.Util.Pose2d;

public class Localizer implements Subsystem {

    private final GoBildaPinpointDriver pinpoint;
    private Pose2d lastKnownPose;

    public Localizer(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,
                RobotConfig.HardwareNames.PINPOINT);

        pinpoint.setOffsets(RobotConfig.LocalizerConstants.X_POD_OFFSET,
                RobotConfig.LocalizerConstants.Y_POD_OFFSET,
                RobotConfig.LocalizerConstants.DISTANCE_UNIT);

        if (RobotConfig.LocalizerConstants.USE_GOBILDA_PODS) {
            pinpoint.setEncoderResolution(RobotConfig.LocalizerConstants.POD_TYPE);
        } else {
            pinpoint.setEncoderResolution(RobotConfig.LocalizerConstants.CUSTOM_TICKS_PER_CM,
                    RobotConfig.LocalizerConstants.DISTANCE_UNIT);
        }

        pinpoint.setEncoderDirections(RobotConfig.LocalizerConstants.X_DIRECTION,
                RobotConfig.LocalizerConstants.Y_DIRECTION);

        pinpoint.resetPosAndIMU();
        pinpoint.update();
        lastKnownPose = getPose();
    }

    @Override
    public void periodic() {
        pinpoint.update();
        lastKnownPose = getPose();
    }

    public Pose2d getPose() {
        return new Pose2d(
                pinpoint.getPosX(RobotConfig.LocalizerConstants.DISTANCE_UNIT),
                pinpoint.getPosY(RobotConfig.LocalizerConstants.DISTANCE_UNIT),
                pinpoint.getHeading(AngleUnit.RADIANS));
    }

    public Pose2d getVelocity() {
        return new Pose2d(
                pinpoint.getVelX(RobotConfig.LocalizerConstants.DISTANCE_UNIT),
                pinpoint.getVelY(RobotConfig.LocalizerConstants.DISTANCE_UNIT),
                pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS));
    }

    public void setPose(Pose2d pose) {
        pinpoint.setPosition(new Pose2D(RobotConfig.LocalizerConstants.DISTANCE_UNIT,
                pose.x, pose.y, AngleUnit.RADIANS, pose.heading));
    }

    public void resetHeading() {
        pinpoint.setHeading(0, AngleUnit.RADIANS);
    }

    public void resetPoseAndImu() {
        pinpoint.resetPosAndIMU();
    }

    public GoBildaPinpointDriver.DeviceStatus getStatus() {
        return pinpoint.getDeviceStatus();
    }


    @Override
    public void stop() {
        // Localizer doesn't need stopping
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {
        Pose2d pose = getPose();
        Pose2d velocity = getVelocity();
        telemetry.addData("Localizare | Pose", pose.toString());
        telemetry.addData("Localizare | Heading (grade)", String.format("%.1f", Math.toDegrees(pose.heading)));
        telemetry.addData("Localizare | Viteza (cm/s)", String.format("%.1f", Math.hypot(velocity.x, velocity.y)));
        telemetry.addData("Localizare | Status", getStatus());
        telemetry.addData("Localizare | Frecventa (Hz)", String.format("%.0f", pinpoint.getFrequency()));
    }
}
