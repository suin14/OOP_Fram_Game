package game.Tool;

import game.Map.MapLoader;

public class Seed extends Tool {
    public Seed(int quantity) {
        super("Seed", quantity);
    }

    @Override
    public void use(int x, int y, MapLoader map) {
        if (map.isFarm()) {
            int index = y * map.getMapWidth() + x;
            if (map.getFarmData().containsKey(index) && map.getFarmData().get(index) == null) {
                // 种植逻辑
//种啥                map.getFarmData().put(index, new Plant("Wheat"));
                decreaseQuantity(1);
                System.out.println("种植成功！");
            } else {
                System.out.println("无法种植！");
            }
        }
    }
}
