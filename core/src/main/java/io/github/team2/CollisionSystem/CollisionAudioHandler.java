package io.github.team2.CollisionSystem;

import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.Entity;

public class CollisionAudioHandler implements CollisionListener {
    private final IAudioManager audioManager;

    // Constructor with dependency injection - accepts interface
    public CollisionAudioHandler(IAudioManager audioManager) {
        this.audioManager = audioManager;
    }

    // Default constructor that uses the Singleton as fallback
    public CollisionAudioHandler() {
        this(AudioManager.getInstance(AudioManager.class));
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        switch (type) {
            case PLAYER_DROP:
                audioManager.playSoundEffect("ding");
                break;
            case CIRCLE_DROP:
                // audioManager.playSoundEffect("ding");
                break;
            case CARD_CARD:
                break;
            default:
                System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }
}
