package io.github.team2.CollisionExtensions;

import io.github.team2.PlayerStatus;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;

public class StartMiniGameHandler implements CollisionListener {
    // You'll need to implement mini-game launching logic here

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.ALIEN_PLAYER) {
            PlayerStatus playerStatus = PlayerStatus.getInstance();

            // Only start mini-game if not already in one
            if (!playerStatus.isInMiniGame()) {
                playerStatus.setInMiniGame(true);
                System.out.println("Starting mini-game with alien!");

                // Here you would launch your mini-game
                // This might involve setting a new scene or changing game state

                // For now, just simulate mini-game
                simulateMiniGame();
            }
        }
    }

    private void simulateMiniGame() {
        // This is where you'd implement your mini-game logic
        // For now, we'll just reset the mini-game flag after a delay

        // In a real implementation, you'd have a complete mini-game
        // and only reset this flag when the mini-game concludes

        // For demonstration purposes:
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simulate 5-second mini-game
                PlayerStatus.getInstance().setInMiniGame(false);
                System.out.println("Mini-game completed!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
