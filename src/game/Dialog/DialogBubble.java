package game.Dialog;

import game.Other.StaticValue;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DialogBubble extends JPanel {
    private static DialogBubble instance;

    private static boolean isTalking = false;

    private static String name;
    private static String message;

    private DialogBubble() {
        isTalking = false;
        name = "NAME";
        message = "MESSAGE";
    }

    public static DialogBubble getInstance() {
        if (instance == null) {
            instance = new DialogBubble();
        }
        return instance;
    }

    public static void startTalking() {
        isTalking = true;
    }

    public static void stopTalking() {
        isTalking = false;
    }

    public static boolean isTalking() {
        return isTalking;
    }


    // 渲染对话框
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 绘制对话框图片
        g2d.drawImage(StaticValue.dialogBubble, 100, 390, 720, 240, this);

        // 设置名字
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.GRAY);
        g2d.drawString(name, 170, 460);

        // 设置对话信息
        g2d.setFont(new Font("黑体", Font.BOLD, 20));
        g2d.setColor(Color.GRAY);
        g2d.drawString(message, 170, 550);
    }


    public static void talk(String dialogWith, String key) {
        startTalking();
        name = dialogWith;
        message = loadMessageFromJSON(dialogWith,key);
//        System.out.println(message);
    }

    // 读取并解析 JSON 文件
    private static String loadMessageFromJSON(String FileName, String key) {
        InputStream inputStream = DialogBubble.class.getClassLoader().getResourceAsStream("assets/dialogue/" + FileName + ".json");
        if (inputStream == null) {
            return "对话文件未找到:" + "assets/dialogue/" + FileName + ".json";
        }

        StringBuilder message = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(message.toString());
        return parseJSON(message.toString(), key);
    }

    private static String parseJSON(String json, String key) {
        String regex = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "没有找到该对话";
    }
}
