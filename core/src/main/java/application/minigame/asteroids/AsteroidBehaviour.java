package application.minigame.asteroids;

/**
 * Combined class to handle asteroid behavior states and actions
 */
public class AsteroidBehaviour {
    /**
     * Represents the possible states of the asteroid
     */
    public enum State {
        IDLE,
        MOVING,
        EXPLODING
    }

    /**
     * Represents the possible actions the asteroid can take
     */
    public enum Action {
        NONE,
        MOVE,
        EXPLODE
    }
}
