package io.github.team2.AudioSystem;

public interface IAudioManager {
    void playSoundEffect(String id);
    void stopSoundEffect(String id);
    void setVolume(float volume);
    float getVolume();
}
