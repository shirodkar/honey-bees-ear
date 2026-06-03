package com.example.honeybees.web;

import java.util.List;

public class FactCategory {

    private String name;
    private List<BeeFact> facts;

    public FactCategory(String name, List<BeeFact> facts) {
        this.name = name;
        this.facts = facts;
    }

    public String getName() {
        return name;
    }

    public List<BeeFact> getFacts() {
        return facts;
    }
}
