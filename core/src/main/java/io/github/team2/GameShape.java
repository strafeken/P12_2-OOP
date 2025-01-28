package io.github.team2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameShape extends Entity {

	private Color color;
	
	public GameShape()
	{
		setEntityType(EntityType.UNDEFINED);
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(0);
		color = Color.WHITE;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public abstract void draw(ShapeRenderer shape);

	public abstract void update();
}
