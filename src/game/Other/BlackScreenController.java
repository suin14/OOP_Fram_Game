package game.Other;

public class BlackScreenController {
    private static boolean isBlackScreen = false;

    // 开启黑屏
    public static void start() {
        isBlackScreen = true;
    }

    // 结束黑屏
    public static void stop() {
        isBlackScreen = false;
    }

    // 获取黑屏状态
    public static boolean isBlackScreen() {
        return isBlackScreen;
    }
}
