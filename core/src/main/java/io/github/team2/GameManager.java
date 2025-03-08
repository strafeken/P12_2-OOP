package io.github.team2;

import io.github.team2.InputSystem.PlayerInputManager;

public class GameManager {
	private static GameManager instance = null;
    private PlayerInputManager playerInputManager;

    private GameManager() {
    	
    }
    
    public static synchronized GameManager getInstance() {
    	if (instance == null)
    		instance = new GameManager();
    	
    	return instance;
    }
    
    public PlayerInputManager getPlayerInputManager() {
        return playerInputManager;
    }

    public void setPlayerInputManager(PlayerInputManager playerInputManager) {
    	this.playerInputManager = playerInputManager;
    }
}
