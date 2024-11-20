package game;

public class Warp {
    private String location; // 传送地图
    private int from;
    private int toX;
    private int toY;

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
