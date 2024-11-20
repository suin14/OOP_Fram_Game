package game;

import java.util.HashMap;

public class MapsData {
    private java.util.Map<String, Map> maps;
    
    public Map nowMap;

    private static MapsData instance;

    private Farmer pc;
    private final BlackScreenController blackScreenController;

    private MapsData() {
        maps = new HashMap<>();
        maps.put("farm", new Map(StaticValue.mapPath + "farm.tmx", StaticValue.mapPath + "farm.png"));
        maps.put("shop", new Map(StaticValue.mapPath + "shop.tmx", StaticValue.mapPath + "shop.png"));
        nowMap = getMap("farm");

        pc = Farmer.getInstance();
        blackScreenController = BlackScreenController.getInstance(); //过场动画
    }

    public static MapsData getInstance() {
        if (instance == null) {
            instance = new MapsData();
        }
        return instance;
    }

    public Map getMap(String mapName) {
        return maps.get(mapName);
    }

    public void updadteNowMap(String mapName) {
        nowMap = maps.get(mapName);
    }
    
    // 碰撞检测
    public boolean checkCollision(int x, int y) {
        int tileX = (x - 16) / (16 * 3) + 1;
        int tileY = (y - 16) / (16 * 3) + 1 ;
//        System.out.println(tileX + ", " +  tileY);
        int index = tileY * nowMap.getMapWidth() + tileX;
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

        for (Warp warp : nowMap.getWarps()) {
            if (warp.getFrom() == index) {
                blackScreenController.startBlackScreen(); // 开始过场动画

                String loc = warp.getLocation();  // 获取目标地图文件
                int targetX = warp.getToX();  // 传送后的目标X坐标
                int targetY = warp.getToY();  // 传送后的目标Y坐标

                updadteNowMap(loc);
                pc.setPosition(targetX, targetY);

                return true;
            }
        }
        return false;
    }
}
