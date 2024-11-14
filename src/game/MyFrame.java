package game;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFrame extends JFrame implements KeyListener {
    private List<Location> Locations = new ArrayList<>(); // 用于存储所有的地图

    private final int LocationsCnt = 1; // 场景数量

    // 用于存储当前地图
    private Location nowLocation = new Location();

    private Image offScreenImage = null;


    public MyFrame() {
        setTitle("圈圈物语");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // 窗口大小不可变
        addKeyListener(this);
        setVisible(true);

        StaticValue.init();

        // 创建所有场景
        for (int i = 1; i <= LocationsCnt; i++) {
            Locations.add(new Location("farm"));
        }

        // 设置初始场景
        nowLocation = Locations.get(0);
        // 绘制图像
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(getWidth(), getHeight());
        }
        Graphics graphics = offScreenImage.getGraphics();
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // 绘制背景，按窗口大小缩放背景图
        graphics.drawImage(nowLocation.getBgImage(), 0, 0, getWidth(), getHeight(), this);

        // 将图片绘制到窗口中
        g.drawImage(offScreenImage, 0, 0, this);
    }


    // 实现KeyListener接口的三个方法
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("按下的键: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("释放的键: " + e.getKeyChar());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 具体操作
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
