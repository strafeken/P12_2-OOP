package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface ISceneManager {
    void addScene(SceneID id, Scene scene);
    void setNextScene(SceneID id);
    void overlayScene(SceneID id);
    void removeOverlay();
    Scene getCurrentScene();
    SceneID getCurrentSceneID();
    void update();
    void draw(SpriteBatch batch);
    void draw(ShapeRenderer shape);
}
