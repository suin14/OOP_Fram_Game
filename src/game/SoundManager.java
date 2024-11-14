package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "audio" + File.separator;

    // 播放背景音乐
    public void playBGM() {
        try {
            File bgmFile = new File(path + "bgm.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bgmFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 播放声效
}