package application.entity;

import abstractengine.entity.Entity;
import application.entity.trash.RecyclableTrash;
import application.scene.LevelManager;

public class PlayerStatus {
    private static PlayerStatus instance = null;

    private Entity player;
    private boolean isCarryingRecyclable = false;
    private Entity carriedItem = null;
    private int lives = 5;

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

    public void reset() {
        isCarryingRecyclable = false;
        carriedItem = null;

        // Set lives based on current level
        int currentLevel = LevelManager.getInstance().getCurrentLevel();
        switch (currentLevel) {
            case 2:
                lives = 7;
                break;
            case 3:
                lives = 5;
                break;
            case 4:
                lives = 3;
                break;
            default:
                lives = 9; // Level 1
        }

        lastAlienEncounter = null;
    }

    private Entity lastAlienEncounter;

    public Entity getLastAlienEncounter() {
        return lastAlienEncounter;
    }

    public void setLastAlienEncounter(Entity alien) {
        this.lastAlienEncounter = alien;
    }

    /**
     * Check if the player is holding trash
     * @return true if the player is holding trash
     */
    public boolean isHoldingTrash() {
        return isCarryingRecyclable && carriedItem != null;
    }

    /**
     * Make the player drop any trash they're holding
     */
    public void dropTrash() {
        if (isCarryingRecyclable && carriedItem != null) {
            System.out.println("Player dropped " +
                              (carriedItem instanceof RecyclableTrash ?
                              ((RecyclableTrash)carriedItem).getRecyclableType().toString() :
                              "trash"));
            isCarryingRecyclable = false;
            carriedItem = null;
        }
    }
}
