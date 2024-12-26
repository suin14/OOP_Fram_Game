package game.Map;

import game.Character.Character;
import game.Character.Shoper;
import org.w3c.dom.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MapLoader extends JPanel {
    private final int scaleFactor = 2; // 缩放倍数
    private BufferedImage tileset; // 图块集图像
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
                            if (warpParts.length % 5 == 0) {
                                for (int j = 0; j < warpParts.length; j += 5) {
                                    String loc = warpParts[i]; // 传送去的地图名字
                                    int fromX = Integer.parseInt(warpParts[j + 1]); // 传送点的X坐标
                                    int fromY = Integer.parseInt(warpParts[j + 2]); // 传送点的Y坐标
                                    int x = Integer.parseInt(warpParts[j + 3]); // 目标地图的X坐标
                                    int y = Integer.parseInt(warpParts[j + 4]); // 目标地图的Y坐标

                                    int fromIndex = fromY * getMapWidth() + fromX;
                                    Warp warpInfo = new Warp(loc, fromIndex, x, y);
                                    warpsData.put(fromIndex, warpInfo);
                                }
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

    public int getMapHeight() {
        return mapHeight;
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

    // 获取指定位置的瓦片ID
    public int getTileIdAt(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= mapWidth || tileY >= mapHeight) {
            return -1;
        }
        int tileIndex = tileY * mapWidth + tileX;
        // 使用Backs2层（index为1）来判断农田
        ArrayList<Integer> layer = layersData.get(1);
        return layer.get(tileIndex);
    }
}
