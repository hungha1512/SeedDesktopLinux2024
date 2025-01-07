package org.uet.rislab.seed.applicationlinux.model.entity;

public class Result {
    private final int id;
    private final double height;
    private final double width;

    public Result(int id, double height, double width) {
        this.id = id;
        this.height = height;
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}


