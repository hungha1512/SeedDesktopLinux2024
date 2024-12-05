package org.uet.rislab.seed.applicationlinux.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Image {
    private String imageId;
    private String filePath;
    private Date capturedDate;
    private List<Seed> seeds;
    private Project project;

    public Image() {
        this.seeds = new ArrayList<>();
    }

    public Image(String imageId, String filePath, Date capturedDate, List<Seed> seeds, Project project) {
        this.imageId = imageId;
        this.filePath = filePath;
        this.capturedDate = capturedDate;
        this.seeds = seeds;
        this.project = project;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCapturedDate() {
        return capturedDate;
    }

    public void setCapturedDate(Date capturedDate) {
        this.capturedDate = capturedDate;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId='" + imageId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", capturedDate=" + capturedDate +
                ", seeds=" + seeds +
                ", project=" + project +
                '}';
    }
}
