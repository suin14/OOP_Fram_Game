package game.Farm;

import game.Hud.InventoryBar;
import game.Other.StaticValue;
import game.Other.TimeSystem;

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
    public void handleHarvest(int tileX, int tileY) {
        String key = tileX + "," + tileY;
        Plant plant = plants.get(key);

        if (plant == null) {
            System.out.println("No plant found at location: " + key);
            return;
        }

        if (!plant.isFullyGrown()) {
            System.out.println("Plant at " + key + " is not fully grown yet.");
            return;
        }

        // 根据植物类型增加对应的计数
        try {
            InventoryBar.getInstance().addItem(plant.getType());
            System.out.println("Harvested plant type " + plant.getType() + " at " + key);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding item to inventory: " + e.getMessage());
        }

        plants.remove(key);
        System.out.println("Plant removed from location: " + key);
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
    }
} 