package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Subsystem {
    void periodic();
    void stop();
    void addTelemetry(Telemetry telemetry);
}
