package game.Hud;

import game.Map.MapsData;
import game.Other.StaticValue;
import game.Map.MapLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryBar extends JPanel {
    private static InventoryBar instance;

    private final MapsData mapViewer;

    private static final int boxWidth = 64;
    private static final int boxHeight = 90;

    private List<Integer> itemCounts = new ArrayList<>(); // 物品数量

    public InventoryBar() {
        mapViewer = MapsData.getInstance();
        itemCounts = new ArrayList<>(List.of(0, 0, 0, 0, 0));
    }

    public static InventoryBar getInstance() {
        if (instance == null) {
            instance = new InventoryBar();
        }
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        MapLoader nowMap = mapViewer.nowMap;
        super.paintComponent(g);
        int startY = (int) (nowMap.getTileHeight() * nowMap.getMapHeight() * 0.3); // 顶部空

        // 设置字体
        g.setFont(StaticValue.pixelFont);
        g.setColor(Color.WHITE); // 字体颜色设为白色

        int currentY = startY;

        // 遍历物品，绘制Box、图片和数量
        for (int i = 0; i < 5; i++) {
            // 绘制Box
            g.drawImage(StaticValue.boxImage, 10, currentY, boxWidth, boxHeight, null);

            // 绘制物品图片
            int itemSize = (int) (boxHeight * 0.35); // 物品图片高度占Box的40%
            int offsetX = (boxWidth - itemSize) / 2; // 水平居中
            int offsetY = (int) (boxHeight * 0.2); // 顶部空白

            switch (i) {
                case 0:
                    g.drawImage(StaticValue.moneyImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
                    break;
                case 1:
                    g.drawImage(StaticValue.potatoImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
                    break;
                case 2:
                    g.drawImage(StaticValue.tomatoImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
                    break;
                case 3:
                    g.drawImage(StaticValue.eggImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
                    break;
                case 4:
                    g.drawImage(StaticValue.milkImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
                    break;
            }

            // 绘制物品数量
            String count = String.valueOf(itemCounts.get(i));
            int textX = 10 + boxWidth / 2 - g.getFontMetrics().stringWidth(count) / 2; // 水平居中
            int textY = currentY + boxHeight - 23; // 靠近Box底部
            g.drawString(count, textX, textY);

            // 更新Y坐标为下一个Box的起始位置
            currentY += boxHeight;
        }
    }

    public void addItem(int itemID) {
        if (itemID >= 1 && itemID <= itemCounts.size()) {
            itemCounts.set(itemID - 1, itemCounts.get(itemID - 1) + 1);
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid item ID: " + itemID);
        }
    }
}
