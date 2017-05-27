package phdev.com.br.metafighter;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class SoundManager {

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    public SoundManager(){

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    public void release(){
        mediaPlayer.release();
        soundPool.release();
    }
}
