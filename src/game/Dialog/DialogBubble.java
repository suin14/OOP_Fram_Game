package game.Dialog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogBubble {

    public static void talk(String dialogWith, String key) {
        String message = loadMessageFromJSON(dialogWith,key);
        System.out.println(message);
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
