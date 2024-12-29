package game.Farm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChickenManager {
    private final List<Chicken> chickens = new ArrayList<>();
    private static final Random random = new Random();
    private boolean isVisible = true;

    public ChickenManager(ArrayList<Integer> walkableArea, int mapWidth) {
        // 生成3只小鸡
        int chickenCount = 3;

        // 从可行走区域随机选择位置生成小鸡
        List<Integer> availablePositions = new ArrayList<>();
        for (int i = 0; i < walkableArea.size(); i++) {
            if (walkableArea.get(i) == -1) {  // 修改这里：-1 表示可行走的位置
                availablePositions.add(i);
            }
        }
        System.out.println("Found " + availablePositions.size() + " available positions");

        // 生成小鸡
        for (int i = 0; i < chickenCount && !availablePositions.isEmpty(); i++) {
            int index = availablePositions.remove(random.nextInt(availablePositions.size()));
            int y = index / mapWidth;
            int x = index % mapWidth;
            chickens.add(new Chicken(x, y));
//            System.out.println("Added chicken at position: " + x + "," + y);
        }
    }

    public void update() {
        chickens.forEach(Chicken::update);
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void render(Graphics2D g) {
        if (!isVisible) return;
        
        chickens.forEach(chicken -> {
            try {
                BufferedImage image = chicken.getCurrentImage();
                if (image != null) {
                    g.drawImage(image, 
                            chicken.getX(), chicken.getY(), 
                            48, 48, null);
                }
            } catch (Exception e) {
                System.err.println("Error rendering chicken: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
} 