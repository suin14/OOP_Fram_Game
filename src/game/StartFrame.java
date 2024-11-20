package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class StartFrame extends JFrame implements ActionListener {
    private BufferedImage offScreenImage;
    private JButton play;

    // 构造方法
    public StartFrame() {
        setTitle("圈圈物语");
        setIconImage(new ImageIcon(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "logo.png").getImage());
        setSize(1152, 648);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        play = new JButton();

        ImageIcon playIcon = new ImageIcon(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui" + File.separator + "start.png");
        play.setIcon(playIcon);
        play.setFocusPainted(false);  // 不显示按钮的焦点矩形
        play.setContentAreaFilled(false); // 去掉按钮的背景色
        play.setBorderPainted(false);  // 去掉按钮的边框
        play.setBounds((getWidth() - playIcon.getIconWidth()) / 2, 370, playIcon.getIconWidth(), playIcon.getIconHeight());
        play.addActionListener(this);
        setLayout(null);
        add(play);

        offScreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        setVisible(true);

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (offScreenImage == null) {
            offScreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D graphics = offScreenImage.createGraphics();
        graphics.clearRect(0, 0, getWidth(), getHeight());

        graphics.drawImage(new ImageIcon(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui" + File.separator + "title.png").getImage(), 0, 0, getWidth(), getHeight(), this);

        g.drawImage(offScreenImage, 0, 0, this);

    }

    public static void main(String[] args) {
        new StartFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            dispose();
            MainFrame gf = null;
            try {
                gf = new MainFrame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
