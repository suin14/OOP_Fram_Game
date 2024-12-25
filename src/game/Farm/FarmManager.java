package game.Farm;

import game.Other.StaticValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class FarmManager {
    private final HashMap<String, Plant> plants = new HashMap<>();
    private int harvestedCount2 = 0;  // 工具2种植物的收获计数
    private int harvestedCount3 = 0;  // 工具3种植物的收获计数

    public boolean canPlant(Boolean isFarm, int x, int y) {
        String key = x + "," + y;
        boolean b = isFarm && !plants.containsKey(key);
        System.out.println("角色坐标：" + x + "," + y + "当前地图数值：" + isFarm);
        return b;
    }

    public void plantSeed(int type, int x, int y) {
        String key = x + "," + y;
        if (!plants.containsKey(key)) {
            plants.put(key, new Plant(type, x, y));
        }
    }

    // 处理收获或销毁
    public void handleHarvest(int x, int y) {
        String key = x + "," + y;
        Plant plant = plants.get(key);
        if (plant != null) {
            if (plant.isFullyGrown()) {
                // 根据植物类型增加对应的计数
                if (plant.getType() == 2) {
                    harvestedCount2++;
                    System.out.println("Harvested plant type 2! Total: " + harvestedCount2);
                } else {
                    harvestedCount3++;
                    System.out.println("Harvested plant type 3! Total: " + harvestedCount3);
                }
            }
            plants.remove(key);
        }
    }

    public void update() {
        plants.values().forEach(Plant::update);
    }

    public void render(Graphics2D g, int frameWidth) {
        // 渲染植物
        plants.values().forEach(plant -> {
            try {
                g.drawImage(plant.getCurrentImage(),
                        plant.getX() * 48,
                        plant.getY() * 48,
                        48, 48, null);
            } catch (Exception e) {
                System.err.println("Error rendering plant at " + plant.getX() + "," + plant.getY());
                e.printStackTrace();
            }
        });

        // 设置字体和颜色
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // 渲染工具2的植物收获计数
        BufferedImage plant2Image = StaticValue.getPlantImage(8, 11); // 工具2的成熟植物图片
        g.drawImage(plant2Image, frameWidth - 80, 50, 32, 32, null);
        g.drawString("" + harvestedCount2, frameWidth - 80, 82);

        // 渲染工具3的植物收获计数
        BufferedImage plant3Image = StaticValue.getPlantImage(9, 11); // 工具3的成熟植物图片
        g.drawImage(plant3Image, frameWidth - 80, 100, 32, 32, null);
        g.drawString("" + harvestedCount3, frameWidth - 80, 132);
    }
} 