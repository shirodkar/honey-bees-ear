package com.example.honeybees.web;

import java.util.List;

public class BeeRole {

    private String name;
    private String description;
    private List<String> responsibilities;
    private int populationPercentage;

    public BeeRole() {
    }

    public BeeRole(String name, String description, List<String> responsibilities, int populationPercentage) {
        this.name = name;
        this.description = description;
        this.responsibilities = responsibilities;
        this.populationPercentage = populationPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public int getPopulationPercentage() {
        return populationPercentage;
    }

    public void setPopulationPercentage(int populationPercentage) {
        this.populationPercentage = populationPercentage;
    }
}
