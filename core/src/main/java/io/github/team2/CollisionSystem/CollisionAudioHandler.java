package io.github.team2.CollisionSystem;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.Entity;

public class CollisionAudioHandler implements CollisionListener {
    private final AudioManager am;

    // Constructor with dependency injection
    public CollisionAudioHandler(AudioManager audioManager) {
        this.am = audioManager;
    }

    // Default constructor that uses the Singleton as fallback
    public CollisionAudioHandler() {
        this(AudioManager.getInstance(AudioManager.class));
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        switch (type) {
            case PLAYER_DROP:
                am.playSoundEffect("ding");
                break;
            case CIRCLE_DROP:
                // am.playSoundEffect("ding");
                break;
            case CARD_CARD:
            	break;
            default:
                System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }
}
