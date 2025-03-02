package io.github.team2.CollisionSystem;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Cards.Card;
import io.github.team2.Cards.CardFactory;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.MouseManager;
import io.github.team2.RecipeSystem.RecipeBook;
import io.github.team2.RecipeSystem.RecipeType;

public class MergeSystem implements CollisionListener {
    private EntityManager em;
    private World world;
    private Queue<Runnable> mergeQueue;
    private MouseManager mouseManager;

    public MergeSystem(EntityManager entityManager, World world, MouseManager mouseManager) {
        em = entityManager;
        this.world = world;
        mergeQueue = new LinkedList<>();
        this.mouseManager = mouseManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.CARD_CARD) {
            queueMerge((Card) a, (Card) b);
        }
    }

    private void queueMerge(Card cardA, Card cardB) {
        String mergedName = RecipeBook.getMergedCard(cardA.getName(), cardB.getName());
        RecipeType recipeType = RecipeBook.getRecipeType(cardA.getName(), cardB.getName());

        if (mergedName != null) {
            System.out.println(cardA.getName() + " merged with " + cardB.getName() + " to form " + mergedName);

            // Queue the merge operation
            mergeQueue.add(() -> {
                performMerge(cardA, cardB, mergedName, recipeType);
            });
        } else {
            System.out.println("Recipe does not exist!");
        }
    }

    private void performMerge(Card cardA, Card cardB, String mergedName, RecipeType recipeType) {
        // Ensure entities and bodies are valid before proceeding
        if (cardA == null || cardB == null) {
            System.err.println("Error: One of the merging cards is null!");
            return;
        }

        if (cardA.getBody() == null || cardB.getBody() == null) {
            System.err.println("Error: One of the card bodies is null!");
            return;
        }

        Body bodyA = cardA.getBody().getBody();
        Body bodyB = cardB.getBody().getBody();

        if (bodyA == null || bodyB == null) {
            System.err.println("Error: One of the card Box2D bodies is null!");
            return;
        }

        // Remove entities from entity manager
        Vector2 newPosition = cardB.getPosition();
//        Vector2 newPosition = new Vector2(
//        		(cardA.getPosition().x + cardB.getPosition().x) / 2,
//        		(cardA.getPosition().y + cardB.getPosition().y) / 2
//        		);
        
        cardA.stopDragging(); // stop the card from moving

        mouseManager.deregisterDraggable(cardA);
        mouseManager.deregisterDraggable(cardB);

        em.removeEntity(cardA);
        em.removeEntity(cardB);

        // Create the new merged card safely
        try {
            Card mergedCard = CardFactory.createCard(mergedName, new Vector2(100, 150), newPosition, world, mouseManager);
            em.addEntities(mergedCard);
            
            System.out.println("New merged card " + mergedName + " created at " + newPosition);

            // Print message based on recipe type
            if (recipeType == RecipeType.HEALTHY) {
                System.out.println("Healthy Recipe Created! (+Bonus)");
            } else if (recipeType == RecipeType.BAD) {
                System.out.println("Bad Recipe Created! (-Penalty)");
            }
        } catch (Exception e) {
            System.err.println("Error while creating merged card: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void processMerges() {
        while (!mergeQueue.isEmpty()) {
            mergeQueue.poll().run();  // Execute queued merge actions
        }
    }
}
