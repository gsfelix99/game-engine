package program.world;

import program.entities.*;
import program.main.Game;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private static Tile[] tiles;
    public static int WIDTH, HIGHT;
    public static final int TITLE_SIZE = 16;


    public World(String path) {

        try {

            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];

            WIDTH = map.getWidth();
            HIGHT = map.getHeight();

            tiles = new Tile[ map.getWidth() * map.getHeight() ];
            map.getRGB(0,0, map.getWidth(), map.getHeight(), pixels,0, map.getWidth());

            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {

                    int pixelAtual = pixels[xx + (yy*map.getWidth())];
                    tiles[ xx + (yy * WIDTH) ] = new FlorTile(Tile.TILE_FLOR, 16*xx, 16*yy);

                    if ( pixelAtual == 0xFF000000) {
                        //Flor
                        tiles[ xx + (yy * WIDTH) ] = new FlorTile(Tile.TILE_FLOR, 16*xx, 16*yy);

                    } else if (pixelAtual == 0xFFFFFFFF) {
                        //Wall
                        tiles[ xx + (yy * WIDTH) ] = new WallTile(Tile.TILE_WALL, 16*xx, 16*yy);

                    } else if ( pixelAtual == 0xFFFF0004) {
                        //Enemy
                        BufferedImage[] buf = new BufferedImage[2];
                        buf[0] = Game.spritesheet.getSprite(112,16,16,16);
                        buf[1] = Game.spritesheet.getSprite(112 + 16,16,16,16);

                        Enemy en = new Enemy(xx*16, yy*16,16,16, Entity.ENEMY_EN);
                        Game.entities.add(en);
                        Game.enemies.add(en);

                    } else if (pixelAtual == 0xFF0026FF ){
                        Game.player.setX( 16 * xx );
                        Game.player.setY( yy * 16 );
                    } else if ( pixelAtual == 0xFFFF7063 ) {
                        //Life
                        Lifepack pack = new Lifepack(xx*16, yy*16,16,16, Entity.LIFEPACK_EN);
                        Game.lifepackList.add(pack);
                        Game.entities.add(pack);

                    } else if ( pixelAtual ==  0xFFFF6A00 ) {
                        //Arm
                        Game.entities.add(new Weapon(xx*16, yy*16,16,16, Entity.WEAPON_EN));

                    } else if ( pixelAtual == 0xFFFFD800) {
                        //Bullet
                        Game.entities.add(new Bullet(xx*16, yy*16,16,16, Entity.BULLET_EN));

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFree(int xnext, int ynext) {
        int x1 = xnext / TITLE_SIZE;
        int y1 = ynext / TITLE_SIZE;

        int x2 = (xnext + (TITLE_SIZE - 1)) / TITLE_SIZE;
        int y2 = ynext / TITLE_SIZE;

        int x3 = xnext / TITLE_SIZE;
        int y3 = (ynext + (TITLE_SIZE - 1)) / TITLE_SIZE;

        int x4 = (xnext + (TITLE_SIZE - 1)) / TITLE_SIZE;
        int y4 = (ynext + (TITLE_SIZE - 1)) / TITLE_SIZE;

        return !( (tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
                  (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
                  (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
                  (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile)  );

    }

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + (Game.WIDTH >> 4);
        int yfinal = ystart + (Game.HEIGHT >> 4);

        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HIGHT)
                    continue;
                Tile tile = tiles[xx + (yy*WIDTH)];
                tile.render(g);
            }
        }
    }
}
