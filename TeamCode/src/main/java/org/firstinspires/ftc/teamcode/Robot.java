package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import org.firstinspires.ftc.teamcode.Subsystems.Localizer;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Transfer;

import java.util.ArrayList;
import java.util.List;


public class Robot {

    public final Localizer localizer;

    public final Drivetrain drivetrain;

    public final Intake intake;

    public final Transfer transfer;

    public final Launcher launcher;

    private final List<Subsystem> subsystems;

    private final List<LynxModule> hubs;

    public Robot(HardwareMap hardwareMap) {
        // Bulk caching MANUAL: citirile hardware dintr-o iterație vin dintr-un
        // singur transfer per hub; cache-ul se golește în periodic().
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // Construim subsistemele în ordinea: localizer, drivetrain, intake, transfer, launcher.
        localizer = new Localizer(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        launcher = new Launcher(hardwareMap);

        subsystems = new ArrayList<Subsystem>();
        subsystems.add(localizer);
        subsystems.add(drivetrain);
        subsystems.add(intake);
        subsystems.add(transfer);
        subsystems.add(launcher);
    }

    public void periodic() {
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }
        for (Subsystem subsystem : subsystems) {
            subsystem.periodic();
        }
    }

    /**
     * Oprește toate actuatoarele — de apelat la finalul OpMode-ului
     * sau în situații de siguranță.
     */
    public void stop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.stop();
        }
    }

    /**
     * Adaugă în telemetrie datele relevante ale tuturor subsistemelor.
     * Apelantul rămâne responsabil de {@code telemetry.update()}.
     *
     * @param telemetry obiectul de telemetrie al OpMode-ului
     */
    public void addTelemetry(Telemetry telemetry) {
        for (Subsystem subsystem : subsystems) {
            subsystem.addTelemetry(telemetry);
        }
    }
}
