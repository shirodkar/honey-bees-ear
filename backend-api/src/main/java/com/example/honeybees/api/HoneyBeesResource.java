package com.example.honeybees.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/bees")
@jakarta.enterprise.context.RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class HoneyBeesResource {

    @GET
    @Path("/overview")
    public Response getOverview() {
        Map<String, String> overview = new LinkedHashMap<>();
        overview.put("scientificName", "Apis mellifera");
        overview.put("commonName", "Western Honey Bee");
        overview.put("kingdom", "Animalia");
        overview.put("phylum", "Arthropoda");
        overview.put("class", "Insecta");
        overview.put("order", "Hymenoptera");
        overview.put("family", "Apidae");
        overview.put("genus", "Apis");
        overview.put("description",
                "The western honey bee is the most common of the 7-12 species of honey bees worldwide. "
                + "They are known for their production of honey and beeswax, as well as their vital role "
                + "as pollinators of crops and wild plants. Honey bees are social insects that live in "
                + "highly organized colonies consisting of a single queen, thousands of workers, and "
                + "hundreds of drones.");
        return Response.ok(overview).build();
    }

    @GET
    @Path("/roles")
    public Response getRoles() {
        return Response.ok(buildRoles()).build();
    }

    @GET
    @Path("/facts")
    public Response getAllFacts() {
        return Response.ok(buildFacts()).build();
    }

    @GET
    @Path("/facts/{category}")
    public Response getFactsByCategory(@PathParam("category") String category) {
        List<BeeFact> filtered = buildFacts().stream()
                .filter(f -> f.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("error", "No facts found for category: " + category))
                    .build();
        }
        return Response.ok(filtered).build();
    }

    @GET
    @Path("/categories")
    public Response getCategories() {
        List<String> categories = buildFacts().stream()
                .map(BeeFact::getCategory)
                .distinct()
                .collect(Collectors.toList());
        return Response.ok(categories).build();
    }

    private List<BeeRole> buildRoles() {
        List<BeeRole> roles = new ArrayList<>();

        roles.add(new BeeRole("Queen",
                "The queen is the only fertile female in the colony and is the mother of all bees in the hive. "
                + "She can live for 2 to 5 years and is the longest-lived member of the colony.",
                Arrays.asList(
                        "Lays up to 2,000 eggs per day during peak season",
                        "Produces pheromones that regulate colony behavior",
                        "Mates only once during a mating flight with multiple drones",
                        "Can choose to lay fertilized (female) or unfertilized (male) eggs"),
                1));

        roles.add(new BeeRole("Worker",
                "Workers are infertile females that make up the vast majority of the colony. "
                + "They perform all tasks necessary for colony survival and their roles change as they age.",
                Arrays.asList(
                        "Clean cells and nurse young larvae (days 1-12)",
                        "Build comb and process honey (days 12-18)",
                        "Guard the hive entrance (days 18-21)",
                        "Forage for nectar, pollen, water, and propolis (days 21+)",
                        "Communicate food sources through the waggle dance",
                        "Regulate hive temperature by fanning wings"),
                95));

        roles.add(new BeeRole("Drone",
                "Drones are male bees whose primary purpose is to mate with virgin queens from other colonies. "
                + "They develop from unfertilized eggs through parthenogenesis.",
                Arrays.asList(
                        "Mate with virgin queens during mating flights",
                        "Die immediately after successful mating",
                        "Cannot sting as they lack a stinger",
                        "Are expelled from the hive before winter"),
                4));

        return roles;
    }

    private List<BeeFact> buildFacts() {
        List<BeeFact> facts = new ArrayList<>();

        facts.add(new BeeFact("Communication", "The Waggle Dance",
                "Honey bees communicate the location of food sources through a sophisticated figure-eight dance. "
                + "The angle of the dance relative to the sun indicates direction, while the duration of the "
                + "waggle run indicates distance."));
        facts.add(new BeeFact("Communication", "Pheromone Signaling",
                "The queen produces a complex blend of pheromones known as the Queen Mandibular Pheromone (QMP) "
                + "that suppresses worker reproduction, attracts drones during mating flights, and maintains "
                + "colony cohesion."));
        facts.add(new BeeFact("Communication", "Piping and Tooting",
                "Virgin queens produce piping sounds to signal their presence to rival queens. The first queen "
                + "to emerge may 'toot' while still-sealed queens respond with 'quacking' sounds."));

        facts.add(new BeeFact("Honey Production", "From Nectar to Honey",
                "Bees visit around 2 million flowers and fly over 55,000 miles to produce just one pound of honey. "
                + "Foragers collect nectar in their honey stomach and pass it to house bees through trophallaxis."));
        facts.add(new BeeFact("Honey Production", "Enzymatic Conversion",
                "Worker bees add the enzyme invertase to nectar, which breaks down sucrose into glucose and fructose. "
                + "They then fan the honey with their wings to reduce moisture content from about 70% to less than 18%."));
        facts.add(new BeeFact("Honey Production", "Honey Never Spoils",
                "Properly sealed honey has an indefinitely long shelf life. Archaeologists have found 3,000-year-old "
                + "honey in Egyptian tombs that was still perfectly edible. Its low moisture content and acidic pH "
                + "prevent bacterial growth."));

        facts.add(new BeeFact("Anatomy", "Five Eyes",
                "Honey bees have five eyes: two large compound eyes on the sides of their head, each with about "
                + "6,900 facets, and three small simple eyes (ocelli) on top of their head that detect light intensity."));
        facts.add(new BeeFact("Anatomy", "Wing Speed",
                "A honey bee's wings beat approximately 200 times per second, producing the characteristic buzzing "
                + "sound. Despite their relatively large bodies, they can fly at speeds up to 15 miles per hour."));
        facts.add(new BeeFact("Anatomy", "The Stinger",
                "A worker bee's stinger is barbed and tears from the bee's body after stinging, resulting in the "
                + "bee's death. Queen bees have smooth stingers and can sting repeatedly without dying."));

        facts.add(new BeeFact("Ecology", "Pollination Powerhouse",
                "Honey bees pollinate approximately 80% of all flowering plants and are responsible for pollinating "
                + "about one-third of the food we eat, including fruits, vegetables, and nuts."));
        facts.add(new BeeFact("Ecology", "Economic Value",
                "The economic value of honey bee pollination in the United States alone is estimated at over "
                + "$15 billion annually. Globally, pollination services are valued at over $200 billion."));
        facts.add(new BeeFact("Ecology", "Colony Collapse Disorder",
                "Since 2006, beekeepers have reported unusually high losses due to Colony Collapse Disorder (CCD), "
                + "linked to pesticides, parasites like the Varroa mite, habitat loss, and climate change."));

        facts.add(new BeeFact("Behavior", "Thermoregulation",
                "Bees maintain the hive temperature at approximately 95 degrees F (35 degrees C) year-round. In winter, they form a "
                + "cluster and vibrate their flight muscles to generate heat. In summer, they collect water and "
                + "fan it to cool the hive."));
        facts.add(new BeeFact("Behavior", "Swarming",
                "When a colony becomes too large, the old queen leaves with about half the workers to establish a "
                + "new colony. The remaining bees raise a new queen from young larvae fed exclusively on royal jelly."));
        facts.add(new BeeFact("Behavior", "Collective Intelligence",
                "A honey bee colony functions as a superorganism, making collective decisions through democratic "
                + "processes. When choosing a new home, scout bees evaluate options and recruit others through "
                + "dancing until consensus is reached."));

        return facts;
    }
}