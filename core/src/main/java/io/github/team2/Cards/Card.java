package io.github.team2.Cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.DragHandler;
import io.github.team2.InputSystem.Draggable;
import io.github.team2.InputSystem.MouseManager;
import io.github.team2.InputSystem.PlayerInputManager;

public class Card extends DynamicTextureObject<CardBehaviour.State, CardBehaviour.Move > implements Draggable {
	private String name;
    private DragHandler dragHandler;

    public Card(String name, String texture, Vector2 size, Vector2 position, World world, MouseManager mouseManager) {
        super(EntityType.CARD, texture, size,
        		position, new Vector2(0, 0), new Vector2(0, 0), 0,
        		CardBehaviour.State.IDLE, CardBehaviour.Move.NONE);
        this.name = name;
        initPhysicsBody(world, BodyDef.BodyType.KinematicBody); 
        this.dragHandler = new DragHandler(this, world);
        mouseManager.registerDraggable(this);
    }
    
    public String getName() {
    	return name;
    }

    public Rectangle getBounds() {
        return new Rectangle(getPosition().x - getWidth() / 2, getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void startDragging() {
        dragHandler.startDragging();
    }

    @Override
    public void updateDragging() {
        dragHandler.updateDragging();
    }

    @Override
    public void stopDragging() {
        dragHandler.stopDragging();
    }

    @Override
    public boolean isDragging() {
        return dragHandler.isDragging();
    }

    @Override
    public void update() {
        updateBody();
    }
}
