package game.Other;

import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
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

    // ui
    public static BufferedImage dialogBubble = null;
    public static ImageIcon menuIcon = null;
    public static BufferedImage menuPanel = null;

    // 工具相关
    public static BufferedImage[] toolImages;
    public static BufferedImage toolBar;
    //道具栏
    public static BufferedImage boxImage = null;
    public static BufferedImage scytheImage = null;
    public static BufferedImage potatoSeedImage = null;
    public static BufferedImage tomatoSeedImage = null;
    public static BufferedImage chooseImage = null;

    private static BufferedImage plantTileset;
    //物品栏
    public static BufferedImage box2Image = null;
    public static BufferedImage moneyImage = null;
    public static BufferedImage potatoImage = null;
    public static BufferedImage tomatoImage = null;
    public static BufferedImage eggImage = null;
    public static BufferedImage milkImage = null;

    // 字体
    public static java.awt.Font pixelFont = null;

    public static void init() {
        try {
            // 加载ui
            dialogBubble = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/dialogBubble.png"),
                    "Resource not found: assets/ui/dialogBubble.png"));

            menuIcon = new ImageIcon(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/menu.png")));

            menuPanel = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/menuPanel.png"),
                    "Resource not found: assets/ui/menuPanel.png"));

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

            // 加载道具栏资源
            boxImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/box.png"),
                    "Resource not found: assets/ui/box.png"));
            scytheImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/scythe.png"),
                    "Resource not found: assets/objects/scythe.png"));
            potatoSeedImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/potatoSeed.png"),
                    "Resource not found: assets/objects/potatoSeed.png"));
            tomatoSeedImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/tomatoSeed.png"),
                    "Resource not found: assets/objects/tomatoSeed.png"));
            chooseImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/choose.png"),
                    "Resource not found: assets/ui/choose.png"));

            // 加载物品栏资源
            box2Image = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/ui/box2.png"),
                    "Resource not found: assets/ui/box2.png"));
            moneyImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/money.png"),
                    "Resource not found: assets/objects/money.png"));
            potatoImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/potato.png"),
                    "Resource not found: assets/objects/potato.png"));
            tomatoImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/tomato.png"),
                    "Resource not found: assets/objects/tomato.png"));
            eggImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/egg.png"),
                    "Resource not found: assets/objects/egg.png"));
            milkImage = ImageIO.read(Objects.requireNonNull(
                    StaticValue.class.getClassLoader().getResource("assets/objects/milk.png"),
                    "Resource not found: assets/objects/milk.png"));

            // 加载像素字体
            pixelFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, StaticValue.class.getClassLoader().getResourceAsStream("assets/font/PressStart2P.ttf")).deriveFont(12f);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
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

    public static BufferedImage getPlantImage(int row, int col) {
        if (plantTileset == null) {
            try {
                plantTileset = ImageIO.read(Objects.requireNonNull(
                        StaticValue.class.getClassLoader().getResource("assets/map/assets.png")));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return plantTileset.getSubimage(col * 16, row * 16, 16, 16);
    }
}
