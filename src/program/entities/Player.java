package program.entities;

import program.main.Game;
import program.world.Camera;
import program.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean right, up, left, down;
    public int right_dir = 0, left_dir = 1;
    public int dir = right_dir;
    public double speed = 1.4;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    public static double life = 100, maxlife = 100;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];

        for (int i = 0; i < 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
            leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
        }
//        for (int i = 0; i < 4; i++) {
//            leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
//        }
    }

    public void tick() {
        moved = false;
        if (right && World.isFree((int)(x + speed), this.getY())) {
            moved = true;
            dir = right_dir;
            x += speed;
        } else if(left && World.isFree((int) (x - speed), this.getY())) {
            moved = true;
            dir = left_dir;
            x -= speed;
        }

        if (up && World.isFree(this.getX(), (int) (y - speed))) {
            moved = true;
            y -= speed;
        } else if (down && World.isFree(this.getX(), (int) (y + speed))) {
            moved = true;
            y += speed;
        }

        if (moved) {
            frames++;
            if (frames == maxFrames){
                frames = 0;
                index ++;
                if (index > maxIndex) index = 0;
            }
        }

        checkCollisionLifePack();
        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, (World.WIDTH*16) - Game.WIDTH); //
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, (World.HIGHT*16) - Game.HEIGHT);

    }

    public void render(Graphics g) {
        if (dir == right_dir) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

        } else if (dir == left_dir) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

    public void checkCollisionLifePack() {
        /**
         *  This function check the collision with life pack
         *  and update the life in case of damage
         */

        for (int i = 0; i < Game.entities.size(); i++) { // Scrolls through the list of entities
            Entity atual = Game.entities.get(i); // Current entity in position "i"
            if (atual instanceof Lifepack) { // // is the current entity a life pack ?
                if (Entity.isColidding(this, atual)) {
                    life += 10;
                    if (life > 100)
                        life = 100;
                    Game.entities.remove(atual);
                }
            }
        }
    }

}
