package game.Character;

import game.Map.MapsData;
import game.Other.StaticValue;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Farmer extends Character implements Runnable {

    private static Farmer instance;

    private int dir; // 0上1右2下3左

    // 移动速度
    public final int movespeed = 8;
    private int xspreed;
    private int yspreed;
    private String newStatus; // 下一状态
    private String currentStatus; // 当前状态
    private int currentFrame = 0; // 当前动画帧

    private final MapsData mapViewer;

    private Farmer(int x, int y) {
        super(x, y);
        this.dir = 2;
        setShow(StaticValue.idle); // 默认向下站立
        this.currentStatus = "stand--down";
        this.newStatus = "stand--down";
        Thread thread = new Thread(this);
        thread.start();

        mapViewer = MapsData.getInstance();
    }

    public static Farmer getInstance(int x, int y) {
        if (instance == null) {
            instance = new Farmer(x, y);
        }
        return instance;
    }

    public static Farmer getInstance() {
        return instance;
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
        while (true) {
            if (xspreed != 0) {
                if (mapViewer.checkWarp(getX() + xspreed, getY())) {
                    continue;
                }
                if (mapViewer.checkCollision(getX() + xspreed, getY())) {
                    setX(getX() + xspreed);
                    if (getX() < 0) {
                        setX(0);
                    }
                    if (getX() > 1112) {
                        setX(1112);
                    }
                }
            }
            if (yspreed != 0) {
                if (mapViewer.checkWarp(getX(), getY() + yspreed)) {
                    continue;
                }
                if (mapViewer.checkCollision(getX(), getY() + yspreed)) {
                    setY(getY() + yspreed);
                    if (getY() < 0) {
                        setY(0);
                    }
                    if (getY() > 600) {
                        setY(600);
                    }
                }
            }

            // 改变pc图像
            switch (newStatus) {
                case "move--up" -> {
                    BufferedImage[] anim = StaticValue.walk_U;
                    updateframe(anim);
                    setShow(anim[currentFrame]);
                }
                case "move--right" -> {
                    BufferedImage[] anim = StaticValue.walk_R;
                    updateframe(anim);
                    setShow(anim[currentFrame]);
                }
                case "move--down" -> {
                    BufferedImage[] anim = StaticValue.walk_D;
                    updateframe(anim);
                    setShow(anim[currentFrame]);
                }
                case "move--left" -> {
                    BufferedImage[] anim = StaticValue.walk_L;
                    updateframe(anim);
                    setShow(anim[currentFrame]);
                }
                case "stand--up" -> {
                    BufferedImage[] anim = StaticValue.walk_U;
                    setShow(anim[2]);
                }
                case "stand--right" -> {
                    BufferedImage[] anim = StaticValue.walk_R;
                    setShow(anim[4]);
                }
                case "stand--down" -> {
                    BufferedImage[] anim = StaticValue.walk_D;
                    setShow(anim[2]);
                }
                case "stand--left" -> {
                    BufferedImage[] anim = StaticValue.walk_L;
                    setShow(anim[4]);
                }
            }
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
