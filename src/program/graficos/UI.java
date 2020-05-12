package program.graficos;

import program.entities.Player;
import program.main.Game;

import java.awt.*;

public class UI {

    public void render (Graphics g) {
        g.setColor(Color.red);
        g.fillRect(8, 4, 50, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4, (int) ((Game.player.life / Game.player.maxlife) * 50), 8);

        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Game.player.life + "/" + (int)Game.player.maxlife, 11, 11);
    }

}
