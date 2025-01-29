package com.appviewx.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,include = JsonTypeInfo.As.PROPERTY,property = "@class")
public class LuaResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value = "tenantId")
    String tenantId;
    @JsonProperty(value = "score")
    Integer score;

    public LuaResponse() {
    }

    public LuaResponse(String tenantId, Integer score) {
        this.tenantId = tenantId;
        this.score = score;
    }

    @Override
    public String toString() {
        return "LuaResponse{" +
                "tenantId='" + tenantId + '\'' +
                ", score=" + score +
                '}';
    }
}
