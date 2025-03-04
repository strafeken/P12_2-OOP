package io.github.team2.CollisionExtensions;

import io.github.team2.CardManager;
import io.github.team2.Player;
import io.github.team2.Cards.Card;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;

public class ConsumeNutritionHandler implements CollisionListener {
	private CardManager cardManager;
	
	public ConsumeNutritionHandler(CardManager cardManager) {
		this.cardManager = cardManager;
	}

	@Override
	public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.CARD_PLAYER) {
            if (a.getEntityType() == EntityType.PLAYER && b.getEntityType() == EntityType.CARD)
                handleCollision((Player) a, (Card) b);
            else if (b.getEntityType() == EntityType.PLAYER && a.getEntityType() == EntityType.CARD)
                handleCollision((Player) b, (Card) a);
        }
	}

    private void handleCollision(Player player, Card card) {
        player.consume(card);
//        cardManager.queueReset();   
    }
}
