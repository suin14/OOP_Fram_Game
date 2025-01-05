package game.Farm;

import game.Other.StaticValue;

import java.awt.image.BufferedImage;

public class Plant {
    private int type;  // 2 或 3，对应工具类型,0：表示不能种，1：表示可以种
    private int growthStage = 0;  // 生长阶段 0-5
    private long lastUpdateTime;   // 上次更新时间
    private final int growthTime;  // 生长时间（秒）
    private final int x, y;        // 种植位置

    public Plant(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.growthTime = (type == 2) ? 2 : 3;  // 工具2是2秒，工具3是3秒
        this.lastUpdateTime = System.currentTimeMillis();
        System.out.println("Created plant type " + type + " at " + x + "," + y); // 调试信息
    }

    public void update() {
        if (growthStage < 5) {  // 未完全成熟
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= growthTime * 1000) {
                growthStage++;
                lastUpdateTime = currentTime;
//                System.out.println("播种在" + x + "," + y + "成长到" + growthStage); // 调试信息
            }
        }
    }

    public BufferedImage getCurrentImage() {
        // 如果type为1，表示空地，不显示图片
        if (type == 1) return null;
        
        // 工具2对应第9排，工具3对应第10排
        int row = (type == 2) ? 8 : 9;
        int col = 5 + growthStage;
        return StaticValue.getPlantImage(row, col);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isFullyGrown() {
        return growthStage >= 5;  // 第6个阶段（索引5）表示完全成熟
    }

    public int getType() {
        return type;
    }
} 