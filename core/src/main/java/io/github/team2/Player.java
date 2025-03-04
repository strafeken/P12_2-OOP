package io.github.team2;


import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.PlayerBehaviour;
import io.github.team2.Cards.Card;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;

public class Player extends DynamicTextureObject<PlayerBehaviour.State, PlayerBehaviour.Move > {
	private Nutrition nutrition;
	
	public Player(
			EntityType type, 
			String texture, 
			Vector2 size,
			Vector2 position, 
			Vector2 direction, 
			Vector2 rotation, 
			float speed,
			PlayerBehaviour.State state, 
			PlayerBehaviour.Move actionState) {
		super(type, texture, size, position, direction,rotation, speed, state, actionState);
		nutrition = new Nutrition(50, 50, 50, 50);
	}
	
    public Nutrition getNutrition() {
        return nutrition;
    }
	
    public void consume(Card card) {
        Nutrition cardNutrition = card.getNutrition();
        
        nutrition.modifyCarbohydrates(cardNutrition.getCarbohydrates());
        nutrition.modifyProteins(cardNutrition.getProteins());
        nutrition.modifyFats(cardNutrition.getFats());
        nutrition.modifyCalories(cardNutrition.getCalories());

        System.out.println(card.getName() + " consumed!");
        System.out.println("Updated carbohydrates: " + nutrition.getCarbohydrates() +
        		" proteins: " + nutrition.getProteins() +
        		" fats: " + nutrition.getFats() +
        		" calories: " + nutrition.getCalories());
    }
}
