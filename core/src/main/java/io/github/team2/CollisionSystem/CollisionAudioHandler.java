package io.github.team2.CollisionSystem;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.Entity;

public class CollisionAudioHandler implements CollisionListener {
	private AudioManager am;
	
	public CollisionAudioHandler() {
		am = AudioManager.getInstance();
	}

	@Override
	public void onCollision(Entity a, Entity b, CollisionType type) {
		switch (type) {
			case PLAYER_DROP:
				am.playSoundEffect("ding");
				break;
			case CIRCLE_DROP: 
//				am.playSoundEffect("ding");
				break;
			default:
				System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
		}
	}
}
