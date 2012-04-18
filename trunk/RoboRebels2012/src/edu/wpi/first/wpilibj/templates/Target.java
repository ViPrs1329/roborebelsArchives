package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class Target {

    private ParticleAnalysisReport report;
    private RRTracker tracker;

    Target(ParticleAnalysisReport report) {
        this.report = report;
    }

    public double shootingAngle() {
        return 0;
    }

    public double distance() {
        return 801.4 / report.boundingRectWidth;
    }

    public int height() {
        return report.boundingRectHeight;
    }

    public int width() {
        return report.boundingRectWidth;
    }

    public int posY() {
        //       return report.center_mass_y;
        return tracker.y(report.center_mass_y);
    }

    public int posX() {
        return tracker.x(report.center_mass_x, width());
    }
}
