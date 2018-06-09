package cz.ophite.mimic.vhackos.botnet.utils;

import cz.ophite.mimic.vhackos.botnet.shared.SharedConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Audio přehrávač.
 *
 * @author mimic
 */
public final class AudioPlayer {

    private static final Logger LOG = LoggerFactory.getLogger(AudioPlayer.class);

    /**
     * Spustí přehrávání.
     */
    public static void play(InputStream stream) {
        if (stream == null || SharedConst.DEBUG) {
            return;
        }
        var t = new Thread(new AudioProcess(stream));
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    private static final class AudioProcess implements Runnable {

        private final InputStream stream;

        private AudioProcess(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            try {
                try (var audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(stream))) {
                    var format = audioIn.getFormat();
                    var info = new DataLine.Info(Clip.class, format);

                    try (var clip = (Clip) AudioSystem.getLine(info)) {
                        var frames = (double) audioIn.getFrameLength();
                        var secLength = (int) (frames / format.getFrameRate());

                        clip.open(audioIn);
                        clip.start();
                        clip.drain();

                        Thread.sleep((secLength + (secLength / 2)) * 1000 + 500);
                    }
                }
            } catch (Exception e) {
                LOG.error("There was an error while playing audio", e);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    LOG.error("There was an unexpected error when closing the audio stream", e);
                }
            }
        }
    }
}
