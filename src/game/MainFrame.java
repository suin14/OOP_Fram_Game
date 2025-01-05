package game;

import game.Character.Farmer;
import game.Dialog.DialogBubble;
import game.Farm.Chicken;
import game.Map.MapLoader;
import game.Map.MapsData;
import game.Other.BlackScreenController;
import game.Other.PauseMenuPanel;
import game.Other.SoundManager;
import game.Other.StaticValue;
import game.Farm.FarmManager;
import game.Other.TimeSystem;
import game.Farm.ChickenManager;
import game.Hud.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.Objects;


public class MainFrame extends JFrame implements KeyListener, Runnable, ActionListener, Serializable {
    private Image offScreenImage = null;
    private final Farmer pc;
    private final MapsData mapViewer;

    private final DialogBubble dialogBubble;

    private final FarmManager farmManager;
    private ToolBar toolBar;
    private InventoryBar inventoryBar;
    private final PauseMenuPanel pauseMenuPanel;
    private TimeSystem timeSystem;
    private final ChickenManager chickenManager;

    // 道具栏相关变量
    private static int selectedToolIndex = 0; // 当前选中的工具
    private final int toolCount = 3;   // 道具数量

    public MainFrame() {
        setTitle("圈圈物语");
        setIconImage(new ImageIcon(Objects.requireNonNull(
                getClass().getClassLoader().getResource("assets/logo.png"))).getImage());
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // 窗口大小不可变
        addKeyListener(this);
        setVisible(true);

        StaticValue.init(); // 初始化图片资源

        // 初始化地图
        mapViewer = MapsData.getInstance();
        mapViewer.updadteNowMap("farm");
        farmManager = new FarmManager();
        timeSystem = TimeSystem.getInstance();
        chickenManager = new ChickenManager(
                mapViewer.nowMap.getCollisionData(),
                mapViewer.nowMap.getMapWidth()
        );

        // 初始化PC
        pc = Farmer.getInstance(19, 9);

        // 播放BGM
        SoundManager.playBGM();

        // 添加鼠标监听
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (timeSystem.isTimeClicked(e.getX(), e.getY())) {
                    timeSystem.switchToNextPhase();
                }
            }
        });
        pauseMenuPanel = new PauseMenuPanel();

        // 对话框
        dialogBubble = DialogBubble.getInstance();

        // 工具栏
        toolBar = ToolBar.getInstance();

        // 物品栏
        inventoryBar = InventoryBar.getInstance();

        // 绘制图像
        repaint();
        Thread thread = new Thread(this);
        thread.start();
        //植物生长
        new Thread(this::gameLoop).start();
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(getWidth(), getHeight());
        }
        Graphics graphics = offScreenImage.getGraphics();
        graphics.clearRect(0, 0, getWidth(), getHeight());  // 清空背景


        if (BlackScreenController.isBlackScreen()) { //播放过场动画
            // 绘制黑屏
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            new Timer(135, e -> {
                BlackScreenController.stop();
                ((Timer) e.getSource()).stop(); // 停止定时器
            }).start();

        } else {
            // 渲染地图
            if (mapViewer != null && mapViewer.nowMap != null) {
                mapViewer.nowMap.paintComponent(graphics);
                if (mapViewer.nowMap.isFarm) {
                    farmManager.render((Graphics2D) graphics, getWidth()); // 农场渲染植物
                    chickenManager.render((Graphics2D) graphics);  // 农场渲染小鸡
                }
            }

            // 绘制PC角色, 放大至64x64
            if (pc != null) {
                graphics.drawImage(pc.getShow(), pc.getX(), pc.getY(), 64, 64, this);
            }

            // 渲染时间系统
            if (mapViewer != null && mapViewer.nowMap != null) {
                timeSystem.render((Graphics2D) graphics, getWidth(), getHeight());
            }

            // 在农场套时间滤镜
            if (mapViewer != null && mapViewer.nowMap != null && mapViewer.nowMap.isFarm) {
                timeSystem.paintDayPhase((Graphics2D) graphics, getWidth(), getHeight());
            }
            
            // 在农场地图绘制道具栏
            if (mapViewer != null && mapViewer.nowMap != null && mapViewer.nowMap.isFarm) {
                toolBar.paintComponent(graphics);
            }

            // 绘制物品栏
            if (mapViewer != null && mapViewer.nowMap != null) {
                inventoryBar.paintComponent(graphics);
            }

            // 渲染对话框
            if (dialogBubble != null && DialogBubble.isTalking()) {
                dialogBubble.paintComponent(graphics);
            }

            if (pauseMenuPanel != null) {
                pauseMenuPanel.paintComponent(graphics);
            }

            // 将图片绘制到窗口中
            g.drawImage(offScreenImage, 0, 0, this);
        }
    }


    // 实现KeyListener接口的三个方法
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

//        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//            // 按下 Esc 键，暂停游戏
//            if (pauseMenuPanel.isPause()) {
//                pauseMenuPanel.hidePanel();
//            } else {
//                pauseMenuPanel.showPanel();
//            }
//        } else if (!pauseMenuPanel.isPause()) {
            // 如果正在进行对话，只能先结束对话
            if (DialogBubble.isTalking()) {
                if (keyCode == KeyEvent.VK_SPACE) {
                    DialogBubble.stopTalking();
                }
            } else if (!pauseMenuPanel.isPause()) {
                if (keyCode == KeyEvent.VK_A) {
                    // 按下 A 键，pc向左移动
                    pc.move(3);
                } else if (keyCode == KeyEvent.VK_D) {
                    // 按下 D 键，pc向右移动
                    pc.move(1);
                } else if (keyCode == KeyEvent.VK_W) {
                    // 按下 W 键，pc向上移动
                    pc.move(0);
                } else if (keyCode == KeyEvent.VK_S) {
                    // 按下 S 键，pc向下移动
                    pc.move(2);
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    // 按下 Q 键，向左切换工具
                    selectedToolIndex = (selectedToolIndex + toolCount - 1) % toolCount;
                    toolBar.setSelectedToolIndex(selectedToolIndex);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    // 按下 E 键，向右切换工具
                    selectedToolIndex = (selectedToolIndex + toolCount + 1) % toolCount;
                    toolBar.setSelectedToolIndex(selectedToolIndex);
                } else if (keyCode == KeyEvent.VK_SPACE) {
                    MapLoader nowMap = mapViewer.nowMap;
                    if (!nowMap.isFarm()) { // 在商店
                        pc.interact();
                    } else {
                        // 使用道具
                        // 获取玩家当前位置对应的瓦片ID
                        int tileX = (pc.getX() + 32) / (mapViewer.nowMap.getTileWidth() * mapViewer.nowMap.getScaleFactor());
                        int tileY = (pc.getY() + 32) / (mapViewer.nowMap.getTileHeight() * mapViewer.nowMap.getScaleFactor());
                        // 使用工具
                        if (selectedToolIndex == 0) {
                            // 工具1：收获或销毁植物
                            farmManager.handleHarvest(tileX, tileY);
                            for (Chicken chicken : chickenManager.getChickens()) {
                                chickenManager.pickEgg(chicken, tileX, tileY);
                            }
                        } else {
                            // 工具2和3：播种
                            if (farmManager.canPlant(tileX, tileY)) {
                                farmManager.plantSeed(selectedToolIndex + 1, tileX, tileY);
                            }
                        }
                    }
                }
//                else if (e.getKeyCode() == KeyEvent.VK_K) {
//                    saveGame("user.data");
//                } else if (e.getKeyCode() == KeyEvent.VK_L) {
//                    loadGame("user.data");
//                }
            }
//        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //当松开任意方向时，停止移动
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S) {
            pc.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        new MainFrame();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(60);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == pause) {
//
//        }
    }

    // 添加游戏循环更新植物生长
    public void gameLoop() {
        while (true) {
            farmManager.update();
            timeSystem.update();
            chickenManager.update();  // 更新小鸡
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存游戏状态
    public void saveGame(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // 写入时间系统
            oos.writeObject(timeSystem);

            // 写入PC的属性
            oos.writeInt(pc.getX());
            oos.writeInt(pc.getY());

            // 保存鸡和鸡蛋的状态
//            oos.writeObject(chickenManager.getChickens());

            System.out.println("游戏已成功保存到文件：" + filePath);
        } catch (IOException e) {
            System.err.println("保存游戏失败：" + e.getMessage());
        }
    }

    // 加载游戏状态
    public void loadGame(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            // 读取时间系统
            timeSystem = (TimeSystem) ois.readObject();

            // 读取并恢复PC的属性
            pc.setX(ois.readInt());
            pc.setY(ois.readInt());
            pc.setCurrentStatus(ois.readUTF());

            // 恢复鸡的状态
//            List<Chicken> chickens = (List<Chicken>) ois.readObject();  // 读取鸡的List
//            chickenManager.setChickens(chickens);  // 将读取到的List设置到鸡管理器中

            System.out.println("游戏已成功加载自文件：" + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载游戏失败：" + e.getMessage());
        }
    }
}
