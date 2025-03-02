package io.github.team2.Cards;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.InputSystem.MouseManager;

public class CardFactory {
    public static Card createCard(String type, Vector2 size, Vector2 position, World world, MouseManager mouseManager) {
        switch (type) {
	     	// Healthy Recipes
	        case "Chicken Salad":
	            return new Card("Chicken Salad", "card.png", size, position, world, mouseManager);
	        case "Tomato Chicken":
	            return new Card("Tomato Chicken", "card.png", size, position, world, mouseManager);
	        case "Veggie Mix":
	            return new Card("Veggie Mix", "card.png", size, position, world, mouseManager);
	
	        // Bad Recipes
	        case "Soggy Salad":
	            return new Card("Soggy Salad", "card.png", size, position, world, mouseManager);
	        case "Junk Combo":
	            return new Card("Junk Combo", "card.png", size, position, world, mouseManager);
	        case "Weird Dessert":
	            return new Card("Weird Dessert", "card.png", size, position, world, mouseManager);
	
	        // Ingredients
	        case "Lettuce":
	            return new Card("Lettuce", "card.png", size, position, world, mouseManager);
	        case "Chicken":
	            return new Card("Chicken", "card.png", size, position, world, mouseManager);
	        case "Tomato":
	            return new Card("Tomato", "card.png", size, position, world, mouseManager);
	        case "Fries":
	            return new Card("Fries", "card.png", size, position, world, mouseManager);
	        case "Coke":
	            return new Card("Coke", "card.png", size, position, world, mouseManager);
	        case "IceCream":
	            return new Card("IceCream", "card.png", size, position, world, mouseManager);
	
	        default:
	            throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }
}
