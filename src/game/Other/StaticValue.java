package game.Other;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class StaticValue {
    // PC
    public static BufferedImage idle = null;
    public static BufferedImage[] walk_U = new BufferedImage[5];
    public static BufferedImage[] walk_D = new BufferedImage[5];
    public static BufferedImage[] walk_L = new BufferedImage[8];
    public static BufferedImage[] walk_R = new BufferedImage[8];

    // npc
    public static BufferedImage shoper = null;

    public static void init() {
        try {
            // 加载PC图片
            walk_U = loadFrames("walk_u.png", 5);
            walk_D = loadFrames("walk_d.png", 5);
            walk_L = loadFrames("walk_l.png", 8);
            walk_R = loadFrames("walk_r.png", 8);
            idle = walk_D[2];

            // 加载npc图片
            shoper = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/characters/npc.png"),
                    "Resource not found: assets/characters/npc.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 导入精灵帧
    private static BufferedImage[] loadFrames(String filename, int frame) throws IOException {
        BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(
                StaticValue.class.getClassLoader().getResource("assets/characters/" + filename),
                "Resource not found: assets/characters/" + filename));
        BufferedImage[] frames = new BufferedImage[frame];

        for (int i = 0; i < frame; i++) {
            frames[i] = spriteSheet.getSubimage(i * 32, 0, 32, 32);
        }
        return frames;
    }
}
