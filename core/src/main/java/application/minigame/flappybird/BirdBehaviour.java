package application.minigame.flappybird;

/**
 * Combined class to handle bird behavior states and actions
 */
public class BirdBehaviour {
    /**
     * Represents the possible states of the bird
     */
    public enum State {
        IDLE,
        FLYING,
        FALLING,
        DEAD
    }

    /**
     * Represents the possible actions the bird can take
     */
    public enum Action {
        NONE,
        FLAP
    }
}
