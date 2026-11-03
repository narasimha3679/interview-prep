package com.framework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Partial response mapping for ReqRes create/update user responses.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private String name;
    private String job;
    /** JSONPlaceholder returns numeric id; ReqRes may return string. */
    private Object id;
    private String createdAt;
    private String updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
