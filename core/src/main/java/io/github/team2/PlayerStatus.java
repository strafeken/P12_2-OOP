package io.github.team2;

import io.github.team2.EntitySystem.Entity;

public class PlayerStatus {
    private static PlayerStatus instance = null;

    private Entity player;
    private boolean isCarryingRecyclable = false;
    private Entity carriedItem = null;
    private int lives = 5;
    private boolean inMiniGame = false;

    private PlayerStatus() {
        // Private constructor for singleton
    }

    public static synchronized PlayerStatus getInstance() {
        if (instance == null)
            instance = new PlayerStatus();

        return instance;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    public boolean isCarryingRecyclable() {
        return isCarryingRecyclable;
    }

    public void setCarryingRecyclable(boolean isCarryingRecyclable) {
        this.isCarryingRecyclable = isCarryingRecyclable;
    }

    public Entity getCarriedItem() {
        return carriedItem;
    }

    public void setCarriedItem(Entity carriedItem) {
        this.carriedItem = carriedItem;
        this.isCarryingRecyclable = (carriedItem != null);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
    }

    public void decrementLife() {
        lives = Math.max(0, lives - 1);
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isInMiniGame() {
        return inMiniGame;
    }

    public void setInMiniGame(boolean inMiniGame) {
        this.inMiniGame = inMiniGame;
    }

    public void reset() {
        isCarryingRecyclable = false;
        carriedItem = null;
        lives = 5;
        inMiniGame = false;
        lastAlienEncounter = null;
    }

    // Add this method to PlayerStatus class
    private Entity lastAlienEncounter;

    public Entity getLastAlienEncounter() {
        return lastAlienEncounter;
    }

    public void setLastAlienEncounter(Entity alien) {
        this.lastAlienEncounter = alien;
    }

}
