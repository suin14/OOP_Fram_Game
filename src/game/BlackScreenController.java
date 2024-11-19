package game;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// 单例模式的 BlackScreenController 类
class BlackScreenController {
    private static BlackScreenController instance;
    private boolean isBlackScreen = false;
    private int duration = 120; // 黑屏持续时间
    private Main main; // Main 实例引用，用于重绘窗口

    // 私有构造方法，防止外部实例化
    private BlackScreenController(Main main) {
        this.main = main;
    }

    // 获取单例实例
    public static BlackScreenController getInstance(Main main) {
        if (instance == null) {
            instance = new BlackScreenController(main);
        }
        return instance;
    }

    public static BlackScreenController getInstance() {
        return instance;
    }

    // 开启黑屏
    public void startBlackScreen() {
        isBlackScreen = true;
        main.repaint(); // 重绘以显示黑屏

        // 使用 Timer 进行延迟处理
        new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBlackScreen = false;
                main.repaint(); // 重绘以移除黑屏
                ((Timer) e.getSource()).stop(); // 停止定时器
            }
        }).start();
    }

    // 获取黑屏状态
    public boolean isBlackScreen() {
        return isBlackScreen;
    }
}
