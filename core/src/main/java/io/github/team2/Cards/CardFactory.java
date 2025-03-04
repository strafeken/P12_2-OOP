package io.github.team2.Cards;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Nutrition;
import io.github.team2.InputSystem.MouseManager;

public class CardFactory {
    private static final Map<String, Nutrition> nutritionMap = new HashMap<>();

    static {
        nutritionMap.put("Chicken Salad", new Nutrition(20, 10, 5, 150));
        nutritionMap.put("Tomato Chicken", new Nutrition(18, 12, 6, 140));
        nutritionMap.put("Veggie Mix", new Nutrition(15, 8, 4, 130));
        nutritionMap.put("Soggy Salad", new Nutrition(25, 5, 10, 200));
        nutritionMap.put("Junk Combo", new Nutrition(50, 5, 20, 500));
        nutritionMap.put("Weird Dessert", new Nutrition(40, 4, 15, 450));
        nutritionMap.put("IceCream", new Nutrition(30, 3, 10, 250));
    }

    public static Card createCard(String type, Vector2 size, Vector2 position, World world, MouseManager mouseManager) {
        Nutrition nutrition = nutritionMap.get(type);
        if (nutrition == null) {
            throw new IllegalArgumentException("Unknown card type: " + type);
        }
        return new Card(type, "card.png", size, position, world, mouseManager, nutrition);
    }
}
