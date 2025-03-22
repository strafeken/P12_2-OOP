package abstractEngine.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Camera {
	public OrthographicCamera camera = null;
	private final float SCALE= 1.0f;
	
	public Camera(float width , float height) {
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width/SCALE, height/SCALE);
	}
	
	
	public void cameraUpdate(float delta, Vector2 position) {
		
		camera.position.set(position.x, position.y , 0);
		camera.update();
	}
	
	
	public void cameraReset() {
		camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		camera.update();
	}
	
}
