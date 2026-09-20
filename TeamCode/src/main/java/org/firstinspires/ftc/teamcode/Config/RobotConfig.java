package org.firstinspires.ftc.teamcode.Config;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public final class RobotConfig{
    private RobotConfig() {}

    public static final class HardwareNames{
        public static final String LEFT_FRONT = "left_front";
        public static final String LEFT_BACK = "left_rear";
        public static final String RIGHT_FRONT = "right_front";
        public static final String RIGHT_BACK = "right_rear";
        public static final String INTAKE = "intake";
        public static final String TRANSFER = "transfer";
        public static final String LAUNCHER1 = "launcher1";
        public static final String LAUNCHER2 = "launcher2";
        public static final String PINPOINT = "pinpoint";
    }
    public static final class DriveConstants {
        public static final DcMotorSimple.Direction LEFT_FRONT_DIRECTION = DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction LEFT_BACK_DIRECTION = DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction RIGHT_FRONT_DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static final DcMotorSimple.Direction RIGHT_BACK_DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static final double TICKS_PER_REV = 384.5;
        public static final double MAX_RPM = 435.0;
    }
    public static final class TeleOpConstants {
        public static double DRIVE_SPEED = 1.0;
        public static double TURN_SPEED = 0.9;
        public static double STICK_DEADBAND = 0.05;
        public static boolean FIELD_CENTRIC_DEFAULT = true;
    }
    public static final class LocalizerConstants {
        public static final DistanceUnit DISTANCE_UNIT = DistanceUnit.CM;

        public static double X_POD_OFFSET = -16.7;
        public static double Y_POD_OFFSET = 0.95;
        public static final boolean USE_GOBILDA_PODS = true;
        public static final GoBildaPinpointDriver.GoBildaOdometryPods POD_TYPE =
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        public static double CUSTOM_TICKS_PER_CM = 0.0;
        public static final GoBildaPinpointDriver.EncoderDirection X_DIRECTION =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;
        public static final GoBildaPinpointDriver.EncoderDirection Y_DIRECTION =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;
    }
    public static final class IntakeConstants {
        public static final DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static double COLLECT_POWER = 1.0;
        public static double EJECT_POWER = -0.6;
    }
    public static final class TransferConstants {
        public static final DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static double FEED_POWER = 0.5;
        public static double REVERSE_POWER = -0.5;
        public static final double TICKS_PER_REV = 384.5;

    }

    public static final class LauncherConstants {
        public static final DcMotorSimple.Direction DIRECTION1 = DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction DIRECTION2 = DcMotorSimple.Direction.FORWARD;
        public static final double TICKS_PER_REV = 28.0;
        public static final double MAX_RPM = 6000.0;
        public static double TARGET_RPM = 5500.0;
        public static double RPM_TOLERANCE = 50.0;
        public static double VELOCITY_P = 0.0;
        public static double VELOCITY_I = 0.0;
        public static double VELOCITY_D = 0.0;
        public static double VELOCITY_F = 0.0;
    }

}
