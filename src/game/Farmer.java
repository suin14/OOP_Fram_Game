package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Farmer implements Runnable{
    // 横纵坐标
    private int x;
    private int y;

    private int dir; //0上1右2下3左

    private final int movespeed = 8;

    // 移动速度
    private int xspreed;
    private int yspreed;

    private String newStatus; // 下一状态
    private String currentStatus; // 当前状态
    private BufferedImage show = null;
    private int currentFrame = 0; //当前动画帧


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
        this.currentStatus = "stand--down";
        this.newStatus = "stand--down";
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

    public Image getShow() {
        return show;
    }

    // 移动
    public void move(int direction) {
        switch (direction) {
            case 0 -> {
                dir = direction;
                xspreed = 0;
                yspreed = -movespeed;
                newStatus = "move--up";
            }
            case 1 -> {
                dir = direction;
                xspreed = movespeed;
                yspreed = 0;
                newStatus = "move--right";
            }
            case 2 -> {
                dir = direction;
                xspreed = 0;
                yspreed = movespeed;
                newStatus = "move--down";
            }
            case 3 -> {
                dir = direction;
                xspreed = -movespeed;
                yspreed = 0;
                newStatus = "move--left";
            }
        }
    }

    public void stop() {
        xspreed = 0;
        yspreed = 0;

        switch (dir) {
            case 0 -> newStatus = "stand--up";
            case 1 -> newStatus = "stand--right";
            case 2 -> newStatus = "stand--down";
            case 3 -> newStatus = "stand--left";
        }
    }

    private void updateframe(BufferedImage[] anim) {
        if (Objects.equals(newStatus, currentStatus)) {
            currentFrame = (currentFrame + 1) % anim.length; // 动画帧循环
        } else {
            currentFrame = 0; // 进入新状态时重置帧
            currentStatus = newStatus;
        }
    }

    @Override
    public void run() {
        while(true) {
            if (xspreed != 0) {
                x += xspreed;
                if (x < 0) {
                    x = 0;
                }
                if (x > 1112) {
                    x = 1112;
                }
            }
            if (yspreed != 0) {
                y += yspreed;

                // 判断pc是否到底地图最下边
                if (y < 0) {
                    y = 0;
                }
                if (y > 600) {
                    y = 600;
                }
            }

            // 改变pc图像
            switch (newStatus) {
                case "move--up" -> {
                    BufferedImage[] anim = StaticValue.walk_U;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--right" -> {
                    BufferedImage[] anim = StaticValue.walk_R;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--down" -> {
                    BufferedImage[] anim = StaticValue.walk_D;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--left" -> {
                    BufferedImage[] anim = StaticValue.walk_L;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "stand--up" -> {
                    BufferedImage[] anim = StaticValue.walk_U;
                    currentFrame = 0;
                    show = anim[2];
                }
                case "stand--right" -> {
                    BufferedImage[] anim = StaticValue.walk_R;
                    currentFrame = 0;
                    show = anim[4];
                }
                case "stand--down" -> {
                    BufferedImage[] anim = StaticValue.walk_D;
                    currentFrame = 0;
                    show = anim[2];
                }
                case "stand--left" -> {
                    BufferedImage[] anim = StaticValue.walk_L;
                    currentFrame = 0;
                    show = anim[4];
                }
            }

            try {
                Thread.sleep(80); // 让线程休眠50毫秒
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
