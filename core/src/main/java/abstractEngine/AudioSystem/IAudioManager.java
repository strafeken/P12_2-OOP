package abstractEngine.AudioSystem;

public interface IAudioManager {
    /**
     * Plays a sound effect with the given id
     * @param id The identifier for the sound effect
     */
    void playSoundEffect(String id);

    /**
     * Stops a sound effect with the given id
     * @param id The identifier for the sound effect
     */
    void stopSoundEffect(String id);

    /**
     * Sets the volume for all audio
     * @param volume The volume level (0.0f to 1.0f)
     */
    void setVolume(float volume);

    /**
     * Gets the current volume level
     * @return The current volume (0.0f to 1.0f)
     */
    float getVolume();

    /**
     * Plays background music from the specified path
     * @param path The file path to the music
     */
    void playMusic(String path);

    /**
     * Loads a sound effect with the given id from path
     * @param id The identifier for the sound effect
     * @param path The file path to the sound effect
     */
    void loadSoundEffect(String id, String path);

    /**
     * Stops the currently playing music
     */
    void stopMusic();

    /**
     * Stops all sounds including music and effects
     */
    void stopAllSounds();

    /**
     * Disposes all audio resources
     */
    void dispose();
}
