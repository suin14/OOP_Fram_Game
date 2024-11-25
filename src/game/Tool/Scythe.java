package game.Tool;

import game.Map.MapLoader;

public class Scythe extends Tool {
    public Scythe() {
        super("Scythe", 1);
    }

    @Override
    public void use(int x, int y, MapLoader map) {
        int index = y * map.getMapWidth() + x;
        if (map.isFarm() && map.getFarmData().containsKey(index) && map.getFarmData().get(index) != null) {
            // 收割逻辑
            map.getFarmData().put(index, null);
            System.out.println("收割成功！");
        } else {
            System.out.println("没有可以收割的作物！");
        }
    }
}
