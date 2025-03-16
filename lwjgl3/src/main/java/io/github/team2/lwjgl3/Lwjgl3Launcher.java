package io.github.team2.lwjgl3;

import java.lang.classfile.FieldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.github.team2.GameMaster;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	


	
	private static int width = 800;
	private static int height = 480;
	
	
	
    public static void main(String[] args) {
        try {
            if (StartupHelper.startNewJvmIfRequired()) return;
            createApplication();
        } catch (Exception e) {
            System.err.println("Application failed to launch: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new GameMaster(width, height), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Drop Game - Team 2");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.setWindowedMode(width, height);
        configuration.setResizable(true);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
