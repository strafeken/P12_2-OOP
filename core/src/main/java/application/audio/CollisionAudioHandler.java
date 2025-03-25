package application.audio;

import abstractengine.audio.IAudioManager;
import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import application.entity.CollisionType;

public class CollisionAudioHandler implements CollisionListener {
    private final IAudioManager audioManager;

    public CollisionAudioHandler(IAudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (audioManager == null) {
            System.out.println("AudioManager is null in CollisionAudioHandler!");
            return;
        }

        switch (type) {
            case RECYCLABLE_PLAYER:
                audioManager.playSoundEffect("ding");
                break;
            case NON_RECYCLABLE_PLAYER:
                audioManager.playSoundEffect("ding");
                break;
            case RECYCLING_BIN_PLAYER:
                audioManager.playSoundEffect("ding");
                break;
            case ALIEN_PLAYER:
                audioManager.playSoundEffect("ding");
                break;
            case ASTEROID_PLAYER:
                audioManager.playSoundEffect("ding");
                break;
            default:
                // No sound for unhandled collisions
                break;
        }
    }
}
