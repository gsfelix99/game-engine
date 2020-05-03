package program.graficos;

import program.entities.Player;

import java.awt.*;

public class UI {

    public void render (Graphics g) {
        g.setColor(Color.red);
        g.fillRect(8, 4, 50, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4, (int) ((Player.life /Player.maxlife) * 50), 8);

        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Player.life + "/" + (int)Player.maxlife, 11, 11);
    }

}
