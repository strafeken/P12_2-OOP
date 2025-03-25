package application.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LevelManager {
    private static LevelManager instance = null;
    private int unlockedLevels;
    private int currentLevel;
    private final Preferences prefs;

    // Points required to unlock each level
    public static final int LEVEL2_POINTS = 150;
    public static final int LEVEL3_POINTS = 500;
    public static final int LEVEL4_POINTS = 1000;

    // Alien speed multiplier for each level
    public static final float LEVEL1_ALIEN_SPEED = 30f;
    public static final float LEVEL2_ALIEN_SPEED = 50f;
    public static final float LEVEL3_ALIEN_SPEED = 80f;
    public static final float LEVEL4_ALIEN_SPEED = 120f;

    private LevelManager() {
        prefs = Gdx.app.getPreferences("level-progress");
        // Make sure we explicitly set to 1, in case there's a bug elsewhere
        unlockedLevels = 1; // Only level 1 is unlocked by default
        prefs.putInteger("unlockedLevels", unlockedLevels);

        currentLevel = prefs.getInteger("currentLevel", 1);

        // Debug output
        System.out.println("LevelManager initialized: unlocked levels = " + unlockedLevels +
                          ", current level = " + currentLevel);

        // Save to ensure preferences file exists with correct values
        saveProgress();
    }

    public static synchronized LevelManager getInstance() {
        if (instance == null)
            instance = new LevelManager();

        return instance;
    }

    public int getUnlockedLevels() {
        return unlockedLevels;
    }

    public void setCurrentLevel(int level) {
        if (level > 0 && level <= 4) {
            this.currentLevel = level;
            System.out.println("Setting current level to: " + level);

            // Save the current level too
            prefs.putInteger("currentLevel", currentLevel);
            prefs.flush();
        } else {
            System.out.println("Invalid level: " + level);
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public float getCurrentAlienSpeed() {
        switch (currentLevel) {
            case 2: return LEVEL2_ALIEN_SPEED;
            case 3: return LEVEL3_ALIEN_SPEED;
            case 4: return LEVEL4_ALIEN_SPEED;
            default: return LEVEL1_ALIEN_SPEED;
        }
    }

    public boolean isLevelUnlocked(int level) {
        return level <= unlockedLevels;
    }

    public boolean checkAndUnlockLevel(int points) {
        int newUnlocked = unlockedLevels;

        if (points >= LEVEL4_POINTS && unlockedLevels < 4) {
            newUnlocked = 4;
        } else if (points >= LEVEL3_POINTS && unlockedLevels < 3) {
            newUnlocked = 3;
        } else if (points >= LEVEL2_POINTS && unlockedLevels < 2) {
            newUnlocked = 2;
        }

        if (newUnlocked > unlockedLevels) {
            unlockedLevels = newUnlocked;
            saveProgress();
            return true;
        }
        return false;
    }

    public void saveProgress() {
        prefs.putInteger("unlockedLevels", unlockedLevels);
        prefs.putInteger("currentLevel", currentLevel);
        prefs.flush();
        System.out.println("Progress saved: unlocked levels = " + unlockedLevels +
                          ", current level = " + currentLevel);
    }

    public void reset() {
        unlockedLevels = 1;
        currentLevel = 1;
        saveProgress();
    }

    // Debug method to force unlock all levels (for testing)
    public void debugUnlockAllLevels() {
        unlockedLevels = 4;
        saveProgress();
        System.out.println("DEBUG: All levels unlocked");
    }
}
