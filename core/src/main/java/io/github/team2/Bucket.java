//package io.github.team2;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Vector2;
//
//import io.github.team2.EntitySystem.EntityType;
//import io.github.team2.EntitySystem.TextureObject;
//
//public class Bucket extends DynamicTextureObject {
//
//	public Bucket(String texture) {
//		setEntityType(EntityType.BUCKET);
//		setTexture(new Texture(texture));
//		setPosition(new Vector2(0, 0));
//		setDirection(new Vector2(0, 0));
//		//setSpeed(0);
//	}
//
//	public Bucket(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
//		setEntityType(type);
//		setTexture(new Texture(texture));
//		setPosition(position);
//		setDirection(direction);
//		//setSpeed(speed);
//	}
//
////	@Override
////	public void moveUserControlled() {
////		if (getBody() == null)
////			return;
////
////		
////	}
//
//
//
//	@Override
//	public void update() {
//		updateBody();
//	}
//}
