package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticValue {
    // 背景
    public static BufferedImage farm = null;

    // PC状态
    public static BufferedImage stand_U = null;
    public static BufferedImage stand_D = null;
    public static BufferedImage stand_L = null;
    public static BufferedImage stand_R = null;

    public static BufferedImage walk_U = null;
    public static BufferedImage walk_D = null;
    public static BufferedImage walk_L = null;
    public static BufferedImage walk_R = null;

    public static String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator;

    public static void init() {
        try {
            // 加载背景图片
            farm = ImageIO.read(new File(path + "farm.png"));

            // 加载PC图片
            // 站立
            stand_U = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_u.png"));
            stand_D = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_d.png"));
            stand_L = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_l.png"));
            stand_R = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_r.png"));
            // 跑动
            walk_U = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_u.gif"));
            walk_D = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_d.gif"));
            walk_L = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_l.gif"));
            walk_R = ImageIO.read(new File(path + "Characters"  + File.separator + "actor_r.gif"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
