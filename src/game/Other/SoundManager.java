package game.Other;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private Clip bgmClip;
    private Clip sfxClip;

    // 静态变量用于保存唯一的实例
    private static SoundManager instance;

    private SoundManager() {}

    // 获取单例实例
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // 播放背景音乐
    public void playBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            return;
        }

        try {
            URL bgmUrl = getClass().getClassLoader().getResource("assets/audio/bgm.wav");
            if (bgmUrl == null) {
                System.err.println("无法找到音频资源: assets/audio/bgm.wav");
                return;
            }

            AudioInputStream bgmStream = AudioSystem.getAudioInputStream(bgmUrl);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(bgmStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    // 播放声效
    public void playSFX(String sfxPath) {
        try {
            URL sfxUrl = getClass().getClassLoader().getResource("assets/audio/" + sfxPath);

            if (sfxUrl == null) {
                System.err.println("无法找到音效文件: assets/audio/" + sfxPath);
                return;
            }

            AudioInputStream sfxStream = AudioSystem.getAudioInputStream(sfxUrl);
            sfxClip = AudioSystem.getClip();
            sfxClip.open(sfxStream);
            sfxClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
