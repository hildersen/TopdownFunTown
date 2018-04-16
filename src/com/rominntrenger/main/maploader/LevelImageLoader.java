package com.rominntrenger.main.maploader;

import java.awt.image.BufferedImage;

public class LevelImageLoader {

    public static ID[][] loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int blockSize;
        String color = new String();
        ID[][] gameMap = new ID[w][h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = image.getRGB(x, y);
                color = toHex(pixel);

                switch(color) {
                    case ("440000"):
                        gameMap[x][y] = ID.SpawnPlayer;
                    break;
                    case ("FFFFFF"):
                        gameMap[x][y] = ID.Wall;
                    break;

                    //DOORS
                    case ("DDDDDD"):
                        gameMap[x][y] = ID.Door;
                    break;
                    case ("DD0000"):
                        gameMap[x][y] = ID.DoorY;
                    break;
                    case ("DD00FF"):
                        gameMap[x][y] = ID.DoorB;
                    break;
                    case ("DDFF00"):
                        gameMap[x][y] = ID.DoorG;
                    break;
                    case ("DD0F0F"):
                        gameMap[x][y] = ID.DoorP;
                    break;
                    case ("DDF0F0"):
                        gameMap[x][y] = ID.DoorR;
                    break;

                    // KEYS
                    case ("CCCCCC"):
                        gameMap[x][y] = ID.Key;
                    break;
                    case ("CC0000"):
                        gameMap[x][y] = ID.KeyY;
                    break;
                    case ("CC00FF"):
                        gameMap[x][y] = ID.KeyB;
                    break;
                    case ("CCFF00"):
                        gameMap[x][y] = ID.KeyG;
                    break;
                    case ("CC0F0F"):
                        gameMap[x][y] = ID.KeyP;
                    break;
                    case ("CCF0F0"):
                        gameMap[x][y] = ID.KeyR;
                    break;

                    //ITEMS
                    case ("330000"):
                        gameMap[x][y] = ID.StasisBox;
                    break;
                    case ("33FFFF"):
                        gameMap[x][y] = ID.Barrel;
                    break;
                    case ("33CCCC"):
                        gameMap[x][y] = ID.Crate;
                    break;
                    case ("334444"):
                        gameMap[x][y] = ID.Corpse;
                    break;
                    case ("336666"):
                        gameMap[x][y] = ID.Ship;
                    break;
                    case ("336600"):
                        gameMap[x][y] = ID.KeyShip;
                    break;
                    case ("335555"):
                        gameMap[x][y] = ID.Table;
                    break;
                    case ("337777"):
                        gameMap[x][y] = ID.Bench;
                    break;

                    //ENEMIES
                    case ("EE2222"):
                        gameMap[x][y] = ID.AlienGreen;
                    break;
                    case ("EE4444"):
                        gameMap[x][y] = ID.AlienPurple;
                    break;
                    case ("EE6666"):
                        gameMap[x][y] = ID.AlienExplode;
                    break;
                    case ("EECCCC"):
                        gameMap[x][y] = ID.AlienTurret;
                    break;
                    default:
                        gameMap[x][y] = ID.Empty;
                    break;

                }

            }
        }
        return gameMap;
    }

        public static String toHex(int value) {
            String hexColor = String.format("%06X", (0xFFFFFF & value));
            return hexColor;
        }
        /*
        public static void main(String[] args) {
            ImageBuffering loader = new ImageBuffering();
            BufferedImage level;
            level = loader.loadImage("mapStart",32,32);
            ID[][] newLevel = loadLevel(level);
            System.out.println(cake[3][6]);
    } */
}