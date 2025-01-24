package io.github.team2;

import com.badlogic.gdx.math.Vector2;


public interface movement {
	
	// default coordinates methods
	
	public Vector2 getCoordinate();
	public void setCoordinate(Vector2 vec);
	
	// 8 point movement system 
	public void moveUp(Vector2 position, Vector2 direction);
	public void moveleft(Vector2 position, Vector2 direction);
	public void moveRight(Vector2 position, Vector2 direction);
	public void moveDown(Vector2 position, Vector2 direction);
	

	public void moveUpLeft(Vector2 position, Vector2 direction);
	public void moveUpRight(Vector2 position, Vector2 direction);
	public void moveDownLeft(Vector2 position, Vector2 direction);
	public void moveDownRight(Vector2 position, Vector2 direction);

	// angle section 
	public int getAngle();
	public int setAngle(int ang);
	
	
	// rotation movement 
	public void rotateTo(int num);
	public void rotateLeft(int num);
	public void rotateRight(int num);
	
	
	// distance methods
	
	public Vector2 getDistance(Vector2 current, Vector2 destination);
	
	
}
