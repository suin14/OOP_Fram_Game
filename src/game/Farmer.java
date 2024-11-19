package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Farmer implements Runnable{
    // 坐标
    private int x;
    private int y;

    private int dir; //0上1右2下3左

    // 移动速度
    public final int movespeed = 10;
    private int xspreed;
    private int yspreed;

    private BufferedImage show;

    private String newStatus; // 下一状态
    private String currentStatus; // 当前状态
    private int currentFrame = 0; //当前动画帧

    private final Thread thread;
    private final MapLoader mapViewer;

    public Farmer(MapLoader mapViewer, int x, int y) {
        this.mapViewer = mapViewer; // 构造时传入MapLoader
        this.x = x;
        this.y = y;
        this.dir = 2;
        show = AssetManager.idle; // 默认向下站立
        this.currentStatus = "stand--down";
        this.newStatus = "stand--down";
        thread = new Thread(this);
        thread.start();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        currentFrame = 0;

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
//            System.out.println(x + ", " +  y);
            if (xspreed != 0 && mapViewer.checkCollision(x + xspreed, y)) {
                x += xspreed;
                if (x < 0) {
                    x = 0;
                }
                if (x > 1112) {
                    x = 1112;
                }
            }
            if (yspreed != 0 && mapViewer.checkCollision(x, y + yspreed)) {
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
                // 走动
                case "move--up" -> {
                    BufferedImage[] anim = AssetManager.walk_U;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--right" -> {
                    BufferedImage[] anim = AssetManager.walk_R;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--down" -> {
                    BufferedImage[] anim = AssetManager.walk_D;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                case "move--left" -> {
                    BufferedImage[] anim = AssetManager.walk_L;
                    updateframe(anim);
                    show = anim[currentFrame];
                }
                // 站立
                case "stand--up" -> {
                    BufferedImage[] anim = AssetManager.walk_U;
                    show = anim[2];
                }
                case "stand--right" -> {
                    BufferedImage[] anim = AssetManager.walk_R;
                    show = anim[4];
                }
                case "stand--down" -> {
                    BufferedImage[] anim = AssetManager.walk_D;
                    show = anim[2];
                }
                case "stand--left" -> {
                    BufferedImage[] anim = AssetManager.walk_L;
                    show = anim[4];
                }
            }
            try {
                Thread.sleep(60); // 让线程休眠80毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
