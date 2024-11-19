package game;

import org.w3c.dom.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class MapLoader extends JPanel {
    private final int scaleFactor = 3; // 缩放倍数
    private BufferedImage tileset; // 图块集图像
    private static int tileWidth; // 单个图块宽度
    private static int tileHeight; // 单个图块高度
    private static int mapWidth; // 地图宽度（以图块为单位）
    private int mapHeight; // 地图高度（以图块为单位）
    private ArrayList<Integer> mapData = new ArrayList<>(); // 地图图块数据
    private ArrayList<Integer> collisionData = new ArrayList<>(); // 碰撞数据（0为无碰撞，1为有碰撞）

    // 存储图层数据
    private ArrayList<ArrayList<Integer>> layersData = new ArrayList<>();

    public MapLoader(String tmxFilePath, String tilesetImagePath) {
        loadTileset(tilesetImagePath);
        loadMap(tmxFilePath);
    }

    // 加载图块集
    private void loadTileset(String tilesetImagePath) {
        try {
            tileset = ImageIO.read(new File(tilesetImagePath));
            tileWidth = 16; // 设置图块宽度
            tileHeight = 16; // 设置图块高度
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载 TMX 地图
    private void loadMap(String tmxFilePath) {
        try {
            File tmxFile = new File(tmxFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(tmxFile);
            document.getDocumentElement().normalize();

            Element mapElement = (Element) document.getElementsByTagName("map").item(0);
            mapWidth = Integer.parseInt(mapElement.getAttribute("width"));
            mapHeight = Integer.parseInt(mapElement.getAttribute("height"));

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
                        layer.add(Integer.parseInt(tile.trim()) - 1); // TMX ID 从1开始，减1方便处理
                    }

                    // 获取图层名称（假设 layerElement 中有 getName() 方法）
                    String layerName = layerElement.getAttribute("name"); // 获取图层名称

                    if ("Buildings".equals(layerName)) {
                        collisionData = layer; // 如果图层名为 Buildings，则设置为碰撞数据
                    }

                    layersData.add(layer); // 添加图层到 layersData 列表
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 渲染地图
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // 循环绘制每一层
        for (int layerIndex = 0; layerIndex < layersData.size(); layerIndex++) {
            ArrayList<Integer> layer = layersData.get(layerIndex);

            // 在每一层上绘制图块
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    int tileId = layer.get(y * mapWidth + x);
                    if (tileId >= 0) {
                        // 计算当前图块的起始位置
                        int tileX = (tileId % (tileset.getWidth() / tileWidth)) * tileWidth;
                        int tileY = (tileId / (tileset.getWidth() / tileWidth)) * tileHeight;

                        // 通过缩放因子将图块尺寸扩大三倍
                        int newTileWidth = tileWidth * scaleFactor;
                        int newTileHeight = tileHeight * scaleFactor;

                        // 绘制当前图块，放大到三倍
                        g2d.drawImage(tileset,
                                x * newTileWidth, y * newTileHeight,
                                x * newTileWidth + newTileWidth, y * newTileHeight + newTileHeight,
                                tileX, tileY, tileX + tileWidth, tileY + tileHeight,
                                this);
                    }
                }
            }
        }
    }

    // 碰撞检测
    public boolean checkCollision(int x, int y) {
        int tileX = x / tileWidth;
        int tileY = y / tileHeight;
        int index = tileY * mapWidth + tileX;
        // 返回碰撞数据
        return collisionData.get(index) == 1;
    }
}
