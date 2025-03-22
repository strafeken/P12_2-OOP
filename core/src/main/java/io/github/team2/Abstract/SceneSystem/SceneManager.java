package io.github.team2.Abstract.SceneSystem;

public class SceneManager extends BaseSceneManager {
    private static SceneManager instance = null;

    public static synchronized SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();

        return instance;
    }
}
