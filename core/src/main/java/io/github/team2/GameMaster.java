package io.github.team2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private IAudioManager audioManager;
    private ISceneManager sceneManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        audioManager = AudioManager.getInstance();
        sceneManager = SceneManager.getInstance();

        // initialize audio with correct paths and volume
        audioManager.loadSoundEffect("start", "sounds/start.mp3");
        audioManager.loadSoundEffect("ding", "sounds/ding.mp3");
        audioManager.loadSoundEffect("mainmenu", "sounds/mainmenu.mp3");
        audioManager.loadSoundEffect("levelsect", "sounds/night.mp3");

        // Add mini-game sound effects
        audioManager.loadSoundEffect("minigame", "sounds/mainmenu.mp3");
        audioManager.loadSoundEffect("jump", "sounds/ding.mp3");
        audioManager.loadSoundEffect("hit", "sounds/ding.mp3");

        audioManager.setVolume(1.0f);

        sceneManager.addScene(SceneID.MAIN_MENU, new MainMenu());
        
        sceneManager.addScene(SceneID.LEVEL1, new GameScene("planet/planet1_purple.jpg",5,0));
        sceneManager.addScene(SceneID.LEVEL2, new GameScene("planet/planet2_yellow.jpg",5,0));
        sceneManager.addScene(SceneID.LEVEL3, new GameScene("planet/planet3_red.jpg",5,0));
        sceneManager.addScene(SceneID.LEVEL4, new GameScene("planet/planet4_grey.jpg",5,0));
        
        
        sceneManager.addScene(SceneID.PAUSE_MENU, new PauseMenu());
        sceneManager.addScene(SceneID.GAME_OVER, new GameOverScreen());
        sceneManager.addScene(SceneID.SETTINGS_MENU, new SettingsMenu());
        sceneManager.addScene(SceneID.LEVEL_SELECT, new LevelSelectScene());
        sceneManager.setNextScene(SceneID.MAIN_MENU);

        // Initialize level manager
        LevelManager.getInstance();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        sceneManager.update();

        batch.begin();
        sceneManager.draw(batch);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        sceneManager.draw(shape);
        shape.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        audioManager.dispose();
    }
}
