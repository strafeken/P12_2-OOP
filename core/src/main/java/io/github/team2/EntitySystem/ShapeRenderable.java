package io.github.team2.EntitySystem;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Interface for entities that can be rendered with ShapeRenderer
 * This allows for a more standardized approach to shape rendering
 */
public interface ShapeRenderable {
    /**
     * Draw the shape using the provided ShapeRenderer
     * @param shapeRenderer The ShapeRenderer to use
     */
    void drawShape(ShapeRenderer shapeRenderer);
}