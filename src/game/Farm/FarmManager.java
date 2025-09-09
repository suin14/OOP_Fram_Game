package game.Farm;

import game.Hud.InventoryBar;
import game.Map.MapLoader;
import game.Map.MapsData;
import game.Other.SoundManager;

import java.awt.*;
import java.util.HashMap;

public class FarmManager {
    private final MapsData mapsData;
    private HashMap<Integer, Plant> plants;

    public FarmManager() {
        // 获取当前地图
        mapsData = MapsData.getInstance();
        plants = mapsData.nowMap.farmData;
    }

    public boolean canPlant(int tileX, int tileY) {
        // 检查是否在可播种区域内
        MapLoader nowMap = mapsData.nowMap;
        int index = tileY * nowMap.getMapWidth() + tileX;
        // System.out.println("Tile coordinates: " + tileX + ", " + tileY);
        // System.out.println("通过x,y计算出index:" + index);
        Plant plant = nowMap.farmData.get(index);

        if (plant != null) {
            if (plant.getType() == 1) {
                // System.out.println("Plant type: " + plant.getType());
                return true;
            }
        }
        return false;
    }

    public void plantSeed(int type, int tileX, int tileY) {
        MapLoader nowMap = mapsData.nowMap;
        int index = tileY * nowMap.getMapWidth() + tileX;
        Plant plant = plants.get(index);
        if (plant != null && plant.getType() == 1) {
            plants.put(index, new Plant(type,
                    (tileX * nowMap.getTileWidth() * nowMap.getScaleFactor()) - 8,  // x坐标左移8像素
                    (tileY * nowMap.getTileHeight() * nowMap.getScaleFactor()) + 4   // y坐标增加4像素间距
            ));
        }
    }

    // 处理收获或销毁
    public void handleHarvest(int tileX, int tileY) {
        MapLoader nowMap = mapsData.nowMap;
        int index = tileY * nowMap.getMapWidth() + tileX;
        // System.out.println("Tile coordinates: " + tileX + ", " + tileY);
        Plant plant = plants.get(index);

        if (plant == null) {
            System.out.println("No plant found at location: " + index);
            return;
        }

        // 添加：检查是否为可种植的空地（type == 1）
        if (plant.getType() == 1) {
            System.out.println("This is empty farmland, nothing to harvest");
            return;
        }

        if (!plant.isFullyGrown()) {
            System.out.println("Plant at " + index + " is not fully grown yet.");
            return;
        }

        // 播放收获音效
        SoundManager.playSFX("harvest.wav");

        // 根据植物类型增加对应的计数
        try {
            InventoryBar.getInstance().addItem(plant.getType());
            System.out.println("Harvested plant type " + plant.getType() + " at " + index);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding item to inventory: " + e.getMessage());
        }

        // 收获后将地块恢复为可种植状态（type=1）
        plants.put(index, new Plant(1, plant.getX(), plant.getY()));
        System.out.println("Plant harvested and land restored to plantable state");
    }


    public void update() {
        plants.values().forEach(Plant::update);
    }

    public void render(Graphics2D g, int frameWidth) {
        plants.values().forEach(plant -> {
            try {
                g.drawImage(plant.getCurrentImage(),
                        plant.getX(),
                        plant.getY() + 24,
                        32,    // 保持宽度
                        32,    // 增加高度以增加垂直间距
                        null);
            } catch (Exception e) {
                System.err.println("Error rendering plant at " + plant.getX() + "," + plant.getY());
                e.printStackTrace();
            }
        });
    }

    public HashMap<Integer, Plant> getPlants() {
        return plants;
    }

    public void setPlants(HashMap<Integer, Plant> loadedPlants) {
        this.plants = loadedPlants;
    }
} 