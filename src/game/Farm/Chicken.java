package game.Farm;

import game.Other.StaticValue;
import game.Other.TimeSystem;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Chicken implements Serializable {
    private int x, y;           // 位置
    private boolean facingLeft; // 朝向

    private int frameIndex;     // 当前动画帧
    private long lastUpdateTime;// 上次更新时间
    public ArrayList<Point> eggs; // 当前鸡的鸡蛋
    private static final Random random = new Random();

    private final TimeSystem timeSystem;

    private Point currentDestination;  // 添加这个属性

    public Chicken(int x, int y) {
        this.x = x;
        this.y = y;
        this.eggs = new ArrayList<>();
        this.facingLeft = random.nextBoolean();
        this.frameIndex = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.timeSystem = TimeSystem.getInstance(); // 引用时间系统
        this.currentDestination = new Point(x, y);  // 初始化目标位置
    }

    public void update() {
        // 每500ms更新一次动画帧
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > 500) {
            frameIndex = (frameIndex + 1) % 4;
            lastUpdateTime = currentTime;

            // 随机改变方向
            if (random.nextInt(100) < 20) { // 5%的概率改变方向
                facingLeft = !facingLeft;
            }

            // 随机移动
            if (random.nextInt(100) < 80) {
                int dx = facingLeft ? -1 : 1;
                x += dx;
            }
            
            // 随机上下移动
            if (random.nextInt(100) < 30) {
                int dy = random.nextBoolean() ? -1 : 1;
                y += dy;
            }
        }

        if (timeSystem.getMinute() % 30 == 0 && timeSystem.getSecond() == 0) {
            eggs.add(new Point(x, y));  // 每隔30分钟下个蛋
            System.out.println("egg");
        }
    }

    public BufferedImage getCurrentImage() {
        int row = facingLeft ? 9 : 8;  // 交换了8和9
        return StaticValue.getPlantImage(row, frameIndex);
    }

    public int getX() { 
        return x * 16 * 2; // tileWidth(16) * scaleFactor(2)
    }
    
    public int getY() { 
        return y * 16 * 2; // tileHeight(16) * scaleFactor(2)
    }

    public ArrayList<Point> getEggs() { return eggs; }

    public void setEggs(ArrayList<Point> eggs) {
        this.eggs = eggs;
    }

    public boolean needsNewDestination() {
        return currentDestination == null || 
               (Math.abs(x - currentDestination.x) < 5 && Math.abs(y - currentDestination.y) < 5);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDestination(int x, int y) {
        this.currentDestination = new Point(x, y);
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}