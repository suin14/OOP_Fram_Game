package game.Other;

import javax.swing.*;
import java.awt.*;

public class PauseMenuPanel extends JPanel{
    private static boolean isPause = false;

    public void hidePanel() {
        isPause = false;
    }

    public void showPanel() {
        isPause = true;
//        SoundManager.stopBGM();
    }

    public boolean isPause() {
        return isPause;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (isPause()) {
            g2d.drawImage(StaticValue.menuPanel, 450, 150, StaticValue.menuPanel.getWidth() * 3, StaticValue.menuPanel.getHeight() * 3, this);
        }
    }
}
