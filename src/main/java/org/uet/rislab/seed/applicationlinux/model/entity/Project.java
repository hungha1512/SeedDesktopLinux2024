package org.uet.rislab.seed.applicationlinux.model.entity;

import org.uet.rislab.seed.applicationlinux.model.enums.EAwns;
import org.uet.rislab.seed.applicationlinux.model.enums.EColor;
import org.uet.rislab.seed.applicationlinux.model.enums.ESeedType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private String projectId;
    private String name;
    private Date creationDate;
    private String description;
    private String imageFolderPath;
    private String analysisDataPath;
    private String resultCsvPath;
    private List<Seed> seeds;

    private ESeedType eSeedType;
    private EAwns eAwns;
    private EColor eColor;

    public Project() {
        this.seeds = new ArrayList<>();
    }

    public Project(String projectId, String name, Date creationDate, String description, String imageFolderPath,
                   String analysisDataPath, String resultCsvPath, List<Seed> seeds) {
        this.projectId = projectId;
        this.name = name;
        this.creationDate = creationDate;
        this.description = description;
        this.imageFolderPath = imageFolderPath;
        this.analysisDataPath = analysisDataPath;
        this.resultCsvPath = resultCsvPath;
        this.seeds = seeds;
        this.eAwns = EAwns.PRESENT;
        this.eColor = EColor.YELLOW;
        this.eSeedType = ESeedType.LONG;
    }

    public Project(String projectId, String name, Date creationDate, String description, String imageFolderPath,
                   String analysisDataPath, String resultCsvPath, List<Seed> seeds, ESeedType eSeedType, EAwns eAwns,
                   EColor eColor) {
        this.projectId = projectId;
        this.name = name;
        this.creationDate = creationDate;
        this.description = description;
        this.imageFolderPath = imageFolderPath;
        this.analysisDataPath = analysisDataPath;
        this.resultCsvPath = resultCsvPath;
        this.seeds = seeds;
        this.eSeedType = eSeedType;
        this.eAwns = eAwns;
        this.eColor = eColor;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFolderPath() {
        return imageFolderPath;
    }

    public void setImageFolderPath(String imageFolderPath) {
        this.imageFolderPath = imageFolderPath;
    }

    public String getAnalysisDataPath() {
        return analysisDataPath;
    }

    public void setAnalysisDataPath(String analysisDataPath) {
        this.analysisDataPath = analysisDataPath;
    }

    public String getResultCsvPath() {
        return resultCsvPath;
    }

    public void setResultCsvPath(String resultCsvPath) {
        this.resultCsvPath = resultCsvPath;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    public ESeedType geteSeedType() {
        return eSeedType;
    }

    public void seteSeedType(ESeedType eSeedType) {
        this.eSeedType = eSeedType;
    }

    public EAwns geteAwns() {
        return eAwns;
    }

    public void seteAwns(EAwns eAwns) {
        this.eAwns = eAwns;
    }

    public EColor geteColor() {
        return eColor;
    }

    public void seteColor(EColor eColor) {
        this.eColor = eColor;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                ", eAwns=" + eAwns +
                ", eSeedType=" + eSeedType +
                ", eColor=" + eColor +
                ", imageFolderPath='" + imageFolderPath + '\'' +
                ", analysisDataPath='" + analysisDataPath + '\'' +
                ", resultCsvPath='" + resultCsvPath + '\'' +
                ", seeds=" + seeds +
                '}';
    }
}
