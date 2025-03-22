package abstractEngine.InputSystem;

public abstract class InputManager implements IInputManager {
	protected final KeyboardManager keyboardManager;
	protected final MouseManager mouseManager;
	
	public InputManager() {
        keyboardManager = new KeyboardManager();
        mouseManager = new MouseManager();
	}
	
	@Override
    public void registerKeyDown(int keycode, Action action) {
        keyboardManager.registerKeyDown(keycode, action);
    }

	@Override
    public void registerKeyUp(int keycode, Action action) {
        keyboardManager.registerKeyUp(keycode, action);
    }

	@Override
    public void registerClickable(Clickable clickable) {
    	mouseManager.registerClickable(clickable);
    }

	@Override
    public void registerMouseDown(int button, Action action) {
        mouseManager.registerMouseDown(button, action);
    }

	@Override
    public void registerMouseUp(int button, Action action) {
        mouseManager.registerMouseUp(button, action);
    }
    
	@Override
    public KeyboardManager getKeyboardManager() {
    	return keyboardManager;
    }
    
	@Override
    public MouseManager getMouseManager() {
    	return mouseManager;
    }
    
	@Override
    public void update() {
        keyboardManager.update();
        mouseManager.update();
    }
}
