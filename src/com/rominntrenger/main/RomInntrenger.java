package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.input.GamepadInput;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.HealthElement;
import com.rominntrenger.gui.Inventory;
import com.rominntrenger.maploader.MapCreator;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.StarterWeapon;
import com.rominntrenger.objects.player.Weapon;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;

public class RomInntrenger extends GameApplication {

    OrthographicCamera cam;
    public Inventory inventory;
    double camSpeed = 15;
    public ArrayList<Player> players = new ArrayList<>();
    public HealthElement healthElement;
    public Weapon currentWeapon;

    public AudioPlayer bgMusic;

    MessageHandler msh;

    GamepadInput gi;

    @Override
    protected void onLoad() {
        super.onLoad();
        currentWeapon = new StarterWeapon(Vec2.ZERO,
            new AnimationSprite("/friendlies/arms", 2), Vec2.ZERO);
        cam = new OrthographicCamera();

        MapCreator level = new MapCreator("startMap");
        level.createLevel();
        inventory = new Inventory(6);
        healthElement = new HealthElement(new Vec2(0, 0));

        bgMusic = new AudioPlayer("./assets/audio/MoodyLoop.wav");
        bgMusic.playLoop();

        msh = MessageHandler.getInstance();

        gi = new GamepadInput();
    }

    @Override
    public void update(double delta) {
        cam.update(delta);
        gi.pullEvents();
        for(Player player : players) {
            if (gi.getNumberOfControllers() > 0) {
                int playerID = players.indexOf(player);
//                System.out.println("Getting id for player " + playerID + " and LEFT X IS " + gi.getLeftJoistick(playerID).getX());
                if (gi.getRightJoistick(playerID).getMagnitude() > 0.1) {
                    ((AnimationSprite) player.getSprite()).setPlaying(true);
                } else {
                    ((AnimationSprite) player.getSprite()).setPlaying(false);
                }
                player.move(gi.getRightJoistick(playerID), delta);

                if (gi.getLeftJoistick(playerID).getMagnitude() > 0.1) {
                    player.lookAt(
                        Vec2.subtract(player.getPosition(),
                            Vec2.multiply(gi.getLeftJoistick(playerID), -1)));
                } else if (gi.getRightJoistick(playerID).getMagnitude() > 0.1) {
                    player.lookAt(
                        Vec2.subtract(player.getPosition(),
                            Vec2.multiply(gi.getRightJoistick(playerID), -1)));
                }

                if(player.hasWeapon()) {
                    if (gi.isShoot(playerID)) {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(true);

                        player.shoot();
                    } else {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(false);
                    }
                }
            } else {
                if (input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input
                    .isKeyDown(KeyCode.A)
                    || input.isKeyDown(KeyCode.D)) {
                    ((AnimationSprite) player.getSprite()).setPlaying(true);
                } else {
                    ((AnimationSprite) player.getSprite()).setPlaying(false);
                }

                if (input.isKeyDown(KeyCode.S)) {
                    player.moveDown(delta);
                }

                if (input.isKeyDown(KeyCode.W)) {
                    player.moveUp(delta);
                }

                if (input.isKeyDown(KeyCode.D)) {
                    player.moveRight(delta);
                }

                if (input.isKeyDown(KeyCode.A)) {
                    player.moveLeft(delta);
                }

                if(player.hasWeapon()) {
                    if (input.isMouseButton0Pressed()) {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(true);

                        player.shoot();
                    } else {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(false);
                    }
                }

                // Lookat

                if (OrthographicCamera.main != null) {
                    player.lookAt(Vec2.subtract(input.getMousePosition(),
                        new Vec2(OrthographicCamera.main.getX(), OrthographicCamera.main.getY())));
                } else {
                    player.lookAt(input.getMousePosition());
                }
            }
        }

    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Player getClosestPlayere(Vec2 relativePoint){

        if(players.size() == 0)
            return null;

        Player ret = players.get(0);
        double minDistance = ret.getTransform().getGlobalPosition().distance(relativePoint);

        for(Player p : players){
            double newDist = p.getTransform().getGlobalPosition().distance(relativePoint);
            if(newDist < minDistance){
                ret = p;
                minDistance = newDist;
            }
        }

        return ret;
    }

}
