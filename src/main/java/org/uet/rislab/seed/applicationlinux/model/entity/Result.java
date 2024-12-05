package org.uet.rislab.seed.applicationlinux.model.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Result {
    private String resultId;
    private Date analysisDate;
    private Map<String, Double> measurements;
    private Seed seed;
    private Project project;

    public Result() {
        this.measurements = new HashMap<>();
    }

}
