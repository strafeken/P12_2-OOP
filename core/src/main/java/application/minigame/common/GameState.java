package application.minigame.common;

/**
 * Represents the possible states of a mini-game.
 */
public enum GameState {
    READY,      // Game is ready, waiting for player to start
    PLAYING,    // Game is actively playing
    GAME_OVER,  // Game is over, showing score
    CONFIRM_EXIT // Player is confirming exit
}
