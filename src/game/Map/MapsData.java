package game.Map;

import game.Character.Farmer;
import game.Other.BlackScreenController;
import game.Other.SoundManager;


import java.util.HashMap;

public class MapsData {
    private final java.util.Map<String, MapLoader> maps;
    
    public MapLoader nowMap;

    private static MapsData instance;

    private Farmer pc;

    private MapsData() {
        maps = new HashMap<>();
        maps.put("farm", new MapLoader("farm.tmx", "farm.png"));
        maps.put("shop", new MapLoader("shop.tmx", "shop.png"));
        nowMap = getMap("farm");

        pc = Farmer.getInstance();
    }

    public static MapsData getInstance() {
        if (instance == null) {
            instance = new MapsData();
        }
        return instance;
    }

    public MapLoader getMap(String mapName) {
        return maps.get(mapName);
    }

    public void updadteNowMap(String mapName) {
        nowMap = maps.get(mapName);
    }
    
    // 碰撞检测
    public boolean checkCollision(int x, int y) {
        int tileX = (x - nowMap.getTileWidth()) / (nowMap.getTileWidth() * nowMap.getScaleFactor()) + 1;
        int tileY = (y - nowMap.getTileHeight()) / (nowMap.getTileWidth() * nowMap.getScaleFactor()) + 1 ;
        int index = tileY * nowMap.getMapWidth() + tileX;
//        System.out.println(tileX + ", " +  tileY);
//        System.out.println(index);

        // 如果保存的value不是-1(empty), 则代表该位置有碰撞
        return nowMap.getCollisionData().get(index) == -1;
    }

    public boolean checkWarp(int x, int y) {
        if (pc == null) {
            pc = Farmer.getInstance();
        }

        int tileX = (x - nowMap.getTileWidth()) / (nowMap.getTileWidth() * nowMap.getScaleFactor()) + 1;
        int tileY = (y - nowMap.getTileHeight()) / (nowMap.getTileWidth() * nowMap.getScaleFactor()) + 1 ;
        int index = tileY * nowMap.getMapWidth() + tileX;

        if (nowMap.getWarpsData().containsKey(index)) {
            BlackScreenController.start(); // 开始过场动画

            Warp warp = nowMap.getWarpsData().get(index);
            String loc = warp.getLocation();  // 获取目标地图文件
            int targetX = warp.getToX();  // 传送后的目标X坐标
            int targetY = warp.getToY();  // 传送后的目标Y坐标

            updadteNowMap(loc);
            SoundManager.playSFX("door.wav");
            pc.setPosition(targetX, targetY);
            return true;
        }
        return false;
    }
}
