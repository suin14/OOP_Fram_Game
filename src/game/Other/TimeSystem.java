package game.Other;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.time.LocalDateTime;

public class TimeSystem implements Serializable {
    private static TimeSystem instance;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private int second;
    private DayPhase currentPhase;

    private static final Rectangle timeClickArea = new Rectangle(50, 60, 200, 30);  // 点击区域

    // 添加七段显示屏的数字定义
    private static final boolean[][] DIGIT_SEGMENTS = {
        {true, true, true, true, true, true, false},     // 0
        {false, true, true, false, false, false, false}, // 1
        {true, true, false, true, true, false, true},    // 2
        {true, true, true, true, false, false, true},    // 3
        {false, true, true, false, false, true, true},   // 4
        {true, false, true, true, false, true, true},    // 5
        {true, false, true, true, true, true, true},     // 6
        {true, true, true, false, false, false, false},  // 7
        {true, true, true, true, true, true, true},      // 8
        {true, true, true, true, false, true, true}      // 9
    };

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
        // 初始化时间
//        LocalDateTime now = LocalDateTime.now();
//        this.year = now.getYear();
//        this.month = now.getMonthValue();
        this.day = 0;
        this.hour = 6;
        this.minute = 0;
        this.second = 0;
        updatePhase();
    }

    public static TimeSystem getInstance() {
        if (instance == null) {
            instance = new TimeSystem();
        }
        return instance;
    }

    public void update() {
        second = second + 6;
        if (second >= 60) {
            second = 0;
            minute++;
            if (minute >= 60) {
                minute = 0;
                hour++;
                if (hour >= 24) {
                    hour = 0;
                    day++;
//                    if (day > getDaysInMonth(year, month)) {
//                        day = 1;
//                        month++;
//                        if (month > 12) {
//                            month = 1;
//                            year++;
//                        }
//                    }
                }
                updatePhase();
            }
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
        g.setColor(Color.BLACK);
        
        // 启用抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int digitWidth = 20;
        int digitHeight = 30;
        int startX = 50;
        int startY = 60;
        
        // 绘制小时
        drawDigit(g, hour / 10, startX, startY, digitWidth, digitHeight);
        drawDigit(g, hour % 10, startX + digitWidth + 5, startY, digitWidth, digitHeight);
        
        // 绘制六边形冒号
        drawHexagonDot(g, startX + (digitWidth + 5) * 2, startY + digitHeight/2 - 5, 3);
        drawHexagonDot(g, startX + (digitWidth + 5) * 2, startY + digitHeight/2 + 5, 3);
        
        // 绘制分钟
        drawDigit(g, minute / 10, startX + (digitWidth + 5) * 2 + 10, startY, digitWidth, digitHeight);
        drawDigit(g, minute % 10, startX + (digitWidth + 5) * 3 + 15, startY, digitWidth, digitHeight);
    }

    private void drawDigit(Graphics2D g, int num, int x, int y, int width, int height) {
        boolean[] segments = DIGIT_SEGMENTS[num];
        int segmentThickness = 3;
        
        // 绘制水平线段（上中下）
        if (segments[0]) drawHexagonSegment(g, x + segmentThickness, y, width - 2*segmentThickness, segmentThickness, true);
        if (segments[6]) drawHexagonSegment(g, x + segmentThickness, y + height/2 - segmentThickness/2, width - 2*segmentThickness, segmentThickness, true);
        if (segments[3]) drawHexagonSegment(g, x + segmentThickness, y + height - segmentThickness, width - 2*segmentThickness, segmentThickness, true);
        
        // 绘制垂直线段（左上、右上、左下、右下）
        if (segments[5]) drawHexagonSegment(g, x, y + segmentThickness, segmentThickness, height/2 - segmentThickness, false);
        if (segments[1]) drawHexagonSegment(g, x + width - segmentThickness, y + segmentThickness, segmentThickness, height/2 - segmentThickness, false);
        if (segments[4]) drawHexagonSegment(g, x, y + height/2, segmentThickness, height/2 - segmentThickness, false);
        if (segments[2]) drawHexagonSegment(g, x + width - segmentThickness, y + height/2, segmentThickness, height/2 - segmentThickness, false);
    }

    // 绘制六边形线段
    private void drawHexagonSegment(Graphics2D g, int x, int y, int width, int height, boolean isHorizontal) {
        int offset = height / 4; // 六边形两端的偏移量
        
        int[] xPoints;
        int[] yPoints;
        
        if (isHorizontal) {
            xPoints = new int[] {
                x, x + offset, x + width - offset, x + width,
                x + width - offset, x + offset
            };
            yPoints = new int[] {
                y + height/2, y, y, y + height/2,
                y + height, y + height
            };
        } else {
            xPoints = new int[] {
                x + width/2, x + width, x + width, x + width/2,
                x, x
            };
            yPoints = new int[] {
                y, y + offset, y + height - offset, y + height,
                y + height - offset, y + offset
            };
        }
        
        g.fillPolygon(xPoints, yPoints, 6);
    }

    // 绘制六边形点（用于冒号）
    private void drawHexagonDot(Graphics2D g, int x, int y, int radius) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            xPoints[i] = x + (int)(radius * Math.cos(angle));
            yPoints[i] = y + (int)(radius * Math.sin(angle));
        }
        
        g.fillPolygon(xPoints, yPoints, 6);
    }

    public void paintDayPhase(Graphics2D g, int frameWidth, int frameHeight) {
        // 绘制时间阶段的滤镜效果
        g.setColor(currentPhase.overlay);
        g.fillRect(0, 0, frameWidth, frameHeight);
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

}