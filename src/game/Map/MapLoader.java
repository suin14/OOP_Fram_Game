package game.Map;

import game.Character.Character;
import game.Character.Shoper;
import org.w3c.dom.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MapLoader extends JPanel {
    private final int scaleFactor = 2; // 缩放倍数
    private BufferedImage tileset; // 图块集图像
    private BufferedImage boxImage, box2Image, scytheImage, potatoSeedImage, tomatoSeedImage, chooseImage; //道具栏相关图片
    private final List<BufferedImage> itemImages = new ArrayList<>(); // 物品图片列表
    private final List<Integer> itemCounts = new ArrayList<>(); // 物品数量
    private Font Font; // 自定义像素字体
    private final int tileWidth = 16; // 单个图块宽度
    private final int tileHeight = 16; // 单个图块高度
    private int mapWidth; // 地图宽度（以图块为单位）
    private int mapHeight; // 地图高度（以图块为单位）

    // 地图数据
    private final ArrayList<ArrayList<Integer>> layersData = new ArrayList<>();
    private ArrayList<Integer> collisionData = new ArrayList<>();

    private final HashMap<Integer, Warp> warpsData = new HashMap<>(); // 传送点信息

    public boolean isFarm = false; // 当前地图有没有农田
    public final HashMap<Integer, Warp> farmData = new HashMap<>(); // 农田信息 // TODO 替换成Plant类

    public final HashMap<Integer, Character> npcData = new HashMap<>(); // 地图上npc信息


    public MapLoader(String tmxFilePath, String tilesetImagePath) {
        loadTileset(tilesetImagePath);
        loadMap(tmxFilePath);
        loadToolBarResources();
        loadInventoryBarResources();
    }

    // 加载图块集
    private void loadTileset(String tilesetImagePath) {
        try {
            URL url = getClass().getClassLoader().getResource("assets/map/" + tilesetImagePath);
            if (url != null) {
                tileset = ImageIO.read(url);
            } else {
                System.err.println("无法找到资源文件: " + tilesetImagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载道具栏相关资源
    private void loadToolBarResources() {
        try {
            boxImage = ImageIO.read(new File("assets/ui/box.png"));
            scytheImage = ImageIO.read(new File("assets/objects/scythe.png"));
            potatoSeedImage = ImageIO.read(new File("assets/objects/potatoSeed.png"));
            tomatoSeedImage = ImageIO.read(new File("assets/objects/tomatoSeed.png"));
            chooseImage = ImageIO.read(new File("assets/ui/choose.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载物品栏相关资源
    private void loadInventoryBarResources() {
        try {
            box2Image = ImageIO.read(new File("assets/ui/box2.png"));
            // 加载物品图片
            itemImages.add(ImageIO.read(new File("assets/objects/money.png")));
            itemImages.add(ImageIO.read(new File("assets/objects/potato.png")));
            itemImages.add(ImageIO.read(new File("assets/objects/tomato.png")));
            itemImages.add(ImageIO.read(new File("assets/objects/egg.png")));
            itemImages.add(ImageIO.read(new File("assets/objects/milk.png")));

            // 初始化物品数量
            itemCounts.add(1000);
            itemCounts.add(1);
            itemCounts.add(2);
            itemCounts.add(3);
            itemCounts.add(4);

            // 加载字体
            Font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/PressStart2P.ttf")).deriveFont(12f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // 加载 TMX 地图
    private void loadMap(String tmxFilePath) {
        try {
            URL url = getClass().getClassLoader().getResource("assets/map/" + tmxFilePath);
            if (url != null) {
                InputStream inputStream = url.openStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                document.getDocumentElement().normalize();

                Element mapElement = (Element) document.getElementsByTagName("map").item(0);
                mapWidth = Integer.parseInt(mapElement.getAttribute("width"));
                mapHeight = Integer.parseInt(mapElement.getAttribute("height"));

                // 读取 Map Properties
                NodeList propertiesList = mapElement.getElementsByTagName("properties");
                if (propertiesList.getLength() > 0) {
                    Element propertiesElement = (Element) propertiesList.item(0);
                    NodeList propertyList = propertiesElement.getElementsByTagName("property");
                    for (int i = 0; i < propertyList.getLength(); i++) {
                        Element propertyElement = (Element) propertyList.item(i);
                        String name = propertyElement.getAttribute("name");
                        String value = propertyElement.getAttribute("value");

                        // 读取传送点信息
                        if ("Warp".equals(name)) {
                            String[] warpParts = value.split(" ");
                            if (warpParts.length == 5) {
                                String loc = warpParts[0]; // 第一个值是传送去的地图名字
                                int fromX = Integer.parseInt(warpParts[1]); // 第二个值是 传送点的X坐标
                                int fromY = Integer.parseInt(warpParts[2]); // 第二个值是 传送点的的Y坐标
                                int x = Integer.parseInt(warpParts[3]); // 第三个值是 传送去的X坐标
                                int y = Integer.parseInt(warpParts[4]); // 第四个值是 传送去的Y坐标
                                int fromIndex = fromY * getMapWidth() + fromX;
                                Warp warpInfo = new Warp(loc, fromIndex, x, y);
                                warpsData.put(fromIndex, warpInfo);
                            }
                        }

                        // 读取农田信息
                        if ("isFarm".equals(name)) {
                            if ("T".equals(value)) {
                                this.isFarm = true;
                            }
                        }
                    }
                }

                // 读取 layer 数据
                NodeList layerList = document.getElementsByTagName("layer");
                for (int i = 0; i < layerList.getLength(); i++) {
                    Element layerElement = (Element) layerList.item(i);
                    NodeList dataList = layerElement.getElementsByTagName("data");
                    if (dataList.getLength() > 0) {
                        Element dataElement = (Element) dataList.item(0);
                        String[] tileData = dataElement.getTextContent().trim().split(",");
                        ArrayList<Integer> layer = new ArrayList<>();
                        for (String tile : tileData) {
                            layer.add(Integer.parseInt(tile.trim()) - 1); // TMX ID从1开始，减1方便处理
                        }

                        String layerName = layerElement.getAttribute("name");
                        if ("Buildings".equals(layerName)) {
                            collisionData = layer; // 只跟Buildings产生碰撞
                        }

                        layersData.add(layer); // 添加图层数据到 layersData 列表
                    }
                }

                // 读取对象层数据
                NodeList objectGroupList = document.getElementsByTagName("objectgroup");
                for (int i = 0; i < objectGroupList.getLength(); i++) {
                    Element objectGroupElement = (Element) objectGroupList.item(i);

                    // 遍历对象层中的每个对象
                    NodeList objectList = objectGroupElement.getElementsByTagName("object");
                    for (int j = 0; j < objectList.getLength(); j++) {
                        Element objectElement = (Element) objectList.item(j);
                        String name = objectElement.getAttribute("name");
                        float x = Float.parseFloat(objectElement.getAttribute("x"));
                        float y = Float.parseFloat(objectElement.getAttribute("y"));

                        // 查找名为 "TileData" 的对象
                        if ("TileData".equals(name)) {
                            // 读取 TileData 的自定义属性
                            NodeList objectPropertiesList = objectElement.getElementsByTagName("properties");
                            if (objectPropertiesList.getLength() > 0) {
                                Element propertiesElement = (Element) objectPropertiesList.item(0);
                                NodeList propertyList = propertiesElement.getElementsByTagName("property");
                                for (int k = 0; k < propertyList.getLength(); k++) {
                                    Element propertyElement = (Element) propertyList.item(k);
                                    String propName = propertyElement.getAttribute("name");

                                    int tileX = (int) ((x / getTileWidth()));
                                    int tileY = (int) ((y / getTileWidth()));
                                    int index = tileY * getMapWidth() + tileX;

                                    // 读取农田数据
                                    if ("canPlanted".equals(propName)) {
                                        String propValue = propertyElement.getAttribute("value");
                                        if ("True".equals(propValue)) {
                                            farmData.put(index, null);
                                        }
                                    }

                                    // 读取NPC信息
                                    if ("NPC".equals(propName)) {
                                        String npcName = propertyElement.getAttribute("value");
                                        if (npcName.equals("Shoper")) {
                                            Shoper shoper = Shoper.getInstance(tileX, tileY);
                                            npcData.put(index, shoper);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                System.err.println("无法找到资源文件: " + tmxFilePath + ".tmx");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 渲染地图
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        ArrayList<ArrayList<Integer>> layersDataCopy = new ArrayList<>(layersData);

        for (ArrayList<Integer> layer : layersDataCopy) {
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    int tileId = layer.get(y * mapWidth + x);
                    if (tileId >= 0) {
                        int tileX = (tileId % (tileset.getWidth() / tileWidth)) * tileWidth;
                        int tileY = (tileId / (tileset.getWidth() / tileWidth)) * tileHeight;

                        int newTileWidth = tileWidth * scaleFactor;
                        int newTileHeight = tileHeight * scaleFactor;

                        g2d.drawImage(
                                tileset,
                                x * newTileWidth, y * newTileHeight,
                                x * newTileWidth + newTileWidth, y * newTileHeight + newTileHeight,
                                tileX, tileY, tileX + tileWidth, tileY + tileHeight,
                                this
                        );
                    }
                }
            }
        }

        // 绘制npc
        for (Character character : npcData.values()) {
            g2d.drawImage(character.getShow(), character.getX(), character.getY(), 64, 64, this);
        }
    }

    // 绘制工具栏
    public void drawToolBar(Graphics g, int selectedToolIndex) {
        int toolCount = 3;
        int boxWidth = 64;
        int boxHeight = 64;
        int startX = 1160 - (boxWidth * toolCount) / 2;
        int startY = 630;

        for (int i = 0; i < toolCount; i++) {
            g.drawImage(boxImage, startX + i * boxWidth, startY, boxWidth, boxHeight, null);

            // 绘制工具图片
            switch (i) {
                case 0 :
                    int toolWidth = (int) (boxWidth * 0.75);
                    int toolHeight = (int) (boxHeight * 0.75);
                    int offsetX = (boxWidth - toolWidth) / 2;
                    int offsetY = (boxHeight - toolHeight) / 2;
                    g.drawImage(scytheImage, startX + i * boxWidth + offsetX, startY + offsetY, toolWidth, toolHeight, null);
                    break;
                case 1 :
                    toolWidth = (int) (boxWidth * 0.55);
                    toolHeight = (int) (boxHeight * 0.55);
                    offsetX = (boxWidth - toolWidth) / 2;
                    offsetY = (boxHeight - toolHeight) / 2;
                    g.drawImage(potatoSeedImage, startX + i * boxWidth + offsetX, startY + offsetY, toolWidth, toolHeight, null);
                    break;
                case 2 :
                    toolWidth = (int) (boxWidth * 0.55);
                    toolHeight = (int) (boxHeight * 0.55);
                    offsetX = (boxWidth - toolWidth) / 2;
                    offsetY = (boxHeight - toolHeight) / 2;
                    g.drawImage(tomatoSeedImage, startX + i * boxWidth + offsetX, startY + offsetY, toolWidth, toolHeight, null);
                    break;
            }

            if (i == selectedToolIndex) {
                g.drawImage(chooseImage, startX + i * boxWidth, startY, boxWidth, boxHeight, null);
            }
        }
    }

    // 绘制物品栏
    public void drawInventoryBar(Graphics g) {
        int boxWidth = 64;
        int boxHeight = 90;
        int startY = (int) (tileHeight * mapHeight * 0.3); // 顶部空40%

        // 设置字体
        if (Font != null) {
            g.setFont(Font);
        }
        g.setColor(Color.WHITE); // 字体颜色设为白色

        int currentY = startY;

        // 遍历物品，绘制Box、图片和数量
        for (int i = 0; i < 5; i++) {
            // 绘制Box
            g.drawImage(boxImage, 10, currentY, boxWidth, boxHeight, null);

            // 绘制物品图片
            BufferedImage itemImage = itemImages.get(i);
            if (itemImage != null) {
                int itemSize = (int) (boxHeight * 0.35); // 物品图片高度占Box的40%
                int offsetX = (boxWidth - itemSize) / 2; // 水平居中
                int offsetY = (int) (boxHeight * 0.2); // 顶部空白
                g.drawImage(itemImage, 10 + offsetX, currentY + offsetY, itemSize, itemSize, null);
            }

            // 绘制物品数量
            String count = String.valueOf(itemCounts.get(i));
            int textX = 10 + boxWidth / 2 - g.getFontMetrics().stringWidth(count) / 2; // 水平居中
            int textY = currentY + boxHeight - 23; // 靠近Box底部
            g.drawString(count, textX, textY);

            // 更新Y坐标为下一个Box的起始位置
            currentY += boxHeight;
        }
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public ArrayList<Integer> getCollisionData() {
        return collisionData;
    }

    public HashMap<Integer, Warp> getWarpsData() {
        return warpsData;
    }

    public boolean isFarm() {
        return isFarm;
    }

    public HashMap<Integer, Warp> getFarmData() {  // TODO 替换成Plant类
        return farmData;
    } // TODO:偷换成Plant类
}
