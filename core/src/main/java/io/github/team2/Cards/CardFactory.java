package io.github.team2.Cards;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CardFactory {
    public static Card createCard(String type, Vector2 size, Vector2 position, World world) {
        switch (type) {
            case "Lettuce":
                return new Card("Lettuce", "card.png", size, position, world);
            case "Chicken":
                return new Card("Chicken", "card.png", size, position, world);
            case "Tomato":
                return new Card("Tomato", "card.png", size, position, world);
            default:
                throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }
}
