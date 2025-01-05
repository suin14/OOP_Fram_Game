package game.Farm;

import game.Hud.InventoryBar;
import game.Map.MapsData;
import game.Other.StaticValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChickenManager {
    // 定义活动范围（瓦片坐标）
    private static final int MIN_TILE_X = 19;
    private static final int MAX_TILE_X = 31;
    private static final int MIN_TILE_Y = 14;
    private static final int MAX_TILE_Y = 18;

    private final List<Chicken> chickens = new ArrayList<>();
    private static final Random random = new Random();
    private final MapsData mapsData;
    private boolean isVisible = true;

    public ChickenManager(ArrayList<Integer> walkableArea, int mapWidth) {
        int chickenCount = 3;
        mapsData = MapsData.getInstance();
        // 只在指定矩形范围内生成小鸡
        for (int i = 0; i < chickenCount; i++) {
            int x = random.nextInt(MAX_TILE_X - MIN_TILE_X + 1) + MIN_TILE_X;
            int y = random.nextInt(MAX_TILE_Y - MIN_TILE_Y + 1) + MIN_TILE_Y;
            chickens.add(new Chicken(x, y));
        }
    }

    public void pickEgg(Chicken chicken, int tileX, int tileY) {  // 收集蛋
        Point targetPoint = new Point(tileX, tileY);
        ArrayList<Point> eggs = chicken.getEggs();

        if (eggs.contains(targetPoint)) {
            InventoryBar.getInstance().addItem(4);
        }

        eggs.remove(targetPoint);
    }

    public List<Chicken> getChickens() {
        return chickens;
    }

    public void update() {
        chickens.forEach(chicken -> {
            int lastX = chicken.getX() / 32;
            int lastY = chicken.getY() / 32;

            chicken.update();

            // 检查是否超出范围并修正位置
            int x = chicken.getX() / 32;
            int y = chicken.getY() / 32;

            if (x < MIN_TILE_X || x  > MAX_TILE_X || y  < MIN_TILE_Y || y > MAX_TILE_Y) {
                chicken.setPosition(lastX, lastY);
            }
        });
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void render(Graphics2D g) {
        if (!isVisible) return;

        chickens.forEach(chicken -> {
            try {
                for (Point egg : chicken.getEggs()) {
                    g.drawImage(StaticValue.eggImage,
                            egg.x * 32,
                            egg.y * 32 + 25,
                            20, 20, null);
                }

                BufferedImage image = chicken.getCurrentImage();
                if (image != null) {
                    g.drawImage(image,
                            chicken.getX(),
                            chicken.getY(),
                            mapsData.nowMap.getMapWidth() * mapsData.nowMap.getScaleFactor() / 2, // 宽度缩小一半
                            mapsData.nowMap.getMapWidth() * mapsData.nowMap.getScaleFactor() / 2, // 高度缩小一半
                            null);
                }
            } catch (Exception e) {
                System.err.println("Error rendering chicken: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
} 