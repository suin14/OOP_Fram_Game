package game.Hud;

import game.Other.StaticValue;

import javax.swing.*;
import java.awt.*;

public class ToolBar extends JPanel {
    private static ToolBar instance;

    private static final int boxWidth = 64;
    private static final int boxHeight = 64;
    private int selectedToolIndex = 0;  // 默认选择第一个工具

    public static ToolBar getInstance() {
        if (instance == null) {
            instance = new ToolBar();
        }
        return instance;
    }

    public void setSelectedToolIndex(int selectedToolIndex) {
        this.selectedToolIndex = selectedToolIndex;
        repaint();  // 重新绘制工具栏
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int toolCount = 3;
        int startX = 1160 - (boxWidth * toolCount) / 2;
        int startY = 630;

        for (int i = 0; i < toolCount; i++) {
            g.drawImage(StaticValue.boxImage, startX + i * boxWidth, startY, boxWidth, boxHeight, null);

            // 绘制工具图片
            switch (i) {
                case 0:
                    g.drawImage(StaticValue.scytheImage, startX + i * boxWidth + 12, startY + 12, 40, 40, null);
                    break;
                case 1:
                    g.drawImage(StaticValue.potatoSeedImage, startX + i * boxWidth + 15, startY + 12, 35, 35, null);
                    break;
                case 2:
                    g.drawImage(StaticValue.tomatoSeedImage, startX + i * boxWidth + 15, startY + 12, 35, 35, null);
                    break;
            }

            // 如果是选中的工具，绘制选中框
            if (i == selectedToolIndex) {
                g.drawImage(StaticValue.chooseImage, startX + i * boxWidth, startY, boxWidth, boxHeight, null);
            }
        }
    }
}
