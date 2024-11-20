package game.Other;

import game.MainFrame;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackScreenController {
    private static BlackScreenController instance;
    private boolean isBlackScreen = false;
    private int duration = 120; // 黑屏持续时间
    private final MainFrame mainFrame; // Main 实例引用，用于重绘窗口

    private BlackScreenController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public static BlackScreenController getInstance(MainFrame mainFrame) {
        if (instance == null) {
            instance = new BlackScreenController(mainFrame);
        }
        return instance;
    }

    public static BlackScreenController getInstance() {
        return instance;
    }

    // 开启黑屏
    public void startBlackScreen() {
        isBlackScreen = true;
        mainFrame.repaint(); // 重绘以显示黑屏

        // 使用 Timer 进行延迟处理
        new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBlackScreen = false;
                mainFrame.repaint(); // 重绘以移除黑屏
                ((Timer) e.getSource()).stop(); // 停止定时器
            }
        }).start();
    }

    // 获取黑屏状态
    public boolean isBlackScreen() {
        return isBlackScreen;
    }
}
