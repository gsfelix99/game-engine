package program.entities;

import program.main.Game;
import program.world.Camera;
import program.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.6;

    private int maskx = 4, masky = 4, maskWidth = 6, maskHeight = 6;
    private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
    private BufferedImage[] spritesEnemy;
    private int life = 10;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        spritesEnemy = new BufferedImage[2];
        spritesEnemy[0] = Game.spritesheet.getSprite(112,16,16,16);
        spritesEnemy[1] = Game.spritesheet.getSprite(112 + 16,16,16,16);
    }

    public void tick() {
        if (this.isColiddingWithPlayer() == false) {

            if (((int) x < Game.player.getX()) &&
                    World.isFree((int) (x + speed), this.getY()) &&
                    !isColidding((int) (x + speed), this.getY())) {

                x += speed;

            } else if ((int) x > Game.player.getX() &&
                    World.isFree((int) (x - speed), this.getY()) &&
                    !isColidding((int) (x - speed), this.getY())) {
                x -= speed;
            }

            if (((int) y < Game.player.getY()) &&
                    World.isFree(this.getX(), (int) (y + speed)) &&
                    !isColidding(this.getX(), (int) (y + speed))) {

                y += speed;

            } else if (((int) y > Game.player.getY()) &&
                    World.isFree(this.getX(), (int) (y - speed)) &&
                    !isColidding(this.getX(), (int) (y - speed))) {
                y -= speed;
            }
        } else {
            if (Game.random.nextInt(100) < 10) {
                /**
                 * Player damage: Random damage in the range 0 to 5
                 */
                Game.player.life -= Game.random.nextInt(5);
                Game.player.isDamage = true;
                System.out.println(Game.player.life);

            }

        }

        frames++;
        if (frames == maxFrames){
            frames = 0;
            index ++;
            if (index > maxIndex) index = 0;
        }
        
        collidingBullet();

    } // End of tick()

    private boolean collidingBullet() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof BulletShoot) {
                if (Entity.isColidding(this, e)) {

                }
            }
        }
        return false;
    }

    public boolean isColiddingWithPlayer() {
        /**
         * Create a mask over the enemies
         * and the player to test the collision
         */
        Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskWidth, maskHeight);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
        return enemyCurrent.intersects(player);
    }

    public boolean isColidding(int xnext, int ynext) {
        Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskWidth, maskHeight);
        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy enemy = Game.enemies.get(i);
            if (enemy == this) //
                continue;
            Rectangle targetEnemy = new Rectangle(enemy.getX() + maskx, enemy.getY() +masky, maskWidth, maskHeight);
            if (enemyCurrent.intersects(targetEnemy))
                return true;
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(spritesEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
}
