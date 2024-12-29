package game.Farm;

import game.Other.StaticValue;
import game.Other.TimeSystem;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Chicken {
    private int x, y;           // 位置
    private boolean facingLeft; // 朝向
    private int frameIndex;     // 当前动画帧
    private long lastUpdateTime;// 上次更新时间
    private final ArrayList<Point> eggs; // 当前鸡的鸡蛋
    private static final Random random = new Random();

    private final TimeSystem timeSystem;

    public Chicken(int x, int y) {
        this.x = x;
        this.y = y;
        this.eggs = new ArrayList<>();
        this.facingLeft = random.nextBoolean();
        this.frameIndex = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.timeSystem = TimeSystem.getInstance(); // 引用时间系统
    }

    public void update() {
        // 每200ms更新一次动画帧
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > 200) {
            frameIndex = (frameIndex + 1) % 4;
            lastUpdateTime = currentTime;

            // 随机改变方向
            if (random.nextInt(100) < 5) { // 5%的概率改变方向
                facingLeft = !facingLeft;
            }

            // 随机移动
            if (random.nextInt(100) < 30) { // 30%的概率移动
                int dx = facingLeft ? -1 : 1;
                x += dx;
            }
        }

        if (timeSystem.getMinute() == 0 && timeSystem.getHour() % 2 == 0) {
            eggs.add(new Point(x, y));  // 每隔2小时下个蛋
        }
    }

    public BufferedImage getCurrentImage() {
        int row = facingLeft ? 9 : 8;  // 交换了8和9
        return StaticValue.getPlantImage(row, frameIndex);
    }

    public int getX() { return x * 48; }  // 转换为屏幕坐标
    public int getY() { return y * 48; }

    public ArrayList<Point> getEggs() { return eggs; }
} 