package io.github.team2.SceneSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.team2.Utils.Singleton;

public class SceneManager extends Singleton<SceneManager> implements ISceneManager {
    private final Map<SceneID, Scene> scenes;
    // handles scene states
    private final Stack<SceneID> sceneStack;

    public SceneManager() {
        scenes = new HashMap<>();
        sceneStack = new Stack<>();
    }

    @Override
    public void addScene(SceneID id, Scene scene) {
        scenes.put(id, scene);
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
        return scenes.get(sceneStack.peek());
    }

    @Override
    public SceneID getCurrentSceneID() {
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
