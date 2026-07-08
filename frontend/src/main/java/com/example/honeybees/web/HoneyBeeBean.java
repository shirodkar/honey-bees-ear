package com.example.honeybees.web;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("honeyBeeBean")
@RequestScoped
public class HoneyBeeBean {

    private static final Logger LOGGER = Logger.getLogger(HoneyBeeBean.class.getName());
    private static final String DEFAULT_API_URL = "http://localhost:8080/api/api/bees";

    private String description;
    private List<BeeRole> roles;
    private List<FactCategory> factCategories;
    private Client client;
    private String apiBaseUrl;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        apiBaseUrl = getConfiguredApiUrl();
        LOGGER.info("Using backend API base URL: " + apiBaseUrl);
        loadDataFromBackend();
    }

    private String getConfiguredApiUrl() {
        // Check system property first
        String url = System.getProperty("honeybees.api.url");
        if (url != null && !url.trim().isEmpty()) {
            return url.trim();
        }

        // Check environment variable
        url = System.getenv("HONEYBEES_API_URL");
        if (url != null && !url.trim().isEmpty()) {
            return url.trim();
        }

        // Use default
        return DEFAULT_API_URL;
    }

    @PreDestroy
    public void cleanup() {
        if (client != null) {
            client.close();
        }
    }

    private void loadDataFromBackend() {
        try {
            loadOverview();
            loadRoles();
            loadFacts();
        } catch (Exception e) {
            LOGGER.severe("Error loading data from backend API: " + e.getMessage());
            e.printStackTrace();
            loadFallbackData();
        }
    }

    private void loadOverview() {
        WebTarget target = client.target(apiBaseUrl).path("overview");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonObject overview = reader.readObject();
                description = overview.getString("description", "");
            }
        } else {
            LOGGER.warning("Failed to load overview: HTTP " + response.getStatus());
        }
        response.close();
    }

    private void loadRoles() {
        WebTarget target = client.target(apiBaseUrl).path("roles");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        roles = new ArrayList<>();
        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonArray rolesArray = reader.readArray();
                for (int i = 0; i < rolesArray.size(); i++) {
                    JsonObject roleObj = rolesArray.getJsonObject(i);
                    String name = roleObj.getString("name");
                    String desc = roleObj.getString("description");
                    int population = roleObj.getInt("populationPercentage");

                    List<String> responsibilities = new ArrayList<>();
                    JsonArray respArray = roleObj.getJsonArray("responsibilities");
                    for (int j = 0; j < respArray.size(); j++) {
                        responsibilities.add(respArray.getString(j));
                    }

                    roles.add(new BeeRole(name, desc, responsibilities, population));
                }
            }
        } else {
            LOGGER.warning("Failed to load roles: HTTP " + response.getStatus());
        }
        response.close();
    }

    private void loadFacts() {
        WebTarget target = client.target(apiBaseUrl).path("facts");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        List<BeeFact> allFacts = new ArrayList<>();
        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonArray factsArray = reader.readArray();
                for (int i = 0; i < factsArray.size(); i++) {
                    JsonObject factObj = factsArray.getJsonObject(i);
                    String category = factObj.getString("category");
                    String title = factObj.getString("title");
                    String desc = factObj.getString("description");
                    allFacts.add(new BeeFact(category, title, desc));
                }
            }
        } else {
            LOGGER.warning("Failed to load facts: HTTP " + response.getStatus());
        }
        response.close();

        Map<String, List<BeeFact>> grouped = allFacts.stream()
                .collect(Collectors.groupingBy(BeeFact::getCategory, LinkedHashMap::new, Collectors.toList()));

        factCategories = grouped.entrySet().stream()
                .map(e -> new FactCategory(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private void loadFallbackData() {
        LOGGER.info("Loading fallback hardcoded data");
        description = "The western honey bee (Apis mellifera) is the most common of the 7-12 species of honey "
                + "bees worldwide. Data could not be loaded from backend API.";
        roles = new ArrayList<>();
        factCategories = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public List<BeeRole> getRoles() {
        return roles;
    }

    public List<FactCategory> getFactCategories() {
        return factCategories;
    }
}
