package game.Character;

import game.Map.MapsData;
import game.Other.StaticValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Shoper {
    // 单例实例
    private static Shoper instance;

    // 坐标
    private int x;
    private int y;

    private BufferedImage show;

    private Shoper(int x, int y) {
        setX(x);
        setY(y);
        show = StaticValue.shoper;
    }

    public static Shoper getInstance(int x, int y) {
        if (instance == null) {
            instance = new Shoper(x, y);
        }
        return instance;
    }

    public void setX(int x) {
        this.x = x * 48 - 8;
    }

    public void setY(int y) {
        this.y = y * 48 - 16;
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

    public void interact() {

    }
}
