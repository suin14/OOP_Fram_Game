package game;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame implements KeyListener,Runnable {
    private List<Location> Locations = new ArrayList<>(); // 用于存储所有的地图

    private final int LocationsCnt = 1; // 场景数量

    // 用于存储当前地图
    private Location nowLocation = new Location();

    private Image offScreenImage = null;

    // 创建PC
    private Farmer pc = new Farmer();

    private Thread thread = new Thread(this);

    private SoundManager soundManager = new SoundManager();


    public Main() {
        setTitle("圈圈物语");
        setSize(1152, 648);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // 窗口大小不可变
        addKeyListener(this);
        setVisible(true);

        AssetManager.init(); // 初始化图片资源

        // 初始化PC
        pc = new Farmer(0, 0);

        // 创建所有场景
        for (int i = 1; i <= LocationsCnt; i++) {
            Locations.add(new Location("farm"));
        }

        // 设置初始场景
        nowLocation = Locations.get(0);

        // 播放BGM
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
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // 绘制背景，按窗口大小缩放背景图(3倍)
        graphics.drawImage(nowLocation.getBgImage(), 0, 0, getWidth(), getHeight(), this);

        // 绘制pc, 放大至64x64
        graphics.drawImage(pc.getShow(), pc.getX(), pc.getY(), 64, 64,  this);

        // 将图片绘制到窗口中
        g.drawImage(offScreenImage, 0, 0, this);
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

        // 互动键(使用道具/对话) Space

        // 切换道具栏 Q E

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
                Thread.sleep(80);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
