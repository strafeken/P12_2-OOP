package io.github.team2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;


public class Camera {
	
	private OrthographicCamera camera = null;
	private final float SCALE= 1.0f;
	
	public Camera(float width , float height) {
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width/SCALE, height/SCALE);
	}
	
	
	public void cameraUpdate(float delta, Vector2 position) {
		
		camera.position.set(position.x, position.y , 0);
			
	}
	
	
}
