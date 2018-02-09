package com.bluebook.graphics;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;

public class SpriteLoader {

    private static HashMap<String, Image> images = new HashMap<>();
    /**
     * Will load a png from assets/sprite/ and return the image, to be used with spriteclass
     * @param name
     * @return
     */
    public static Image loadImage(String name){
        if(!images.containsKey(name)){
            images.put(name, new Image("file:./assets/sprite/" + name + ".png"));
        }
        return images.get(name);
    }
}
