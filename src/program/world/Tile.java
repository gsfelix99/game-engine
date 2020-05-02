package program.world;

import program.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public static BufferedImage TILE_FLOR = Game.spritesheet.getSprite(0,0,16,16);
    public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16,0,16,16);

    private  BufferedImage sprite;
    private int x, y;

    public Tile(BufferedImage sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }

}
