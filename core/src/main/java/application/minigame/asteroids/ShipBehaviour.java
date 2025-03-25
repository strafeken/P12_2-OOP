package application.minigame.asteroids;

/**
 * Combined class to handle ship behavior states and actions
 */
public class ShipBehaviour {
    /**
     * Represents the possible states of the ship
     */
    public enum State {
        IDLE,
        MOVING,
        INVULNERABLE,
        DESTROYED
    }

    /**
     * Represents the possible actions the ship can take
     */
    public enum Action {
        NONE,
        MOVE_LEFT,
        MOVE_RIGHT,
        BOOST
    }
}
