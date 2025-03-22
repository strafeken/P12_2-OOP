 package abstractengine.input;

public interface IInputManager {
    public void registerKeyDown(int keycode, Action action);
    public void registerKeyUp(int keycode, Action action);
    public void registerClickable(Clickable clickable);
    public void registerMouseDown(int button, Action action);
    public void registerMouseUp(int button, Action action);
    public KeyboardManager getKeyboardManager();
    public MouseManager getMouseManager();
    public void update();
}
