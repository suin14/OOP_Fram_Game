package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "audio" + File.separator;

    private Clip bgmClip;

    private Clip sfxClip;

    // 播放背景音乐
    public void playBGM() {
        try {
            File bgmFile = new File(path + "bgm.wav");
            AudioInputStream bgmStream = AudioSystem.getAudioInputStream(bgmFile);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(bgmStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 播放声效
    public void playSFX(String sfx) {
        try {
            File sfxFile = new File(path + sfx + ".wav");
            AudioInputStream sfxStream = AudioSystem.getAudioInputStream(sfxFile);
            sfxClip = AudioSystem.getClip();
            sfxClip.open(sfxStream);
            sfxClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}