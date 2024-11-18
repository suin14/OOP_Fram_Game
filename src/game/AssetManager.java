package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AssetManager {

    // 背景
    public static BufferedImage farm = null;

    // PC
    public static BufferedImage idle = null;
    public static BufferedImage[] walk_U = new BufferedImage[5];
    public static BufferedImage[] walk_D = new BufferedImage[5];
    public static BufferedImage[] walk_L = new BufferedImage[8];
    public static BufferedImage[] walk_R = new BufferedImage[8];

    public static String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator;

    public static void init() {
        try {
            // 加载背景图片
            farm = ImageIO.read(new File(path + "farm.png"));

            // 加载PC图片
            walk_U = loadFrames("walk_u.png", 5);
            walk_D = loadFrames("walk_d.png", 5);
            walk_L = loadFrames("walk_l.png", 8);
            walk_R = loadFrames("walk_r.png", 8);

            idle = walk_D[2];

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 导入精灵帧
    private static BufferedImage[] loadFrames(String filename, int frame) throws IOException {
        BufferedImage spriteSheet = ImageIO.read(new File(path + "Characters" + File.separator + filename));
        BufferedImage[] frames = new BufferedImage[frame];

        for (int i = 0; i < frame; i++) {
            frames[i] = spriteSheet.getSubimage(i * 32, 0, 32, 32);
        }
        return frames;
    }
}
