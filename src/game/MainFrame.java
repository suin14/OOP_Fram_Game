package game;

import game.Character.Farmer;
import game.Dialog.DialogBubble;
import game.Map.MapsData;
import game.Other.BlackScreenController;
import game.Other.SoundManager;
import game.Other.StaticValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Objects;


public class MainFrame extends JFrame implements KeyListener,Runnable {
    private Image offScreenImage = null;
    private final Farmer pc;
    private final MapsData mapViewer;

    private final DialogBubble dialogBubble;

    // 道具栏相关变量
    private int selectedToolIndex = 0; // 当前选中的工具
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

        // 初始化PC
        pc = Farmer.getInstance(19, 9);

        // 播放BGM
        SoundManager.playBGM();

        // 对话框
        dialogBubble = DialogBubble.getInstance();

        // 绘制图像
        repaint();
        Thread thread = new Thread(this);
        thread.start();
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
            }

            // 在农场地图绘制道具栏
            if (mapViewer != null && mapViewer.nowMap != null && mapViewer.nowMap.isFarm) {
                mapViewer.nowMap.drawToolBar(graphics, selectedToolIndex);
            }

            // 绘制PC角色, 放大至64x64
            if (pc != null) {
                graphics.drawImage(pc.getShow(), pc.getX(), pc.getY(), 64, 64, this);
            }

            // 渲染对话框
            if (dialogBubble != null && DialogBubble.isTalking()) {
                dialogBubble.paintComponent(graphics);
            }

            // 将图片绘制到窗口中
            g.drawImage(offScreenImage, 0, 0, this);
        }
    }


    // 实现KeyListener接口的三个方法
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // 如果正在进行对话，只能先结束对话
        if (DialogBubble.isTalking()) {
            if (keyCode == KeyEvent.VK_SPACE) {
                DialogBubble.stopTalking();
            }
        } else {

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
            }

            else if (e.getKeyCode() == KeyEvent.VK_Q) {
                // 按下 Q 键，切换工具
                selectedToolIndex = (selectedToolIndex + 1) % toolCount;
                repaint();
            } else if (e.getKeyCode() == KeyEvent.VK_E) {
                // 按下 E 键，使用道具

            }

            else if (keyCode == KeyEvent.VK_SPACE) {
                // 按下 Space 键，pc进行交互
                    pc.interact();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // 当松开任意方向时，停止移动
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
}
