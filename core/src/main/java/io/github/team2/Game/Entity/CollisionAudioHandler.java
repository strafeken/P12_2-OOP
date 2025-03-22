package io.github.team2.Game.Entity;

import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.Entity;

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
            default:
                // No sound for unhandled collisions
                break;
        }
    }
}
