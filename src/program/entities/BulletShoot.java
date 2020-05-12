package program.entities;

import program.main.Game;
import program.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity {
    private double dx;
    private double dy;
    private double spd = 4;

    private int life = 50, curLife = 0;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick() {
        x += dx*spd;
        y += dy*spd;
        curLife ++;
        if (curLife == life) {
            Game.bulletShoots.remove(this);
            return;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
    }
}
