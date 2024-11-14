package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Farmer implements Runnable{
    // 横纵坐标
    private int x;
    private int y;

    private int dir; //0上1右2下3左

    // 移动速度
    private int xspreed;
    private int yspreed;

    // 当前状态
    private String status;
    private BufferedImage show = null;

    private Location loc = new Location();

    // 实现PC动作
    private Thread thread = null;

    public Farmer() {

    }

    public Farmer(int x, int y) {
        this.x = x;
        this.y = y;
        this.dir = 2;
        show = StaticValue.stand_D; // 默认向下站立
        this.status = "stand--down";
        thread = new Thread(this);
        thread.start();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getShow() {
        return show;
    }

    // 移动
    public void move(int direction) {
        switch (direction) {
            case 0:
                dir = direction;
                xspreed = 0;
                yspreed = -5;
                status = "move--up";
                break;
            case 1:
                dir = direction;
                xspreed = 5;
                yspreed = 0;
                status = "move--right";
                break;
            case 2:
                dir = direction;
                xspreed = 0;
                yspreed = 5;
                status = "move--down";
                break;
            case 3:
                dir = direction;
                xspreed = -5;
                yspreed = 0;
                status = "move--left";
        }
    }

    public void stop() {
        xspreed = 0;
        yspreed = 0;

        switch (dir) {
            case 0:
                status = "stop--up";
                break;
            case 1:
                status = "stop--right";
                break;
            case 2:
                status = "stop--down";
                break;
            case 3:
                status = "stop--left";
        }
    }

    @Override
    public void run() {
        while(true) {
            if (xspreed != 0) {
                x += xspreed;
                // 判断pc是否到底地图最左边
                if (x < 0) {
                    x = 0;
                }
            }
            if (yspreed != 0) {
                y += yspreed;
                // 判断pc是否到底地图最下边
                if (y < 0) {
                    y = 0;
                }
            }

            // 改变pc图像
            if (status.equals("move--up")) {
                show = StaticValue.walk_U;
            } else if (status.equals("move--right")) {
                show = StaticValue.walk_R;
            } else if (status.equals("move--down")) {
                show = StaticValue.walk_D;
            } else if (status.equals("move--left")) {
                show = StaticValue.walk_L;
            } else if (status.equals("stand--up")) {
                show = StaticValue.stand_U;
            } else if (status.equals("stand--right")) {
                show = StaticValue.stand_R;
            } else if (status.equals("stand--down")) {
                show = StaticValue.stand_D;
            } else if (status.equals("stand--left")) {
                show = StaticValue.stand_L;
            }

            try {
                Thread.sleep(50); // 让线程休眠50毫秒
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
