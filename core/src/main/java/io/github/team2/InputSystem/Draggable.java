package io.github.team2.InputSystem;

public interface Draggable {
    public void startDragging();
    public void updateDragging();
    public void stopDragging();
    public boolean isDragging();
}
