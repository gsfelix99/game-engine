package program.entities;

import program.graficos.Spritesheet;
import program.main.Game;
import program.world.Camera;
import program.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    public boolean right, up, left, down;
    public int right_dir = 0, left_dir = 1;
    public int dir = right_dir;
    public double speed = 1.4;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    private BufferedImage playerDamage;
    private int damageFrames = 0;

    public boolean shoot = false;
    public boolean mouseShoot = false;

    public int ammunition = 0;
    public boolean isDamage = false;

    private boolean hasGun = false;

    public double life = 100, maxlife = 100;
    public int mx, my;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDamage = Game.spritesheet.getSprite(0,16,16,16);

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
        checkColissionAmno();
        checkColissionGun();

        if (isDamage) {
            this.damageFrames ++;
            if (this.damageFrames == 6) {
                this.damageFrames = 0;
                isDamage = false;
            }
        }

        if (shoot) {
            shoot = false;
            if (hasGun && ammunition > 0) {
                ammunition--;

                int dx = 0;
                int px = 0;
                int py = 6;
                if (dir == right_dir) {
                    px = 18;
                    dx = 1;
                } else {
                    px = -8;
                    dx = -1;
                }

                BulletShoot bulletShoot = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
                Game.bulletShoots.add(bulletShoot);
            }

        }


        if (mouseShoot) {
            mouseShoot = false;
            double angle = 0;

            if (hasGun && ammunition > 0) {

                ammunition--;

                int px = 8;
                int py = 8;
                if (dir == right_dir) {
                    px = 18;
                    angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
                } else {
                    px = -8;
                    angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
                }

                double dx = Math.cos(angle);
                double dy = Math.sin(angle);


                BulletShoot bulletShoot = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
                Game.bulletShoots.add(bulletShoot);
            }
        }

        if (life <= 0) {
            Game.entities = new ArrayList<Entity>();
            Game.enemies = new ArrayList<Enemy>();
            Game.lifepackList = new ArrayList<Lifepack>();
            Game.bulletList = new ArrayList<Bullet>();

            Game.spritesheet = new Spritesheet("/spritesheet.png");

            Game.player = new Player(0,0,16,16, Game.spritesheet.getSprite(32,0,16,16));
            Game.entities.add(Game.player);
            Game.world = new World("/map.png");
            return;
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, (World.WIDTH*16) - Game.WIDTH); //
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, (World.HIGHT*16) - Game.HEIGHT);

    }

    public void render(Graphics g) {
        if (!isDamage) {
            if (dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if (hasGun) {
                    // Draw arm Right
                    g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 8, this.getY() - Camera.y, null);
                }

            } else if (dir == left_dir) {
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if (hasGun) {
                    // Draw arm Left
                    g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - 8, this.getY() - Camera.y, null);
                }
            }
        } else {
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

    public void checkColissionGun() {
        for (int i = 0; i < Game.weaponList.size(); i++) { // Scrolls through the list of entities
            Entity atual = Game.weaponList.get(i); // Current entity in position "i"
            if (atual instanceof Weapon) { // // is the current entity a Weapon ?
                if (Entity.isColidding(this, atual)) {
                    hasGun = true;
                    Game.weaponList.remove(atual);
                    Game.entities.remove(atual);
                }
            }
        }
    }


    public void checkColissionAmno() {
        for (int i = 0; i < Game.bulletList.size(); i++) { // Scrolls through the list of entities
            Entity atual = Game.bulletList.get(i); // Current entity in position "i"
            if (atual instanceof Bullet) { // // is the current entity a Bullet ?
                if (Entity.isColidding(this, atual)) {
                    ammunition += 100000000;
                    System.out.println("Munição atual: " + ammunition);
                    Game.entities.remove(atual);
                    Game.bulletList.remove(atual);
                }
            }
        }
    }




    public void checkCollisionLifePack() {
        /**
         *  This function check the collision with life pack
         *  and update the life in case of damage
         */

        for (int i = 0; i < Game.lifepackList.size(); i++) { // Scrolls through the list of entities
            Entity atual = Game.lifepackList.get(i); // Current entity in position "i"
            if (atual instanceof Lifepack) { // // is the current entity a life pack ?
                if (Entity.isColidding(this, atual)) {
                    life += 10;
                    if (life > 100)
                        life = 100;
                    Game.entities.remove(atual);
                    Game.lifepackList.remove(atual);
                }
            }
        }
    }

}
