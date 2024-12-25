package game.Farm;

import game.Other.StaticValue;

import java.awt.image.BufferedImage;

public class Plant {
    private final int type;  // 2 或 3，对应工具类型
    private int growthStage = 0;  // 生长阶段 0-6
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
        if (growthStage < 6) {  // 未完全成熟
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= growthTime * 1000) {
                growthStage++;
                lastUpdateTime = currentTime;
                System.out.println("Plant at " + x + "," + y + " grew to stage " + growthStage); // 调试信息
            }
        }
    }

    public BufferedImage getCurrentImage() {
        // 工具2对应第9排，工具3对应第10排
        int row = (type == 2) ? 8 : 9;
        int col = 5 + growthStage;  // 从第6个开始是生长图片
        BufferedImage image = StaticValue.getPlantImage(row, col);
        if (image == null) {
            System.err.println("Failed to get plant image for stage " + growthStage); // 调试信息
        }
        return image;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isFullyGrown() {
        return growthStage >= 6;  // 第7个阶段（索引6）表示完全成熟
    }

    public int getType() {
        return type;
    }
} 