package com.bluebook.engine;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.physics.CollisionThread;
import com.bluebook.physics.HitDetectionHandler;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.threads.UpdateThread;
import com.bluebook.util.GameObject;
import com.topdownfuntown.objects.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class is the main engine of the game
 */
public class GameEngine {

    private UpdateThread updateThread;
    private CollisionThread collisionThread;
    private final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1);

    public ArrayList<GameObject> updateObjects = new ArrayList<>();

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    public static boolean DEBUG = false;

    private boolean isPaused = false;

    public static GameEngine singelton;

    public double FPS = 0.0;

    Canvas canvas;
    Player p;
    OrtographicCamera camera;

    /**
     * Constructor for GameEngine
     * @param canvas Canvas for game to be drawn to
     */
    public GameEngine(Canvas canvas){
        singelton = this;
        CanvasRenderer.getInstance().setCanvas(canvas);

        this.canvas = canvas;


        updateThread = new UpdateThread(this, messageQueue);
//        collisionThread = new CollisionThread();
        startAnimationTimer();

    }



    private void startAnimationTimer(){
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                CanvasRenderer.getInstance().drawAll();

                // Finding FSP for debugging
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    FPS = 1_000_000_000.0 / elapsedNanosPerFrame ;
                }
            }
        };
        timer.start();
    }

    public void Pause(){
        isPaused = true;
//        stopCollisionThread();
        stopUpdateThread();
    }

    public void unPause(){
        isPaused = false;
        startUpdateThread();
//        startCollisionThread();
    }

    public boolean isPaused(){
        return isPaused;
    }

    /**
     * Singelton getter
     * @return
     */
    public static GameEngine getInstance(){
        return singelton;
    }

    /**
     * This is used by the constructor of gameobject so it's update function is called
     * @param go
     */
    public void addGameObject(GameObject go){
        synchronized (this) {
            updateObjects.add(go);
        }
    }

    /**
     * Removes the object from updateobjects, this is called during an gameobjects destruction
     * @param go
     */
    public void removeGameObject(GameObject go){
        synchronized (this) {
            if (updateObjects.contains(go))
                updateObjects.remove(go);
        }
    }

    /**
     * This should be called every tick from the {@link UpdateThread}
     * @param delta
     */
    public void update(double delta){
        synchronized (this) {
            GameApplication ga = GameApplication.getInstance();

            //Y_scale = getScreenHeight() / Integer.parseInt(loadedSettings.get("game_resolution_Y"));

            HitDetectionHandler.getInstance().updatePositions();

            HitDetectionHandler.getInstance().lookForCollision();
            GameApplication.getInstance().update(delta);
            int lengthOfArray = updateObjects.size();
            for (int i = 0; i < lengthOfArray; i++)
                if(updateObjects.get(i).isAlive())
                    updateObjects.get(i).update(delta);
        }
        //CanvasRenderer.getInstance().drawAll();
    }

    /**
     * Will start the update thread that runs concurrently to process logic
     */
    public void startUpdateThread(){
        Thread t = new Thread(updateThread);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Will terminate logic thread
     */
    public void stopUpdateThread(){
        if(updateThread.isRunning())
            updateThread.terminate();
    }

    public void startCollisionThread(){
        Thread t = new Thread(collisionThread);
        t.setDaemon(true);
        t.start();
    }

    public void stopCollisionThread(){
        if (collisionThread.isRunning()) {
            collisionThread.terminate();
        }
    }
}
