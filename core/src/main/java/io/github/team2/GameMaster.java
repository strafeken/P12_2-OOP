package io.github.team2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.AudioSystem.AudioManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private SceneManager sm;

    @Override
    public void create() {
        sm = SceneManager.getInstance();

        // Initialize audio with correct paths and volume
        AudioManager.getInstance().loadSoundEffect("start", "sounds/start.mp3");
        AudioManager.getInstance().loadSoundEffect("ding", "sounds/ding.mp3");
        AudioManager.getInstance().loadSoundEffect("mainmenu", "sounds/mainmenu.mp3");
        AudioManager.getInstance().setVolume(1.0f);

        sm.addScene(SceneID.MAIN_MENU, new MainMenu());
        sm.addScene(SceneID.GAME_SCENE, new GameScene());
        sm.addScene(SceneID.PAUSE_MENU, new PauseMenu());

        sm.setNextScene(SceneID.MAIN_MENU);

        batch = new SpriteBatch();
        shape = new ShapeRenderer();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        sm.update();
        batch.begin();
        sm.draw(batch);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        sm.draw(shape);
        shape.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        AudioManager.getInstance().dispose();
    }
}