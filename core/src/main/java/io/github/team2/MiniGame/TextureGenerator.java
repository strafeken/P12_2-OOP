package io.github.team2.MiniGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;

public class TextureGenerator {

    /**
     * Generates a pipe texture for the Flappy Bird mini-game
     * @param isTopPipe Whether this is a top pipe (true) or bottom pipe (false)
     * @return A texture with a green pipe properly oriented
     */
    public static Texture generatePipeTexture(boolean isTopPipe) {
        int width = 60;
        int height = 400;

        // Create a pixmap
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Base pipe color - bright green for better visibility
        Color pipeColor = new Color(0.2f, 0.9f, 0.2f, 1);
        pixmap.setColor(pipeColor);
        pixmap.fill();

        // Border for visibility
        Color borderColor = new Color(0.0f, 0.4f, 0.0f, 1);
        pixmap.setColor(borderColor);

        // Draw border
        int borderWidth = 3;
        // Left border
        pixmap.fillRectangle(0, 0, borderWidth, height);
        // Right border
        pixmap.fillRectangle(width - borderWidth, 0, borderWidth, height);
        // Top border
        pixmap.fillRectangle(0, 0, width, borderWidth);
        // Bottom border
        pixmap.fillRectangle(0, height - borderWidth, width, borderWidth);

        // Highlight color for 3D effect
        Color highlightColor = new Color(0.3f, 1.0f, 0.3f, 1);
        pixmap.setColor(highlightColor);
        pixmap.fillRectangle(borderWidth + 2, borderWidth + 2, 10, height - (borderWidth + 4));

        // Shadow color for 3D effect
        Color shadowColor = new Color(0.1f, 0.6f, 0.1f, 1);
        pixmap.setColor(shadowColor);
        pixmap.fillRectangle(width - 15 - borderWidth, borderWidth + 2, 10, height - (borderWidth + 4));

        // Cap at the appropriate end
        if (isTopPipe) {
            // Cap at the bottom for top pipe
            pixmap.setColor(borderColor.r * 1.2f, borderColor.g * 1.2f, borderColor.b * 1.2f, 1);
            pixmap.fillRectangle(0, height - 30, width, 30);

            // Cap texture
            Color capColor = new Color(0.3f, 0.8f, 0.3f, 1);
            pixmap.setColor(capColor);
            pixmap.fillRectangle(borderWidth, height - 30, width - (borderWidth * 2), 20);
        } else {
            // Cap at the top for bottom pipe
            pixmap.setColor(borderColor.r * 1.2f, borderColor.g * 1.2f, borderColor.b * 1.2f, 1);
            pixmap.fillRectangle(0, 0, width, 30);

            // Cap texture
            Color capColor = new Color(0.3f, 0.8f, 0.3f, 1);
            pixmap.setColor(capColor);
            pixmap.fillRectangle(borderWidth, 10, width - (borderWidth * 2), 20);
        }

        // Create and return the texture
        Texture pipeTexture = new Texture(pixmap, true); // Use mipmaps for better rendering
        pipeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // Smooth scaling

        // Clean up
        pixmap.dispose();

        return pipeTexture;
    }

    /**
     * Legacy method for backward compatibility
     */
    public static Texture generatePipeTexture() {
        return generatePipeTexture(true);
    }
}
