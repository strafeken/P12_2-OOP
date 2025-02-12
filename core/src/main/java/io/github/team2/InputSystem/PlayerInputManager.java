package io.github.team2.InputSystem;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.Rotate;
import io.github.team2.EntitySystem.Entity;

public class PlayerInputManager extends InputManager {

	private Entity player;

	public PlayerInputManager(Entity player) {
		this.player = player;
	}

	public void registerUserInput() {
		registerKeyDown(Input.Keys.W, new Move(player, new Vector2(0, 1)));
		registerKeyDown(Input.Keys.A, new Move(player, new Vector2(-1, 0)));
		registerKeyDown(Input.Keys.S, new Move(player, new Vector2(0, -1)));
		registerKeyDown(Input.Keys.D, new Move(player, new Vector2(1, 0)));
		registerKeyDown(Input.Keys.Q, new Rotate(player, new Vector2(-1, 0)));
		registerKeyDown(Input.Keys.E, new Rotate(player, new Vector2(1, 0)));

		registerKeyUp(Input.Keys.W, new Move(player, new Vector2(0, 0)));
		registerKeyUp(Input.Keys.A, new Move(player, new Vector2(0, 0)));
		registerKeyUp(Input.Keys.S, new Move(player, new Vector2(0, 0)));
		registerKeyUp(Input.Keys.D, new Move(player, new Vector2(0, 0)));
		registerKeyUp(Input.Keys.Q, new Rotate(player, new Vector2(0, 0)));
		registerKeyUp(Input.Keys.E, new Rotate(player, new Vector2(0, 0)));

	}
}
