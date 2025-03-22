package io.github.team2.SceneSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface ISceneManager {
	public void addScene(SceneID id, Scene scene);
	public void removeScene(SceneID id);
	public void setNextScene(SceneID id);
	public void overlayScene(SceneID id);
	public void removeOverlay();
	public Scene getCurrentScene();
	public Scene getScene(SceneID id);
	public SceneID getCurrentSceneID();
    public void update();
    public void draw(SpriteBatch batch);
    public void draw(ShapeRenderer shape);
    public boolean hasScene(SceneID id);
}
