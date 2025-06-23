package com.vazzarmoviedb.backend.model.entity;

public enum Jobs {
    DIRECTOR("Director");

    private final String jobTitle;

    Jobs(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getJobTitle() {
        return jobTitle;
    }
}
