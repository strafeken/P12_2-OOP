package io.github.team2;

import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.Utils.Singleton;

public class GameManager extends Singleton<GameManager> {
    private PlayerInputManager playerInputManager;

    public GameManager() {
    	
    }
    
    public PlayerInputManager getPlayerInputManager() {
        return playerInputManager;
    }

    public void setPlayerInputManager(PlayerInputManager playerInputManager) {
    	this.playerInputManager = playerInputManager;
    }
}
