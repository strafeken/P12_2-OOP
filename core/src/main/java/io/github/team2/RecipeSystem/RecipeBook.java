package io.github.team2.RecipeSystem;

import java.util.HashMap;
import java.util.Map;

public class RecipeBook {
	private static final Map<String, String> healthyRecipes = new HashMap<>();
    private static final Map<String, String> badRecipes = new HashMap<>();

    static {
        // Healthy Recipes
        healthyRecipes.put("Lettuce+Chicken", "Chicken Salad");
        healthyRecipes.put("Tomato+Chicken", "Tomato Chicken");
        healthyRecipes.put("Lettuce+Tomato", "Veggie Mix");

        // Bad Recipes
        badRecipes.put("Lettuce+Fries", "Soggy Salad");
        badRecipes.put("Chicken+Coke", "Junk Combo");
        badRecipes.put("Tomato+IceCream", "Weird Dessert");
    }

    public static String getMergedCard(String card1, String card2) {
        String key1 = card1 + "+" + card2;
        String key2 = card2 + "+" + card1; // Allow reverse combinations

        if (healthyRecipes.containsKey(key1)) {
            return healthyRecipes.get(key1);
        } else if (healthyRecipes.containsKey(key2)) {
            return healthyRecipes.get(key2);
        } else if (badRecipes.containsKey(key1)) {
            return badRecipes.get(key1);
        } else if (badRecipes.containsKey(key2)) {
            return badRecipes.get(key2);
        }
        return null;
    }

    public static RecipeType getRecipeType(String card1, String card2) {
        String key1 = card1 + "+" + card2;
        String key2 = card2 + "+" + card1;

        if (healthyRecipes.containsKey(key1) || healthyRecipes.containsKey(key2)) {
            return RecipeType.HEALTHY;
        } else if (badRecipes.containsKey(key1) || badRecipes.containsKey(key2)) {
            return RecipeType.BAD;
        }
        return RecipeType.NONE;
    }
}
