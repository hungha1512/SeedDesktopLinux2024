package org.uet.rislab.seed.applicationlinux.model.entity;

import org.uet.rislab.seed.applicationlinux.model.enums.EAwns;
import org.uet.rislab.seed.applicationlinux.model.enums.EColor;

public class Seed {
    private double length;
    private double width;
    private double ratioLengthAndWidth;
    private String imagePath;
    private Project project;

    public Seed() {
        ;
    }

    public Seed(double length, double width, String imagePath, Project project) {
        this.length = length;
        this.width = width;
        this.ratioLengthAndWidth = 0.000000001;
        this.imagePath = imagePath;
        this.project = project;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getRatioLengthAndWidth() {
        return ratioLengthAndWidth;
    }

    public void setRatioLengthAndWidth(double ratioLengthAndWidth) {
        this.ratioLengthAndWidth = ratioLengthAndWidth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Seed{" +
                "length=" + length +
                ", width=" + width +
                ", ratioLengthAndWidth=" + ratioLengthAndWidth +
                '}';
    }
}
