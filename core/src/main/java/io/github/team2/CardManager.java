package io.github.team2;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.Cards.CardFactory;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.InputSystem.GameInputManager;

public class CardManager {
    private IEntityManager entityManager;
    private GameInputManager gameInputManager;
    private World world;
    private Random random;
    private Entity[] food;
    private final Vector2 CARD_SIZE = new Vector2(75, 100);
    private boolean resetPending = false;
    
    public CardManager(IEntityManager entityManager, GameInputManager gameInputManager, World world) {
        this.entityManager = entityManager;
        this.gameInputManager = gameInputManager;
        this.world = world;
        this.random = new Random();
        this.food = new Entity[3];
    }

    public void initializeFood() {
        String[] availableFoods = CardFactory.getFoodTypes();

        for (int i = 0; i < food.length; i++) {
            String randomFood = availableFoods[random.nextInt(availableFoods.length)];
            int xPosition = 100 + (i * 200);
            food[i] = CardFactory.createCard(randomFood, CARD_SIZE, new Vector2(xPosition, 100), world, gameInputManager.getMouseManager());
            entityManager.addEntities(food[i]);
        }
    }
    
    public void queueReset() {
        resetPending = true;
    }

    public void processReset() {
        if (resetPending) {
            resetPending = false;
            resetFood();
        }
    }

    public void resetFood() {
        for (Entity foodCard : food) {
            entityManager.removeEntity(foodCard);
        }

        initializeFood();
    }

    public Entity[] getFood() {
        return food;
    }
}
