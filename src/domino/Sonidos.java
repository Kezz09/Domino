package domino;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sonidos {
    private static Clip backgroundClip;

    public static void playSound(String nombreArchivo) {
        try {
            URL url = Sonidos.class.getClassLoader().getResource(nombreArchivo);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void playBackground(String nombreArchivo, boolean loop) {
        stopBackground();
        try {
            URL url = Sonidos.class.getClassLoader().getResource(nombreArchivo);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);

            // Volumen al 50%
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float gain = (float) (min + (max - min) * 0.8);
            gainControl.setValue(gain);

            if (loop) backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            else backgroundClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void stopBackground() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }
}
