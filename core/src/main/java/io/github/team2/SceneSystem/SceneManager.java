package io.github.team2.SceneSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SceneManager implements ISceneManager {
    private static SceneManager instance = null;
    private final Map<SceneID, Scene> scenes;
    // handles scene states
    private final Stack<SceneID> sceneStack;

    private SceneManager() {
        scenes = new HashMap<>();
        sceneStack = new Stack<>();
    }

    public static synchronized SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();

        return instance;
    }

    @Override
    public void addScene(SceneID id, Scene scene) {
        scenes.put(id, scene);
    }

    @Override
    public void removeScene(SceneID id) {
        if (scenes.containsKey(id)) {
            Scene scene = scenes.get(id);
            scene.unload();
            scenes.remove(id);
        }
    }

    @Override
    public void setCurrentScene(SceneID id) {
        setNextScene(id);
    }

    @Override
    public void setNextScene(SceneID id) {
        if (!sceneStack.isEmpty())
            unloadCurrentScene();

        loadScene(id);
    }

    @Override
    // used for pause menu
    public void overlayScene(SceneID id) {
        loadScene(id);
    }

    @Override
    // used to return to game scene
    public void removeOverlay() {
        // ensure there is always a scene
        if (sceneStack.size() > 1)
            unloadCurrentScene();
    }

    @Override
    public Scene getCurrentScene() {
        if (sceneStack.isEmpty())
            return null;
        return scenes.get(sceneStack.peek());
    }

    @Override
    public Scene getScene(SceneID id) {
        return scenes.get(id);
    }

    @Override
    public SceneID getCurrentSceneID() {
        if (sceneStack.isEmpty())
            return null;
        return sceneStack.peek();
    }

    @Override
    public void update() {
        if (!sceneStack.isEmpty()) {
            scenes.get(sceneStack.peek()).update();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (SceneID id : sceneStack)
            scenes.get(id).draw(batch);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        for (SceneID id : sceneStack)
            scenes.get(id).draw(shape);
    }

    @Override
    public boolean hasScene(SceneID id) {
        return scenes.containsKey(id);
    }

    private void loadScene(SceneID id) {
        if (!scenes.containsKey(id))
            return;

        sceneStack.push(id);
        scenes.get(id).load();
    }

    private void unloadCurrentScene() {
        if (sceneStack.isEmpty())
            return;

        scenes.get(sceneStack.pop()).unload();
    }
}
