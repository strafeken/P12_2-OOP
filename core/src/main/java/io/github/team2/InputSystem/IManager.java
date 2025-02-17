package io.github.team2.InputSystem;

public class IManager {
	private final KeyboardManager keyboardManager;
//    private final MManager mouseManager;
    
    public IManager()
    {
        this.keyboardManager = new KeyboardManager();
//        this.mouseManager = new MManager();
    }

    public void update() {
        keyboardManager.update();
//        mouseManager.update();
    }
}
