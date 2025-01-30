package io.github.team2;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SceneManager {
	// singleton
	private static SceneManager instance;

	private final Map<SceneID, Scene> scenes;
	// handles scene states
	private final Stack<SceneID> sceneStack;
	
	private SceneManager()
	{
		scenes = new HashMap<>();
		sceneStack = new Stack<>();
	}
	
	public static SceneManager getInstance()
	{
		if(instance == null)
			instance = new SceneManager();
		
		return instance;
	}
	
	public void addScene(SceneID id, Scene scene)
	{
		scenes.put(id, scene);
	}
	
	public void setNextScene(SceneID id)
	{
		if(!sceneStack.isEmpty())
			unloadCurrentScene();

		loadScene(id);
	}
	
	// used for pause menu
	public void overlayScene(SceneID id)
	{
		loadScene(id);
    }

	// used to return to game scene
    public void removeOverlay()
    {
    	// ensure there is always a scene
    	if(sceneStack.size() > 1)
    		unloadCurrentScene();
    }

    public Scene getCurrentScene()
    {
        return scenes.get(sceneStack.peek());
    }

    public SceneID getCurrentSceneID()
    {
    	return sceneStack.peek();
    }
    
    // run the update function of the current scene
    public void update()
    {
        if (!sceneStack.isEmpty())
            scenes.get(sceneStack.peek()).update();
    }

    public void draw(SpriteBatch batch)
    {
        for (SceneID id : sceneStack)
        	scenes.get(id).draw(batch);
    }

    public void draw(ShapeRenderer shape)
    {
        for (SceneID id : sceneStack)
            scenes.get(id).draw(shape);
    }
    
    private void loadScene(SceneID id)
    {
    	if (!scenes.containsKey(id))
    		return;

    	sceneStack.push(id);
    	scenes.get(id).load();
    	updateInputProcessor();
    }
    
    private void unloadCurrentScene()
    {
        if (sceneStack.isEmpty())
        	return;
        
        scenes.get(sceneStack.pop()).unload();
        updateInputProcessor();
    }

    private void updateInputProcessor()
    {
        if (sceneStack.isEmpty())
        	return;
        
        // sets input processor to the input manager of the next scene
        Gdx.input.setInputProcessor(scenes.get(sceneStack.peek()).getInputManager());
    }
}