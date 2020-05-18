package program.entities;

import program.main.Game;
import program.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16,0,16,16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16,0,16,16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16,16,16,16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16,16,16,16);
    public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
    public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128,0,16,16);
    public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(128 + 16,0,16,16);

    protected double x;
    protected double y;
    protected int width;
    protected int height;



    private BufferedImage sprite;
    private int maskx, masky, maskWidth, maskHeight;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskx = 0;
        this.masky = 0;
        this.maskWidth = width;
        this.maskHeight = height;
    }

    public void setMask(int maskx, int masky, int maskWidth, int maskHeight) {
        this.maskx = maskx;
        this.masky = masky;
        this.maskWidth = maskWidth;
        this.maskHeight = maskHeight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Methods Getters
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public static boolean isColidding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskWidth, e1.maskHeight);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskWidth, e2.maskHeight);

        return e1Mask.intersects(e2Mask);
    }

    public void tick() {

    }
    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
//        g.setColor(Color.red);
//        g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskWidth, maskHeight);
    }


}
