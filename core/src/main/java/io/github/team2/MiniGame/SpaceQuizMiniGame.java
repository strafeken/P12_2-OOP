package io.github.team2.MiniGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.AudioSystem.IAudioManager;
import io.github.team2.CollisionExtensions.StartMiniGameHandler;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.TextManager;
import io.github.team2.Utils.DisplayManager;

public class SpaceQuizMiniGame extends Scene {
    // Game state
    private enum GameState { READY, PLAYING, GAME_OVER }
    private GameState state;
    
    // Quiz data
    private static class QuizQuestion {
        public String question;
        public String[] options;
        public int correctAnswer;
        
        public QuizQuestion(String question, String[] options, int correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }
    
    private Array<QuizQuestion> questions;
    private int currentQuestionIndex;
    private int selectedOption;
    private boolean answered;
    private int correctAnswers;
    private float questionTimer;
    private float questionTimeLimit = 10.0f; // 10 seconds per question
    
    // Background
    private Texture backgroundTexture;
    private Color backgroundColor = new Color(0.1f, 0.1f, 0.3f, 1);
    
    // Game duration
    private float gameTime = 0;
    private float maxGameTime = 30f; // 30 seconds max
    private boolean gameCompleted = false;
    private float gameOverDelay = 2.0f;
    private float currentDelay = 0f;
    
    // Reference to managers
    private PointsManager pointsManager;
    private IAudioManager audioManager;
    private ISceneManager sceneManager;
    private StartMiniGameHandler miniGameHandler;
    
    // Shape renderer
    private ShapeRenderer localShapeRenderer;
    
    public SpaceQuizMiniGame(PointsManager pointsManager, StartMiniGameHandler miniGameHandler) {
        super();
        this.pointsManager = pointsManager;
        this.miniGameHandler = miniGameHandler;
        this.state = GameState.READY;
        
        // Initialize managers
        this.entityManager = new EntityManager();
        this.gameInputManager = new GameInputManager();
        this.textManager = new TextManager();
        this.audioManager = AudioManager.getInstance();
        this.sceneManager = SceneManager.getInstance();
        this.localShapeRenderer = new ShapeRenderer();
        
        // Initialize quiz questions
        questions = new Array<>();
        setupQuizQuestions();
    }
    
    private void setupQuizQuestions() {
        // Add questions about space and recycling
        questions.add(new QuizQuestion(
            "Which of these materials can be recycled?",
            new String[]{"Glass bottles", "Used tissues", "Bubble wrap", "Greasy pizza boxes"},
            0
        ));
        
        questions.add(new QuizQuestion(
            "What is space debris also known as?",
            new String[]{"Space trash", "Orbital junk", "Cosmic waste", "Star waste"},
            1
        ));
        
        questions.add(new QuizQuestion(
            "How long does it take a plastic bottle to decompose?",
            new String[]{"10-20 years", "50-100 years", "400-500 years", "1000+ years"},
            2
        ));
        
        questions.add(new QuizQuestion(
            "Which is NOT a method to clean up orbital debris?",
            new String[]{"Harpoons", "Nets", "Lasers", "Fans"},
            3
        ));
        
        questions.add(new QuizQuestion(
            "What percentage of plastic ever produced has been recycled?",
            new String[]{"About 9%", "About 25%", "About 50%", "About 75%"},
            0
        ));
        
        questions.add(new QuizQuestion(
            "Which is a major issue with space debris?",
            new String[]{"It blocks sunlight", "It causes collisions", "It causes radiation", "It creates acid rain"},
            1
        ));
        
        questions.add(new QuizQuestion(
            "What symbol is used internationally for recycling?",
            new String[]{"A triangle of arrows", "A star", "A circle with a line", "A hexagon"},
            0
        ));
    }
    
    @Override
    public void load() {
        try {
            backgroundTexture = new Texture(Gdx.files.internal("space_background.jpg"));
        } catch (Exception e) {
            System.err.println("Error loading background texture: " + e.getMessage());
        }
        
        // Reset game state
        currentQuestionIndex = 0;
        selectedOption = 0;
        answered = false;
        correctAnswers = 0;
        gameTime = 0;
        questionTimer = questionTimeLimit;
        gameCompleted = false;
        currentDelay = 0f;
        
        // Shuffle the questions for variety
        questions.shuffle();
        
        // Play mini-game music
        audioManager.playSoundEffect("minigame");
        
        state = GameState.READY;
    }
    
    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        gameTime += deltaTime;
        
        // Check for game completion based on time or questions
        if ((gameTime >= maxGameTime || currentQuestionIndex >= questions.size) && state == GameState.PLAYING) {
            completeGame(true);
            return;
        }
        
        switch (state) {
            case READY:
                // Press space to start
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    state = GameState.PLAYING;
                    gameTime = 0;
                }
                break;
                
            case PLAYING:
                // Update question timer
                if (!answered) {
                    questionTimer -= deltaTime;
                    
                    // Move to next question if time runs out
                    if (questionTimer <= 0) {
                        audioManager.playSoundEffect("hit");
                        moveToNextQuestion();
                    }
                }
                
                // Handle player input
                if (!answered) {
                    // Move selection up and down
                    if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && selectedOption > 0) {
                        selectedOption--;
                        audioManager.playSoundEffect("jump");
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && 
                              selectedOption < questions.get(currentQuestionIndex).options.length - 1) {
                        selectedOption++;
                        audioManager.playSoundEffect("jump");
                    }
                    
                    // Select answer
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        answered = true;
                        
                        // Check if answer is correct
                        if (selectedOption == questions.get(currentQuestionIndex).correctAnswer) {
                            correctAnswers++;
                            audioManager.playSoundEffect("ding");
                        } else {
                            audioManager.playSoundEffect("hit");
                        }
                        
                        // Wait briefly before moving to next question
                        questionTimer = 2.0f; // 2 seconds to show result
                    }
                } else {
                    // After answering, wait for timer to move to next question
                    questionTimer -= deltaTime;
                    if (questionTimer <= 0) {
                        moveToNextQuestion();
                    }
                }
                break;
                
            case GAME_OVER:
                // Wait for delay, then return to game
                currentDelay += deltaTime;
                if (currentDelay >= gameOverDelay) {
                    // Use Q key to return immediately
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                        completeGame(true);
                    }
                    // Auto-return to game after delay
                    else if (currentDelay >= gameOverDelay * 2) {
                        completeGame(true);
                    }
                }
                break;
        }
    }
    
    private void moveToNextQuestion() {
        currentQuestionIndex++;
        selectedOption = 0;
        answered = false;
        
        // Check if we've reached the end of questions
        if (currentQuestionIndex >= questions.size) {
            state = GameState.GAME_OVER;
            currentDelay = 0;
        } else {
            questionTimer = questionTimeLimit;
        }
    }
    
    private void completeGame(boolean success) {
        gameCompleted = true;
        
        // Calculate score based on number of correct answers
        int finalScore = correctAnswers * 50;
        pointsManager.addPoints(finalScore);
        System.out.println("Space Quiz completed! Added " + finalScore + " points from " + correctAnswers + " correct answers.");
        
        // Stop mini-game music
        audioManager.stopSoundEffect("minigame");
        
        // Return to main game
        PlayerStatus.getInstance().setInMiniGame(false);
        sceneManager.removeOverlay();
        
        // Notify handler that mini-game is completed
        if (miniGameHandler != null) {
            miniGameHandler.onMiniGameCompleted();
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        // End the batch before drawing shapes and begin it again afterward
        batch.end();
        
        // Draw background
        localShapeRenderer.begin(ShapeType.Filled);
        localShapeRenderer.setColor(backgroundColor);
        localShapeRenderer.rect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        localShapeRenderer.end();
        
        // Restart the batch for sprites
        batch.begin();
        
        // Draw background texture if available
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
        }
        
        switch (state) {
            case READY:
                textManager.draw(batch, "SPACE QUIZ", DisplayManager.getScreenWidth()/2 - 100,
                              DisplayManager.getScreenHeight()/2 + 50, Color.YELLOW);
                textManager.draw(batch, "Test your knowledge about space debris and recycling!",
                              DisplayManager.getScreenWidth()/2 - 250, DisplayManager.getScreenHeight()/2, Color.WHITE);
                textManager.draw(batch, "Use UP/DOWN arrows to select an answer",
                              DisplayManager.getScreenWidth()/2 - 200, DisplayManager.getScreenHeight()/2 - 50, Color.WHITE);
                textManager.draw(batch, "Press SPACE to start",
                              DisplayManager.getScreenWidth()/2 - 120, DisplayManager.getScreenHeight()/2 - 100, Color.WHITE);
                break;
                
            case PLAYING:
                if (currentQuestionIndex < questions.size) {
                    QuizQuestion currentQ = questions.get(currentQuestionIndex);
                    
                    // Draw question number and timer
                    textManager.draw(batch, "Question " + (currentQuestionIndex + 1) + "/" + questions.size,
                                  20, DisplayManager.getScreenHeight() - 20, Color.WHITE);
                    textManager.draw(batch, "Time: " + (int)questionTimer,
                                  DisplayManager.getScreenWidth() - 100, DisplayManager.getScreenHeight() - 20, Color.WHITE);
                    
                    // Draw progress
                    textManager.draw(batch, "Correct: " + correctAnswers,
                                  DisplayManager.getScreenWidth()/2 - 50, DisplayManager.getScreenHeight() - 20, Color.GREEN);
                    
                    // Draw question
                    drawWrappedText(batch, currentQ.question, 
                                  DisplayManager.getScreenWidth()/2 - 300, DisplayManager.getScreenHeight() - 80, 
                                  600, Color.YELLOW);
                    
                    // Draw options
                    float optionY = DisplayManager.getScreenHeight() - 200;
                    for (int i = 0; i < currentQ.options.length; i++) {
                        String prefix = (i == selectedOption) ? ">> " : "   ";
                        Color optionColor;
                        
                        if (answered) {
                            // Show correct/incorrect after answering
                            if (i == currentQ.correctAnswer) {
                                optionColor = Color.GREEN;
                            } else if (i == selectedOption && selectedOption != currentQ.correctAnswer) {
                                optionColor = Color.RED;
                            } else {
                                optionColor = Color.WHITE;
                            }
                        } else {
                            // Before answering, highlight selection
                            optionColor = (i == selectedOption) ? Color.YELLOW : Color.WHITE;
                        }
                        
                        textManager.draw(batch, prefix + currentQ.options[i],
                                      DisplayManager.getScreenWidth()/2 - 250, optionY - (i * 50), optionColor);
                    }
                }
                break;
                
            case GAME_OVER:
                textManager.draw(batch, "QUIZ COMPLETE!", DisplayManager.getScreenWidth()/2 - 120,
                              DisplayManager.getScreenHeight()/2 + 50, Color.YELLOW);
                textManager.draw(batch, "You got " + correctAnswers + " out of " + questions.size + " questions correct!",
                              DisplayManager.getScreenWidth()/2 - 250, DisplayManager.getScreenHeight()/2, Color.WHITE);
                textManager.draw(batch, "Points earned: " + (correctAnswers * 50),
                              DisplayManager.getScreenWidth()/2 - 120, DisplayManager.getScreenHeight()/2 - 50, Color.GREEN);
                textManager.draw(batch, "Returning to game...",
                              DisplayManager.getScreenWidth()/2 - 140, DisplayManager.getScreenHeight()/2 - 100, Color.WHITE);
                
                // Show countdown
                int remainingTime = (int)(gameOverDelay * 2 - currentDelay);
                textManager.draw(batch, "(" + remainingTime + ")",
                              DisplayManager.getScreenWidth()/2 - 20, DisplayManager.getScreenHeight()/2 - 150, Color.WHITE);
                
                // Optional quick return
                if (currentDelay >= gameOverDelay) {
                    textManager.draw(batch, "Press Q to return now",
                                  DisplayManager.getScreenWidth()/2 - 140, DisplayManager.getScreenHeight()/2 - 200, Color.WHITE);
                }
                break;
        }
    }
    
    private void drawWrappedText(SpriteBatch batch, String text, float x, float y, float maxWidth, Color color) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineY = y;
        
        for (String word : words) {
            String oldLine = line.toString();
            String newLine = oldLine.length() > 0 ? oldLine + " " + word : word;
            float width = textManager.getFont().draw(batch, newLine, 0, 0).width;
            
            if (width > maxWidth && !oldLine.isEmpty()) {
                // Draw current line and move to next line
                textManager.draw(batch, oldLine, x, lineY, color);
                line = new StringBuilder(word);
                lineY -= 30; // Line spacing
            } else {
                // Add word to current line
                if (oldLine.length() > 0) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        
        // Draw the last line
        if (line.length() > 0) {
            textManager.draw(batch, line.toString(), x, lineY, color);
        }
    }
    
    @Override
    public void draw(ShapeRenderer shape) {
        // No custom shape rendering needed for quiz game
    }
    
    @Override
    public void unload() {
        // If somehow the game gets unloaded without completing properly,
        // make sure we clean up the player status flag
        if (!gameCompleted) {
            PlayerStatus.getInstance().setInMiniGame(false);
        }
        
        dispose();
    }
    
    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
            backgroundTexture = null;
        }
        if (localShapeRenderer != null) {
            localShapeRenderer.dispose();
            localShapeRenderer = null;
        }
    }
} 