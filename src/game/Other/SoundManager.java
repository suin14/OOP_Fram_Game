package game.Other;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private static Clip bgmClip;
    private static Clip sfxClip;

    // 播放背景音乐
    public static void playBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            return;
        }

        try {
            URL bgmUrl = SoundManager.class.getClassLoader().getResource("assets/audio/bgm.wav");
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

    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning())
            bgmClip.stop();
    }


    // 播放声效
    public static void playSFX(String sfxPath) {
        try {
            URL sfxUrl = SoundManager.class.getClassLoader().getResource("assets/audio/" + sfxPath);

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
