package game;

import java.awt.image.BufferedImage;

public class Location {
    // 当前场景要显示的图片
    private BufferedImage bgImage = null;

    private String location; // 当前场景名字

    public Location() {

    }

    public Location(String location) {
        this.location = location;

        switch (location) {
            case "farm": bgImage = AssetManager.farm;

        }
    }

    public BufferedImage getBgImage() {
        return bgImage;
    }

    public String getLocation() {
        return location;
    }
}
