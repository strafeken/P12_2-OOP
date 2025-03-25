package application.scene;

public class PointsManager {
    private int points;
    private boolean levelUnlocked = false;
    private LevelManager levelManager;

    public static final int LEVEL_2_THRESHOLD = 150;
    public static final int LEVEL_3_THRESHOLD = 500;
    public static final int LEVEL_4_THRESHOLD = 1000;

    public PointsManager() {
        points = 0;
        levelManager = LevelManager.getInstance();
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int value) {
        points += value;
        if (levelManager.checkAndUnlockLevel(points)) {
            levelUnlocked = true;
        }
    }

    public boolean isLevelUnlocked() {
        boolean result = levelUnlocked;
        levelUnlocked = false; // Reset flag after reading
        return result;
    }

    public void resetPoints() {
        points = 0;
    }
}
