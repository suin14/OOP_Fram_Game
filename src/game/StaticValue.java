package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticValue {
    // 背景
    public static BufferedImage bg = null;

    // 地图
    public static String[] locations = {"farm"};

    public static String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator;

    public static void init() {
        // 加载背景图片
        try {
            bg = ImageIO.read(new File(path + "farm.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
