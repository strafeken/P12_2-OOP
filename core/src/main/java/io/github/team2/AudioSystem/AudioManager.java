package io.github.team2.AudioSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import io.github.team2.Utils.Singleton;

import java.util.HashMap;
import java.util.Map;

public class AudioManager extends Singleton<AudioManager> {
	private Map<String, Sound> soundEffects;
	private Map<String, Long> activeSound; // Track active sound IDs
	private String currentMusic;
	private float volume;
	private Music music;

    public AudioManager() {
        soundEffects = new HashMap<>();
        activeSound = new HashMap<>(); // Add this to track active sounds
        currentMusic = "";
        volume = 1.0f;
        Gdx.app.log("AudioManager", "AudioManager created.");
    }

    public void playMusic(String path) {
        if (currentMusic.equals(path) && music != null && music.isPlaying()) {
            Gdx.app.log("AudioManager", "Music already playing: " + path);
            return;
        }
        if (music != null) {
            music.stop();
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal(path));
        if (music == null) {
            Gdx.app.log("AudioManager", "Error: Failed to load music from path: " + path);
            return;
        }
        music.setLooping(true);
        music.setVolume(volume);
        music.play();
        currentMusic = path;
        Gdx.app.log("AudioManager", "Playing music: " + path + " at volume: " + volume);
    }
    public void loadSoundEffect(String id, String path) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        soundEffects.put(id, sound);
    }


    public void playSoundEffect(String id) {
        Sound sound = soundEffects.get(id);
        if (sound != null) {

            // Stop previous instance if it exists
            if (activeSound.containsKey(id)) {
                sound.stop(activeSound.get(id));
            }
            // Play and store the new sound ID
            long soundId = sound.play(volume); // Play with initial volume
            activeSound.put(id, soundId);
            Gdx.app.log("AudioManager", "Playing sound effect: " + id + " with volume: " + volume);
        }
    }



    public void stopSoundEffect(String id) {
        Sound sound = soundEffects.get(id);

        if (sound != null && activeSound.containsKey(id)) {
            sound.stop(activeSound.get(id));
            activeSound.remove(id);
        }
    }

    public void setVolume(float newVolume) {
        this.volume = Math.max(0f, Math.min(1f, newVolume));
        // Update music volume
        if (music != null) {
            //music.setVolume(volume);
            music.setVolume(this.volume);
        }
        // Update all active sound effects
        for (Map.Entry<String, Sound> entry : soundEffects.entrySet()) {
            String id = entry.getKey();
            Sound sound = entry.getValue();
            if (activeSound.containsKey(id)) {
                sound.setVolume(activeSound.get(id), this.volume);
            }
        }

        Gdx.app.log("AudioManager", "Volume updated to: " + this.volume);
    }
    public float getVolume() {
    	return volume;
    }

    public void stopMusic() {
        if (music != null) {
            music.dispose();
            music = null;
        }
        currentMusic = "";
        Gdx.app.log("AudioManager", "Music stopped.");
    }

    public void stopAllSounds() {
        stopMusic();
        for (Sound sound : soundEffects.values()) {
            if (sound != null) {
                sound.stop();
            }
        }
        activeSound.clear();
        Gdx.app.log("AudioManager", "All sounds stopped.");
    }

    public void dispose() {
        if (music != null) {
            music.dispose();
            music = null;
        }
        for (Sound sound : soundEffects.values()) {
            if (sound != null) {
                sound.dispose();
            }
        }
        soundEffects.clear();
        activeSound.clear();
        Gdx.app.log("AudioManager", "AudioManager disposed.");
    }
}

