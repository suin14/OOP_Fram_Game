package game.Map;

public class Warp {
    private final String location; // 传送地图
    private final int from;
    private final int toX;
    private final int toY;

    // 构造方法
    public Warp(String location, int from, int toX, int toY) {
        this.location = location;
        this.from = from;
        this.toX = toX;
        this.toY = toY;
    }

    public String getLocation() {
        return location;
    }

    public int getFrom() {
        return from;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }
}
