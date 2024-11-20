package game;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class Main extends JFrame implements KeyListener,Runnable {
    private Image offScreenImage = null;

    private final Thread thread = new Thread(this);

    private final Farmer pc;
    private final BlackScreenController blackScreenController;
    private final MapsData mapViewer;

    public Main() {
        setTitle("圈圈物语");
        setSize(1152, 648);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // 窗口大小不可变
        addKeyListener(this);
        setVisible(true);

        StaticValue.init(); // 初始化图片资源

        blackScreenController = BlackScreenController.getInstance(this); //控制过场动画

        // 初始化地图
        mapViewer = MapsData.getInstance();
        mapViewer.updadteNowMap("farm");
        add(mapViewer.nowMap, BorderLayout.CENTER);

        // 初始化PC
        pc = Farmer.getInstance(14, 7);

        // 播放BGM
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playBGM();


        // 绘制图像
        repaint();
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(getWidth(), getHeight());
        }
        Graphics graphics = offScreenImage.getGraphics();
        graphics.clearRect(0, 0, getWidth(), getHeight());  // 清空背景


        if (blackScreenController != null && blackScreenController.isBlackScreen()) { //播放过场动画
            // 绘制黑屏
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // 渲染地图
            if (mapViewer != null && mapViewer.nowMap != null) {
                mapViewer.nowMap.paintComponent(graphics);
            }

            // 绘制PC角色, 放大至64x64
            if (pc != null) {
                graphics.drawImage(pc.getShow(), pc.getX(), pc.getY(), 64, 64, this);
            }

            // 将图片绘制到窗口中
            g.drawImage(offScreenImage, 0, 0, this);
        }
    }


    // 实现KeyListener接口的三个方法
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A) {
            // 按下 A 键，农民向左移动
                pc.move(3);
        } else if (keyCode == KeyEvent.VK_D) {
            // 按下 D 键，农民向右移动
                pc.move(1);
        } else if (keyCode == KeyEvent.VK_W) {
            // 按下 W 键，农民向上移动
                pc.move(0);
        } else if (keyCode == KeyEvent.VK_S) {
            // 按下 S 键，农民向下移动
                pc.move(2);
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
        new Main();
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
