package game.Other;

import java.awt.*;
import java.time.LocalDateTime;

public class TimeSystem {
    private static TimeSystem instance;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private DayPhase currentPhase;

    private static final Rectangle timeClickArea = new Rectangle(50, 60, 200, 30);  // 点击区域

    public enum DayPhase {
        DAWN(5, new Color(150, 150, 200, 50)),      // 黎明 5-7点
        DAY(7, new Color(0, 0, 0, 0)),              // 白天 7-17点
        DUSK(17, new Color(255, 200, 100, 50)),     // 黄昏 17-19点
        NIGHT(19, new Color(0, 0, 50, 100));        // 夜晚 19-5点

        public final int startHour;
        public final Color overlay;

        DayPhase(int startHour, Color overlay) {
            this.startHour = startHour;
            this.overlay = overlay;
        }
    }

    private TimeSystem() {
        // 初始化为系统时间
        LocalDateTime now = LocalDateTime.now();
        this.year = now.getYear();
        this.month = now.getMonthValue();
        this.day = now.getDayOfMonth();
        this.hour = now.getHour();
        this.minute = now.getMinute();
        updatePhase();
    }

    public static TimeSystem getInstance() {
        if (instance == null) {
            instance = new TimeSystem();
        }
        return instance;
    }

    public void update() {
        minute++;
        if (minute >= 60) {
            minute = 0;
            hour++;
            if (hour >= 24) {
                hour = 0;
                day++;
                if (day > getDaysInMonth(year, month)) {
                    day = 1;
                    month++;
                    if (month > 12) {
                        month = 1;
                        year++;
                    }
                }
            }
            updatePhase();
        }
    }

    private int getDaysInMonth(int year, int month) {
        return java.time.YearMonth.of(year, month).lengthOfMonth();
    }

    private void updatePhase() {
        if (hour >= DayPhase.DAWN.startHour && hour < DayPhase.DAY.startHour) {
            currentPhase = DayPhase.DAWN;
        } else if (hour >= DayPhase.DAY.startHour && hour < DayPhase.DUSK.startHour) {
            currentPhase = DayPhase.DAY;
        } else if (hour >= DayPhase.DUSK.startHour && hour < DayPhase.NIGHT.startHour) {
            currentPhase = DayPhase.DUSK;
        } else {
            currentPhase = DayPhase.NIGHT;
        }
    }

    public void render(Graphics2D g, int frameWidth, int frameHeight) {
        // 绘制时间和日期
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String timeStr = String.format("%d-%02d-%02d %02d:%02d",
            year, month, day, hour, minute);
        g.drawString(timeStr, 50, 80);

        // 调试用：显示点击区域
        // g.setColor(new Color(255, 255, 255, 50));
        // g.fillRect(timeClickArea.x, timeClickArea.y, timeClickArea.width, timeClickArea.height);

        // 绘制时间阶段的滤镜效果
        g.setColor(currentPhase.overlay);
        g.fillRect(0, 0, frameWidth, frameHeight);
    }

    public DayPhase getCurrentPhase() {
        return currentPhase;
    }

    // 检查点击是否在时间显示区域内
    public boolean isTimeClicked(int x, int y) {
        return timeClickArea.contains(x, y);
    }

    // 切换到下一个时间段
    public void switchToNextPhase() {
        DayPhase nextPhase;
        if (currentPhase == DayPhase.NIGHT) {
            nextPhase = DayPhase.DAWN;
        } else if (currentPhase == DayPhase.DAWN) {
            nextPhase = DayPhase.DAY;
        } else if (currentPhase == DayPhase.DAY) {
            nextPhase = DayPhase.DUSK;
        } else {
            nextPhase = DayPhase.NIGHT;
        }

        // 设置为下一个时间段的开始时间
        hour = nextPhase.startHour;
        minute = 1;
        updatePhase();
    }
} 