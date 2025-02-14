package io.github.team2.AudioSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private Map<String, Sound> soundEffects;
    private String currentMusic;
    private float volume;
    private Music music;

    private AudioManager() {
        soundEffects = new HashMap<>();
        currentMusic = "";
        volume = 1.0f;
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playMusic(String path) {
        if (!currentMusic.equals(path)) {
            if (music != null) {
                music.stop();
                music.dispose();
            }
            music = Gdx.audio.newMusic(Gdx.files.internal(path));
            music.setLooping(true);
            music.setVolume(volume);
            music.play();
            currentMusic = path;
        }
    }

    public void loadSoundEffect(String id, String path) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        soundEffects.put(id, sound);
    }

    public void playSoundEffect(String id) {
        Sound sound = soundEffects.get(id);
        if (sound != null) {
            sound.play(volume);
        }
    }

    public void stopSoundEffect(String id) {
        Sound sound = soundEffects.get(id);
        if (sound != null) {
            sound.stop();
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (music != null) {
            music.setVolume(volume);
        }
    }
    public float getVolume() {
    	return volume;
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
            currentMusic = "";
        }
    }

    public void stopAllSounds() {
        stopMusic();
        for (Sound sound : soundEffects.values()) {
            sound.stop();
        }
    }

    public void dispose() {
        stopAllSounds();
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        soundEffects.clear();
    }
}