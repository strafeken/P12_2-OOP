package io.github.team2;

import io.github.team2.InputSystem.PInputManager;
import io.github.team2.Utils.Singleton;

public class GameManager extends Singleton<GameManager> {
    private PInputManager playerInputManager;

    public GameManager() {
    	
    }

    public void setPlayerInputManager(PInputManager playerInputManager) {
    	this.playerInputManager = playerInputManager;
    }
    
    public PInputManager getPlayerInputManager() {
        return playerInputManager;
    }
}
