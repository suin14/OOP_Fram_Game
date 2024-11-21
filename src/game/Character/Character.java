package game.Character;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Character {
    // 坐标
    private int x;
    private int y;

    private BufferedImage show;

    public Character(int x, int y) {
        setPosition(x, y);
    }

    public void setPosition(int x, int y) {
        setX(x * 48 - 8);
        setY(y * 48 - 16);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setShow(BufferedImage show) {
        this.show = show;
    }

    public Image getShow() {
        return show;
    }

    public void interact() {
    }
}
